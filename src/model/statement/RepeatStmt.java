package model.statement;

import model.ADT.IDict;
import model.exceptions.MyException;
import model.exceptions.StatementException;
import model.expression.IExpr;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.IType;

public class RepeatStmt implements IStmt{
    private final IStmt stmt;
    private final IExpr exp;

    public RepeatStmt(IStmt stmt, IExpr exp){
        this.stmt = stmt;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        //pop in one step
        IStmt converted = new CompoundStmt(
                stmt,
                new WhileStmt(exp, stmt)
        );
        state.getExeStack().push(converted);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new RepeatStmt(stmt.deepCopy(), exp.deepCopy());
    }


    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        IType typeExp = exp.typecheck(typeEnv);
        if( typeExp.equals(new BoolType()) ){
            stmt.typecheck(typeEnv.deepCopy());
            return typeEnv;
        }else{
            throw new MyException("Conditions is not bool");
        }
    }

    @Override
    public String toString() {
        return "repeat (" + stmt.toString() + ") as (" + exp.toString() + ")";}
}
