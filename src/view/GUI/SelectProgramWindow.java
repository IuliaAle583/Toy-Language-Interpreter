package view.GUI;

import controller.Controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ADT.*;
import model.state.ProgramState;
import model.statement.IStmt;
import repository.Repository;
import view.AllExamples;

public class SelectProgramWindow {

    public SelectProgramWindow(Stage stage) {
        ListView<IStmt> listView = new ListView<>();
        listView.setItems(
                FXCollections.observableArrayList(AllExamples.getAll())
        );

        listView.setStyle("-fx-control-inner-background: #EAF9F7; -fx-background-color: transparent;");

        Button runBtn = new Button("Run selected program");
        // teal style used for buttons
        String btnBaseColor = "#A8E6DD";
        String btnHoverColor = "#8FD9CD";
        String btnTextColor = "#064B47";
        String btnStyle = String.format(
                "-fx-background-color: %s; -fx-text-fill: %s; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 12;",
                btnBaseColor, btnTextColor
        );
        String btnHoverStyle = String.format(
                "-fx-background-color: %s; -fx-text-fill: %s; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 12;",
                btnHoverColor, btnTextColor
        );

        runBtn.setStyle(btnStyle);

        Button exitBtn = new Button("Exit");
        exitBtn.setStyle(btnStyle);
        exitBtn.setOnAction(ev -> Platform.exit());

        runBtn.setOnMouseEntered(ev -> runBtn.setStyle(btnHoverStyle));
        runBtn.setOnMouseExited(ev -> runBtn.setStyle(btnStyle));
        exitBtn.setOnMouseEntered(ev -> exitBtn.setStyle(btnHoverStyle));
        exitBtn.setOnMouseExited(ev -> exitBtn.setStyle(btnStyle));

        runBtn.setOnAction(e -> {
            int selectedIndex = listView.getSelectionModel().getSelectedIndex();
            IStmt stmt = listView.getSelectionModel().getSelectedItem();

            if (stmt == null) {
                new Alert(Alert.AlertType.ERROR, "Select a program first").show();
                return;
            }

            String logFilePath = "log" + (selectedIndex + 1) + ".txt";

            ProgramState prg = new ProgramState(
                    new MyStack<>(),
                    new MyDict<>(),
                    new model.ADT.MyList<>(),
                    new MyDict<>(),
                    new MyHeap<>(),
                    new BarrierTable(),
                    stmt
            );

            try {
                Repository repo = new Repository(prg, logFilePath);
                Controller ctrl = new Controller(repo);
                new view.GUI.InterpreterWindow(ctrl);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });

        HBox buttons = new HBox(10, runBtn, exitBtn);
        VBox root = new VBox(10, listView, buttons);

        root.setStyle("-fx-background-color: #EAF9F7; -fx-padding: 12;");

        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Select program");
        stage.show();
    }
}