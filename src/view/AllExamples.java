package view;

import model.expression.*;
import model.statement.*;
import model.type.*;
import model.value.*;

import java.util.List;

public class AllExamples {

    public static List<IStmt> getAll() {
        return List.of(
                buildExample1(),
                buildExample2(),
                buildExample3(),
                buildExample4_FileIO(),
                buildExample5_Relational(),
                buildExample6_While(),
                buildExample7_TypecheckFail(),

                buildEx1(),
                buildEx2(),
                buildEx3(),
                buildHeapExampleGC(),
                buildForkExampleComplex(),
                buildRepeatExample(),
                buildExampleBarrier()
        );
    }

    private static IStmt buildRepeatExample() {
        IStmt forkBody = new CompoundStmt(
                new PrintStmt(new VariableExpr("v")),
                new CompoundStmt(
                        new AssignStmt("x", new ArithExpr(new VariableExpr("x"), new ValueExpr(new IntValue(1)), 2)), // x = x - 1
                        new PrintStmt(new VariableExpr("x"))
                )
        );

        IStmt repeatBody = new CompoundStmt(
                new ForkStmt(forkBody),
                new AssignStmt("v", new ArithExpr(new VariableExpr("v"), new ValueExpr(new IntValue(1)), 1)) // v = v + 1
        );

        return new CompoundStmt(new VariableDeclStmt("v", new IntType()),
                new CompoundStmt(new VariableDeclStmt("x", new IntType()),
                new CompoundStmt(new AssignStmt("v", new ValueExpr(new IntValue(3))),
                new CompoundStmt(new AssignStmt("x", new ValueExpr(new IntValue(2))),
                new CompoundStmt(new RepeatStmt(repeatBody, new RelExpr(new VariableExpr("v"), new ValueExpr(new IntValue(0)), 3)), // v == 0
                new CompoundStmt(new NoOpStmt(),
                new CompoundStmt(new NoOpStmt(),
                new CompoundStmt(new NoOpStmt(),
                new CompoundStmt(new NoOpStmt(),
                new CompoundStmt(new NoOpStmt(),
                new CompoundStmt(new NoOpStmt(),
                new CompoundStmt(new NoOpStmt(),
                new PrintStmt(new VariableExpr("x"))))))))))))));
    }

    private static IStmt buildExampleBarrier(){
        IStmt fork1 = new ForkStmt(new CompoundStmt(new AwaitStmt("id"),
                new CompoundStmt(new WriteHeapStmt("v1", new ArithExpr(new ReadHeapExpr(new VariableExpr("v1")), new ValueExpr(new IntValue(10)), 3)),
                        new PrintStmt(new ReadHeapExpr(new VariableExpr("v1"))))));

        IStmt fork2 = new ForkStmt(new CompoundStmt(new AwaitStmt("id"),
                new CompoundStmt(new WriteHeapStmt("v2", new ArithExpr(new ReadHeapExpr(new VariableExpr("v2")), new ValueExpr(new IntValue(10)), 3)),
                        new CompoundStmt(new WriteHeapStmt("v2", new ArithExpr(new ReadHeapExpr(new VariableExpr("v2")), new ValueExpr(new IntValue(10)), 3)),
                                new CompoundStmt(new WriteHeapStmt("v2", new ArithExpr(new ReadHeapExpr(new VariableExpr("v2")), new ValueExpr(new IntValue(10)), 3)),
                                        new PrintStmt(new ReadHeapExpr(new VariableExpr("v2"))))))));

        IStmt fork3= new ForkStmt(new CompoundStmt(new AwaitStmt("id"),
                new PrintStmt(new ArithExpr(new ReadHeapExpr(new VariableExpr("v3")),
                        new ValueExpr(new IntValue(40)),3))));

        return new CompoundStmt(new VariableDeclStmt("v1", new RefType(new IntType())),
                new CompoundStmt(new VariableDeclStmt("v2", new RefType(new IntType())),
                new CompoundStmt(new VariableDeclStmt("v3", new RefType(new IntType())),
                new CompoundStmt(new VariableDeclStmt("id", new IntType()),
                new CompoundStmt(new AssignStmt("id", new ValueExpr(new IntValue(5))),
                new CompoundStmt(new NewStmt("v1", new ValueExpr(new IntValue(2))),
                new CompoundStmt(new NewStmt("v2", new ValueExpr(new IntValue(3))),
                new CompoundStmt(new NewStmt("v3", new ValueExpr(new IntValue(4))),
                new CompoundStmt(new NewBarrierStmt("id", new ReadHeapExpr(new VariableExpr("v2"))),
                new CompoundStmt(fork1,
                new CompoundStmt(fork2,
                new CompoundStmt(fork3,
                new PrintStmt(new ReadHeapExpr(new VariableExpr("v3")))
                ))))))))))));

    }


