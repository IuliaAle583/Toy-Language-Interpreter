# Toy-Language-Interpreter

A Java-based interpreter designed to execute a custom-defined programming language. The project demonstrates core concepts of language processing, memory management, and advanced concurrency synchronization mechanisms using a Model-View-Controller (MVC) architecture and a JavaFX graphical interface.

## Key Technical Features

### Multi-Threading & Concurrency
The interpreter supports parallel execution of multiple programs or threads.

- **ExecutorService:** Managed thread pool used to run concurrent PrgState (Program State) instances.
- **Garbage Collector:** A custom-implemented safe memory reclamation mechanism that runs during execution to clean up unreferenced addresses in the Heap.

### Synchronization Mechanisms
Advanced flow control primitives implemented to handle concurrent access:
- **Barrier:** Used to synchronize multiple threads at a specific point, ensuring they all reach the barrier before any can proceed.
- **Lock Mechanism:** Ensuring thread-safe operations on shared resources like the Heap or File Table.

### JavaFX Graphical Interface
The application features a modern GUI built with JavaFX that provides real-time visualization of the execution:

- **Program State Monitors:** Tables and lists for viewing the Symbol Table, Heap, File Table, and Output.
- **Execution Step-by-Step:** Control the interpreter flow with a "Run One Step" mechanism to observe how memory and thread states change.
- **Program Selection:** A dashboard to choose between multiple predefined programs.

### Data Structures (Interpreter Internals)
- **ExeStack:** A stack representing the control flow of the current thread.
- **SymTable:** A dictionary mapping variable names to their values.
- **Heap:** A shared memory space for dynamic allocation (Ref values).
- **FileTable:** Keeps track of open files for I/O operations.

## Built With
- **Java 17+:** Core logic and syntax processing.
- **JavaFX:** For the desktop graphical user interface.
- **SceneBuilder:** For UI layout design.

## Project Architecture

The project is strictly organized to separate concerns:

- **Model:** Defines the expressions (Exp), statements (Stmt), and the PrgState.
- **Repository:** Manages the collection of program states and handles log file persistence.
- **Controller:** Contains the execution logic and the garbage collection algorithm.
- **View:** The JavaFX layer that interacts with the user.
