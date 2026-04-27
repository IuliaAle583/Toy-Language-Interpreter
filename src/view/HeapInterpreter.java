package view;

import controller.Controller;
import model.ADT.*;
import model.statement.IStmt;
import model.type.IntType;
import model.type.RefType;
import model.value.*;
import repository.IRepository;
import repository.Repository;

import java.io.BufferedReader;
import java.util.List;
import java.util.Scanner;

public class HeapInterpreter {

    public static void main(String[] args) {
        List<IStmt> examples = AllExamples.getAll();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select example to run:");
            System.out.println("0 : exit");
            for (int i = 0; i < examples.size(); i++) {
                System.out.println((i + 1) + " : " + examples.get(i).toString());
            }
            System.out.print("Input option: ");
            String line = scanner.nextLine().trim();
            int opt;
            try {
                opt = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (opt == 0) {
                System.out.println("Exiting.");
                break;
            }

            int idx = opt - 1;
            if (idx < 0 || idx >= examples.size()) {
                System.out.println("Invalid option. Choose a number between 0 and " + examples.size());
                continue;
            }

            IStmt program = examples.get(idx);
            String logFile = "heap_log" + opt + ".txt";
            System.out.println("=========== Running Example " + opt + " ===========");
            runExample(program, logFile);
            System.out.println("=========== Finished Example " + opt + " ===========");
            System.out.println();
        }

        scanner.close();
    }

    private static void runExample(IStmt program, String logFile) {
        IStack<IStmt> stack = new MyStack<>();
        IDict<String, IValue> sym = new MyDict<>();
        IList<IValue> out = new MyList<>();
        IDict<StringValue, BufferedReader> fileTable = new MyDict<>();
        IHeap<Integer, IValue> heap = new MyHeap<>();

        try {
            // Typecheck before execution!!!!
            program.typecheck(new MyDict<>());

            model.state.ProgramState prg = new model.state.ProgramState(stack, sym, out, fileTable, heap,new BarrierTable(), program);
            IRepository repo = new Repository(prg, logFile);
            Controller ctrl = new Controller(repo);
            ctrl.allStep();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return;
        }

        System.out.println("Heap:\n" + heap);
        System.out.println("SymTable:\n" + sym);
        System.out.println("Out:\n" + out);
    }

}