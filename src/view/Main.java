package view;

import controller.Controller;
import model.ADT.*;
import model.exceptions.MyException;
import model.expression.ArithExpr;
import model.expression.ValueExpr;
import model.expression.VariableExpr;
import model.state.ProgramState;
import model.statement.*;
import model.type.BoolType;
import model.type.IntType;
import model.type.StringType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.IValue;
import repository.IRepository;
import repository.Repository;

import java.io.BufferedReader;

public class Main {
    public static void main(String[] args) {
        try {
            IStmt ex1 =
                    new CompoundStmt(
                            new VariableDeclStmt("v", new IntType()),
                            new CompoundStmt(
                                    new AssignStmt(
                                            "v",
                                            new ValueExpr(new IntValue(2))
                                    ),
                                    new PrintStmt(
                                            new VariableExpr("v")
                                    )
                            )
                    );

            runExample("Example 1", ex1);

            IStmt ex2 =
                    new CompoundStmt(
                            new VariableDeclStmt("a", new IntType()),
                            new CompoundStmt(
                                    new VariableDeclStmt("b", new IntType()),
                                    new CompoundStmt(
                                            // a = 2 + 3 * 5;
                                            new AssignStmt(
                                                    "a",
                                                    new ArithExpr(
                                                            new ValueExpr(new IntValue(2)),
                                                            new ArithExpr(
                                                                    new ValueExpr(new IntValue(3)),
                                                                    new ValueExpr(new IntValue(5)),
                                                                    3
                                                            ),
                                                            1
                                                    )
                                            ),
                                            new CompoundStmt(
                                                    new AssignStmt(
                                                            "b",
                                                            new ArithExpr(
                                                                    new ArithExpr(
                                                                            new VariableExpr("a"),
                                                                            new ArithExpr(
                                                                                    new ValueExpr(new IntValue(4)),
                                                                                    new ValueExpr(new IntValue(2)),
                                                                                    4
                                                                            ),
                                                                            2
                                                                    ),
                                                                    new ValueExpr(new IntValue(7)),
                                                                    1
                                                            )
                                                    ),
                                                    new PrintStmt(
                                                            new VariableExpr("b")
                                                    )
                                            )
                                    )
                            )
                    );

            runExample("Example 2", ex2);

            IStmt ex3 =
                    new CompoundStmt(
                            new VariableDeclStmt("a", new BoolType()),
                            new CompoundStmt(
                                    new AssignStmt(
                                            "a",
                                            new ValueExpr(new BoolValue(true))
                                    ),
                                    new CompoundStmt(
                                            new VariableDeclStmt("v", new IntType()),
                                            new CompoundStmt(
                                                    new IfStmt(
                                                            new VariableExpr("a"),
                                                            new AssignStmt(
                                                                    "v",
                                                                    new ValueExpr(new IntValue(2))
                                                            ),
                                                            new AssignStmt(
                                                                    "v",
                                                                    new ValueExpr(new IntValue(3))
                                                            )
                                                    ),
                                                    new PrintStmt(
                                                            new VariableExpr("v")
                                                    )
                                            )
                                    )
                            )
                    );

            runExample("Example 3", ex3);

            IStmt ex4 =
                    new CompoundStmt(
                            new VariableDeclStmt("varf", new StringType()),
                            new CompoundStmt(
                                    new AssignStmt("varf", new ValueExpr(new StringValue("test.in"))),
                                    new CompoundStmt(
                                            new OpenRFile(new VariableExpr("varf")),
                                            new CompoundStmt(
                                                    new VariableDeclStmt("varc", new IntType()),
                                                    new CompoundStmt(
                                                            new ReadFile(new VariableExpr("varf"), "varc"),
                                                            new CompoundStmt(
                                                                    new PrintStmt(new VariableExpr("varc")),
                                                                    new CompoundStmt(
                                                                            new ReadFile(new VariableExpr("varf"), "varc"),
                                                                            new CompoundStmt(
                                                                                    new PrintStmt(new VariableExpr("varc")),
                                                                                    new CloseRFile(new VariableExpr("varf"))
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    );

            runExample("Example 4 - File operations", ex4);

        } catch (MyException e) {
            System.out.println("RUNTIME ERROR: " + e.getMessage());
        }
    }

    private static void runExample(String title, IStmt program) throws MyException {
        System.out.println("=========== " + title + " ===========");

        IStack<IStmt> stack = new MyStack<>();
        IDict<String, IValue> sym = new MyDict<>();
        IList<IValue> out = new MyList<>();
        IDict<StringValue, BufferedReader> fileTable = new MyDict<>();
        IHeap<Integer, IValue> heap = new MyHeap<>();

        ProgramState prg = new ProgramState(stack, sym, out, fileTable, heap, new BarrierTable(),program);

        String logPath = "log.txt";
        IRepository repo = new Repository(prg, logPath);
        Controller ctrl = new Controller(repo);

        ctrl.allStep();

        System.out.println("Final Out: " + prg.getOut().toString());
        System.out.println();
    }
}