package repository;

import model.exceptions.MyException;
import model.state.ProgramState;

import java.util.List;

public interface IRepository {
    ProgramState getCurrentProgram();

    List<ProgramState> getProgramList();

    void setProgramList(List<ProgramState> list);

    void addProgram(ProgramState prg);

    void logPrgStateExec(ProgramState prg) throws MyException;
}