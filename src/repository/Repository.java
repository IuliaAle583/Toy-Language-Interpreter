package repository;

import model.exceptions.MyException;
import model.state.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<ProgramState> prgList;
    private final String logFilePath;

    public Repository(ProgramState initial, String logFilePath) throws MyException {
        this.prgList = new ArrayList<>();
        this.prgList.add(initial);
        this.logFilePath = logFilePath;

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, false)))) {
            pw.print("");
        } catch (IOException e) {
            throw new MyException("Cannot open log file: " + e.getMessage());
        }
    }

    @Override
    public ProgramState getCurrentProgram() {
        return prgList.get(0);
    }

    @Override
    public List<ProgramState> getProgramList() {
        return prgList;
    }

    @Override
    public void setProgramList(List<ProgramState> list) {
        this.prgList = list;
    }

    @Override
    public void addProgram(ProgramState prg) {
        prgList.add(prg);
    }

    @Override
    public void logPrgStateExec(ProgramState prg) throws MyException {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            pw.println(prg.toString());
            pw.println("====");
        } catch (IOException e) {
            throw new MyException("Logging failed: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return prgList.toString();
    }
}