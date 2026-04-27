package model.statement;

import model.ADT.IDict;
import model.exceptions.ADTException;
import model.exceptions.MyException;
import model.exceptions.StatementException;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntType;
import model.value.IntValue;
import model.value.IValue;
import javafx.util.Pair;
import java.util.List;

public class AwaitStmt implements IStmt {
    private final String var;

    public AwaitStmt(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        try{
            IDict<String, IValue> symTable = state.getSymTable();
            if(!symTable.isDefined(var)){
                throw new StatementException("Await: variable " + var + " not defined");
            }
            IValue val = symTable.lookup(var);
            if(!(val.getType().equals(new IntType()))){
                throw new StatementException("Await: variable " + var + " is not of type int");
            }
            int index = ((IntValue) val).getValue();    //aka id

            synchronized(state.getBarrierTable()){
                if(!state.getBarrierTable().contains(index)){
                    throw new StatementException("Await: variable " + var + " not found");
                }

                Pair<Integer, List<Integer>> entry= state.getBarrierTable().get(index);
                int N=entry.getKey();   //cate trb sa ajunga
                List<Integer> list= entry.getValue();
                int NL=list.size(); //cate au ajuns

                if(NL<N){
                    if(!list.contains(state.getId())){
                        list.add(state.getId());
                    }
                    //same push for both
                    state.getExeStack().push(this);
                }
            }

            return null;
        } catch (ADTException e) {
            throw new StatementException("Await ADT error: " + e.getMessage());
        }
    }

    @Override
    public IStmt deepCopy() {
        return new AwaitStmt(this.var);
    }


    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        IType typeVar=typeEnv.lookup(var);
        if(typeVar.equals(new IntType())){
            return typeEnv;
        }else{
            throw new MyException("Await: variable " + var + " is not of type int");
        }
    }

    @Override
    public String toString()
    {
        return "await(" + var + ")";

    }
}
