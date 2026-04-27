package model.statement;

import model.ADT.IDict;
import model.exceptions.ExpressionException;
import model.exceptions.MyException;
import model.exceptions.StatementException;
import model.expression.IExpr;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.ArrayList;

public class NewBarrierStmt implements IStmt{
    private final String var;
    private final IExpr exp;

    public NewBarrierStmt(String var, IExpr exp){
        this.var = var;
        this.exp = exp;
    }


    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        try{
            synchronized (state.getBarrierTable()) {
                //val = capacity of barrier
                IValue val = exp.eval(state.getSymTable(), state.getHeap());
                if (!val.getType().equals(new IntType()))
                    throw new StatementException("Expression not integer");
                //nr threads
                int nr = ((IntValue) val).getValue();
                //i do and get the address where i put the barrier in the barrier table
                int address = state.getBarrierTable().put(nr, new ArrayList<>());

                if (state.getSymTable().isDefined(var)) {
                    //variable for address is int - check
                    IType typeVar = state.getSymTable().lookup(var).getType();
                    if (typeVar.equals(new IntType())) {
                        state.getSymTable().update(var, new IntValue(address));
                    } else {
                        throw new StatementException("Variable " + var + " is defined but is not of type Int");
                    }
                } else {
                    throw new StatementException("Variable " + var + " was not declared before use");
                }
            }

            return null;
        } catch (Exception e) {
            throw new StatementException(e.getMessage());
        }

    }

    @Override
    public IStmt deepCopy() {
        return new NewBarrierStmt(var, exp.deepCopy());
}

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        if (!typeEnv.lookup(var).equals(new IntType()))
            throw new MyException("Var not int");
        if (!exp.typecheck(typeEnv).equals(new IntType()))
            throw new MyException("Exp not int");
        return typeEnv;
    }


    @Override
    public String toString() {
        return "newBarrier(" + var + ", " + exp.toString() + ")";
    }
}


