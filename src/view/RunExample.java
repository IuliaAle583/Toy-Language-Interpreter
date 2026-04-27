package view;

import controller.Controller;
import model.exceptions.MyException;
import model.state.ProgramState;
import model.value.IValue;
import repository.IRepository;

public class RunExample extends Command {
    private final Controller ctrl;

    public RunExample(String key, String desc, Controller ctrl) {
        super(key, desc);
        this.ctrl = ctrl;
    }

    @Override
    public void execute() {
        try {
            // 1. Get the initial program state (which holds the shared Out list)
            // Assuming your repository contains the initial state list.
            // We retrieve the first one because the Out list is shared.
            IRepository repo = ctrl.getRepo(); // You likely need a getter for the repository in Controller
            ProgramState initialPrg = repo.getProgramList().get(0);

            // 2. Run the program (Execution loop finishes here)
            ctrl.allStep();

            // 3. RETRIEVE AND PRINT THE FINAL OUTPUT
            System.out.println("--- Final Output (Out List) ---");

            // Iterate over the shared Out list contents (which were updated during allStep)
            // Assuming prg.getOut().getContent() returns an iterable collection (like List or Map values)
            for (IValue val : initialPrg.getOut().getContent()) {
                System.out.println(val.toString());
            }

        } catch (MyException e) {
            System.out.println("RUNTIME ERROR: " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            // Catches error if repo list is empty (e.g., if ProgramState list was reset prematurely)
            System.out.println("PROGRAM ENDED. Final output retrieved from shared list.");
        }
    }
}