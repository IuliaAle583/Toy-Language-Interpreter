package controller;

import model.ADT.IHeap;
import model.ADT.IStack;
import model.exceptions.MyException;
import model.exceptions.StatementException;
import model.state.ProgramState;
import model.statement.IStmt;
import model.value.RefValue;
import model.value.IValue;
import repository.IRepository;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private final IRepository repo;
    private ExecutorService executor;

    public Controller(IRepository repo) {
        this.repo = repo;
        //this.executor = Executors.newFixedThreadPool(5); il initializez in allStep
    }

    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }


    void oneStepForAllPrg(List<ProgramState> prgList) throws InterruptedException {

        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (MyException e) {
                System.out.println("Log error before step: " + e.getMessage());
            }
        });

        List<Callable<ProgramState>> callList = prgList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) p::oneStep)
                .collect(Collectors.toList());

        // Execute all threads and filter out errors
        List<ProgramState> newStates = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        System.err.println("Execution error in thread (Future.get): " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Keep only actually spawned/new states (those not already in prgList)
        List<ProgramState> spawnedStates = newStates.stream()
                .filter(ns -> prgList.stream().noneMatch(p -> p.getId() == ns.getId()))
                .collect(Collectors.toList());

        // Merge existing programs with spawned ones
        List<ProgramState> allPrograms = new ArrayList<>(prgList);
        allPrograms.addAll(spawnedStates);

        // Deduplicate by id (preserve insertion order)
        Map<Integer, ProgramState> uniqById = new LinkedHashMap<>();
        for (ProgramState p : allPrograms) {
            uniqById.put(p.getId(), p);
        }
        List<ProgramState> merged = new ArrayList<>(uniqById.values());

        // Log after execution for the merged list
        merged.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (MyException e) {
                System.out.println("Log error after step: " + e.getMessage());
            }
        });

        // Save the merged list (keep completed programs so the GUI can show their final state).
        // The batch runner (allStep) will remove completed programs between iterations.
        repo.setProgramList(merged);
    }

    public void allStep() throws MyException {
        executor = Executors.newFixedThreadPool(2);

        List<ProgramState> prgList = removeCompletedPrg(repo.getProgramList());

        while (!prgList.isEmpty()) {

            IHeap<Integer, IValue> heap = prgList.get(0).getHeap();

            // Collect all root addresses from all SymTables
            List<Integer> allRoots = prgList.stream()
                    .flatMap(p -> p.getSymTable().getContent().values().stream())
                    .filter(v -> v instanceof RefValue)
                    .map(v -> ((RefValue) v).getAddress())
                    .collect(Collectors.toList());

            // Apply the recursive GC: keeps all roots and recursively reachable addresses
            heap.setContent(
                    safeGarbageCollector(allRoots, heap.getContent())
            );

            try {
                oneStepForAllPrg(prgList);
            } catch (InterruptedException e) {
                executor.shutdownNow();
                throw new MyException("Program interrupted: " + e.getMessage());
            }

            // update prgList for next iteration — remove completed programs now
            prgList = removeCompletedPrg(repo.getProgramList());
        }

        executor.shutdownNow();
        repo.setProgramList(prgList); // Should be empty at the end
    }




    private List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddress())
                .collect(Collectors.toList());
    }

    private Map<Integer, IValue> safeGarbageCollector(List<Integer> roots,
                                                      Map<Integer, IValue> heapContent) {

        Set<Integer> reachable = new HashSet<>(roots);
        Deque<Integer> workList = new ArrayDeque<>(roots);

        while (!workList.isEmpty()) {
            int addr = workList.removeFirst();
            IValue v = heapContent.get(addr);

            // Safety check for existing address
            if (v == null) continue;

            // Check if the value stored in the heap is a reference to another location
            if (v instanceof RefValue refVal) {
                int innerAddr = refVal.getAddress();

                // Check if the inner address is valid AND!!!! not yet marked
                if (heapContent.containsKey(innerAddr) && !reachable.contains(innerAddr)) {
                    reachable.add(innerAddr);
                    workList.add(innerAddr);
                }
            }
        }

        return heapContent.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public IRepository getRepo() {
        return repo;
    }


    public void oneStepGUI() throws MyException, InterruptedException {
        if (executor == null) {
            executor = Executors.newFixedThreadPool(2);
        }

        repo.setProgramList(
                removeCompletedPrg(repo.getProgramList())
        );

        if (repo.getProgramList().isEmpty()) {
            executor.shutdownNow();
            executor = null;
            return;
        }

        oneStepForAllPrg(repo.getProgramList());

        // if after the step there are no programs left, clean up executor
        if (repo.getProgramList().isEmpty()) {
            executor.shutdownNow();
            executor = null;
        }
    }
}