    private static IStmt buildExample1() {
        return new CompoundStmt(
                new VariableDeclStmt("v", new IntType()),
                new CompoundStmt(
                        new AssignStmt("v", new ValueExpr(new IntValue(2))),
                        new PrintStmt(new VariableExpr("v"))
                )
        );
    }

    private static IStmt buildExample2() {
        return new CompoundStmt(
                new VariableDeclStmt("a", new IntType()),
                new CompoundStmt(
                        new VariableDeclStmt("b", new IntType()),
                        new CompoundStmt(
                                new AssignStmt("a",
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
                                        new AssignStmt("b",
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
                                        new PrintStmt(new VariableExpr("b"))
                                )
                        )
                )
        );
    }

    private static IStmt buildExample3() {
        return new CompoundStmt(
                new VariableDeclStmt("a", new BoolType()),
                new CompoundStmt(
                        new AssignStmt("a", new ValueExpr(new BoolValue(true))),
                        new CompoundStmt(
                                new VariableDeclStmt("v", new IntType()),
                                new CompoundStmt(
                                        new IfStmt(
                                                new VariableExpr("a"),
                                                new AssignStmt("v", new ValueExpr(new IntValue(2))),
                                                new AssignStmt("v", new ValueExpr(new IntValue(3)))
                                        ),
                                        new PrintStmt(new VariableExpr("v"))
                                )
                        )
                )
        );
    }

    private static IStmt buildExample4_FileIO() {
        return new CompoundStmt(
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
    }

    private static IStmt buildExample5_Relational() {
        return new CompoundStmt(
                new VariableDeclStmt("a", new IntType()),
                new CompoundStmt(
                        new VariableDeclStmt("b", new IntType()),
                        new CompoundStmt(
                                new AssignStmt("a", new ValueExpr(new IntValue(10))),
                                new CompoundStmt(
                                        new AssignStmt("b", new ValueExpr(new IntValue(20))),
                                        new PrintStmt(new RelExpr(
                                                new VariableExpr("a"),
                                                new VariableExpr("b"),
                                                1 // <
                                        ))
                                )
                        )
                )
        );
    }

    private static IStmt buildExample6_While() {
        return new CompoundStmt(
                new VariableDeclStmt("v", new IntType()),
                new CompoundStmt(
                        new AssignStmt("v", new ValueExpr(new IntValue(4))),
                        new CompoundStmt(
                                new WhileStmt(
                                        new RelExpr(new VariableExpr("v"), new ValueExpr(new IntValue(0)), 5), // v>0
                                        new CompoundStmt(
                                                new PrintStmt(new VariableExpr("v")),
                                                new AssignStmt("v", new ArithExpr(new VariableExpr("v"), new ValueExpr(new IntValue(1)), 2))
                                        )
                                ),
                                new PrintStmt(new VariableExpr("v"))
                        )
                )
        );
    }

    private static IStmt buildExample7_TypecheckFail() {
        return new CompoundStmt(
                new VariableDeclStmt("flag", new BoolType()),
                new AssignStmt("flag", new ValueExpr(new IntValue(42)))
        );
    }


    private static IStmt buildEx1() {
        return new CompoundStmt(
                new VariableDeclStmt("v", new RefType(new IntType())),
                new CompoundStmt(
                        new NewStmt("v", new ValueExpr(new IntValue(20))),
                        new CompoundStmt(
                                new VariableDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompoundStmt(
                                        new NewStmt("a", new VariableExpr("v")),
                                        new CompoundStmt(
                                                new PrintStmt(new VariableExpr("v")),
                                                new PrintStmt(new VariableExpr("a"))
                                        )
                                )
                        )
                )
        );
    }

    private static IStmt buildEx2() {
        return new CompoundStmt(
                new VariableDeclStmt("v", new RefType(new IntType())),
                new CompoundStmt(
                        new NewStmt("v", new ValueExpr(new IntValue(20))),
                        new CompoundStmt(
                                new VariableDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompoundStmt(
                                        new NewStmt("a", new VariableExpr("v")),
                                        new CompoundStmt(
                                                new PrintStmt(new ReadHeapExpr(new VariableExpr("v"))),
                                                new PrintStmt(new ArithExpr(
                                                        new ReadHeapExpr(new ReadHeapExpr(new VariableExpr("a"))),
                                                        new ValueExpr(new IntValue(5)),
                                                        1
                                                ))
                                        )
                                )
                        )
                )
        );
    }

    private static IStmt buildEx3() {
        return new CompoundStmt(
                new VariableDeclStmt("v", new RefType(new IntType())),
                new CompoundStmt(
                        new NewStmt("v", new ValueExpr(new IntValue(20))),
                        new CompoundStmt(
                                new PrintStmt(new ReadHeapExpr(new VariableExpr("v"))),
                                new CompoundStmt(
                                        new WriteHeapStmt("v", new ValueExpr(new IntValue(30))),
                                        new PrintStmt(new ArithExpr(
                                                new ReadHeapExpr(new VariableExpr("v")),
                                                new ValueExpr(new IntValue(5)),
                                                1
                                        ))
                                )
                        )
                )
        );
    }

    private static IStmt buildHeapExampleGC() {
        return new CompoundStmt(
                new VariableDeclStmt("v", new RefType(new IntType())),
                new CompoundStmt(
                        new NewStmt("v", new ValueExpr(new IntValue(20))),
                        new CompoundStmt(
                                new VariableDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompoundStmt(
                                        new NewStmt("a", new VariableExpr("v")),
                                        new CompoundStmt(
                                                new NewStmt("v", new ValueExpr(new IntValue(30))),
                                                new PrintStmt(new ReadHeapExpr(new ReadHeapExpr(new VariableExpr("a"))))
                                        )
                                )
                        )
                )
        );
    }

    private static IStmt buildForkExampleComplex() {
        return new CompoundStmt(
                new VariableDeclStmt("v", new IntType()),
                new CompoundStmt(
                        new VariableDeclStmt("a", new RefType(new IntType())),
                        new CompoundStmt(
                                new AssignStmt("v", new ValueExpr(new IntValue(10))),
                                new CompoundStmt(
                                        new NewStmt("a", new ValueExpr(new IntValue(22))),
                                        new CompoundStmt(
                                                new ForkStmt(
                                                        new CompoundStmt(
                                                                new WriteHeapStmt("a", new ValueExpr(new IntValue(30))),
                                                                new CompoundStmt(
                                                                        new AssignStmt("v", new ValueExpr(new IntValue(32))),
                                                                        new CompoundStmt(
                                                                                new PrintStmt(new VariableExpr("v")),
                                                                                new PrintStmt(new ReadHeapExpr(new VariableExpr("a")))
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new CompoundStmt(
                                                        new PrintStmt(new VariableExpr("v")),
                                                        new PrintStmt(new ReadHeapExpr(new VariableExpr("a")))
                                                )
                                        )
                                )
                        )
                )
        );
    }
}
