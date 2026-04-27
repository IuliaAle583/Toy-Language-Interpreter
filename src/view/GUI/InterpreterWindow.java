package view.GUI;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.state.ProgramState;
import model.value.IValue;
import model.value.StringValue;

import java.util.Map;
import java.util.List;

public class InterpreterWindow {

    private final Controller ctrl;

    private final ListView<Integer> prgIds = new ListView<>();
    private final ListView<String> exeStack = new ListView<>();
    private final ListView<IValue> out = new ListView<>();
    private final ListView<String> fileTable = new ListView<>();

    private final TableView<Map.Entry<Integer, IValue>> heapTable = new TableView<>();
    private final TableView<Map.Entry<String, IValue>> symTable = new TableView<>();
    private final TableView<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>> barrierTableView = new TableView<>();


    private final TextField nrPrgStates = new TextField();
    private final Button oneStepBtn = new Button("Run one step");

    public InterpreterWindow(Controller ctrl) {
        this.ctrl = ctrl;
        Stage stage = new Stage();

        setupHeapTable();
        setupSymTable();
        setupBarrierTable();

        TitledPane prgPane   = new TitledPane("Program States", prgIds);
        TitledPane stackPane = new TitledPane("Execution Stack", exeStack);
        TitledPane heapPane  = new TitledPane("Heap", heapTable);
        TitledPane symPane   = new TitledPane("Symbol Table", symTable);
        TitledPane outPane   = new TitledPane("Output", out);
        TitledPane filePane  = new TitledPane("File Table", fileTable);
        TitledPane barrierPane = new TitledPane("Barrier Table", barrierTableView);


        for (TitledPane p : new TitledPane[]{
                prgPane, stackPane, heapPane, symPane, outPane, filePane, barrierPane
        }) {
            p.setCollapsible(false);
        }

        VBox left = new VBox(10, prgPane, stackPane);
        VBox mid  = new VBox(10, heapPane, symPane, barrierPane);
        VBox right = new VBox(10, outPane, filePane);

        VBox.setVgrow(prgIds, Priority.ALWAYS);
        VBox.setVgrow(exeStack, Priority.ALWAYS);
        VBox.setVgrow(heapTable, Priority.ALWAYS);
        VBox.setVgrow(symTable, Priority.ALWAYS);
        VBox.setVgrow(out, Priority.ALWAYS);
        VBox.setVgrow(fileTable, Priority.ALWAYS);
        VBox.setVgrow(barrierTableView, Priority.ALWAYS);

        nrPrgStates.setEditable(false);
        nrPrgStates.setAlignment(Pos.CENTER);
        nrPrgStates.setMaxWidth(80);

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

        oneStepBtn.setStyle(btnStyle);
        oneStepBtn.setOnMouseEntered(ev -> oneStepBtn.setStyle(btnHoverStyle));
        oneStepBtn.setOnMouseExited(ev -> oneStepBtn.setStyle(btnStyle));

        HBox bottomBox = new HBox(10,
                new Label("Nr ProgramStates"),
                nrPrgStates,
                oneStepBtn
        );
        bottomBox.setAlignment(Pos.CENTER);
        right.getChildren().add(bottomBox);

        GridPane root = new GridPane();
        root.setPadding(new Insets(12));
        root.setHgap(12);
        root.setVgap(12);
        root.setAlignment(Pos.CENTER);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(30);
        c1.setHgrow(Priority.ALWAYS);

        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(40);
        c2.setHgrow(Priority.ALWAYS);

        ColumnConstraints c3 = new ColumnConstraints();
        c3.setPercentWidth(30);
        c3.setHgrow(Priority.ALWAYS);

        root.getColumnConstraints().addAll(c1, c2, c3);

        root.add(left, 0, 0);
        root.add(mid, 1, 0);
        root.add(right, 2, 0);

        root.setStyle("""
            -fx-font-family: "Segoe UI";
            -fx-font-size: 13px;
            -fx-background-color: #EAF9F7;
        """);

        String paleTeal = "#EAF9F7";
        String outBlue = "#D7E9FF";
        prgIds.setStyle("-fx-control-inner-background: " + paleTeal + ";");
        exeStack.setStyle("-fx-control-inner-background: " + paleTeal + ";");
        heapTable.setStyle("-fx-control-inner-background: " + paleTeal + "; -fx-background-color: transparent;");
        symTable.setStyle("-fx-control-inner-background: " + paleTeal + "; -fx-background-color: transparent;");
        fileTable.setStyle("-fx-control-inner-background: " + paleTeal + ";");
        barrierTableView.setStyle("-fx-control-inner-background: " + paleTeal + "; -fx-background-color: transparent;");

        out.setStyle(String.format("-fx-control-inner-background: %s; -fx-font-family: \"Monospaced\";", outBlue));

        // EVENTS
        prgIds.getSelectionModel().selectedItemProperty()
                .addListener((a, b, c) -> updateSelectedPrg());

        oneStepBtn.setOnAction(e -> runOneStep());

        // INIT
        updateAll();

        Scene scene = new Scene(root, 1100, 650);
        stage.setScene(scene);
        stage.setTitle("Interpreter");
        stage.show();
    }

    private void setupBarrierTable() {
        TableColumn<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>, Integer> indexCol = new TableColumn<>("Index");
        indexCol.setCellValueFactory(e -> new javafx.beans.property.SimpleIntegerProperty(e.getValue().getKey()).asObject());

        TableColumn<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>, Integer> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(e -> new javafx.beans.property.SimpleIntegerProperty(e.getValue().getValue().getKey()).asObject());

        TableColumn<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>, String> listCol = new TableColumn<>("List of IDs");
        listCol.setCellValueFactory(e -> new javafx.beans.property.SimpleStringProperty(e.getValue().getValue().getValue().toString()));

        barrierTableView.getColumns().addAll(indexCol, valueCol, listCol);

    }

    private void setupHeapTable() {
        TableColumn<Map.Entry<Integer, IValue>, Integer> addr =
                new TableColumn<>("Address");
        addr.setCellValueFactory(e ->
                new javafx.beans.property.SimpleIntegerProperty(e.getValue().getKey()).asObject());

        TableColumn<Map.Entry<Integer, IValue>, IValue> val =
                new TableColumn<>("Value");
        val.setCellValueFactory(e ->
                new javafx.beans.property.SimpleObjectProperty<>(e.getValue().getValue()));

        heapTable.getColumns().addAll(addr, val);
    }

    private void setupSymTable() {
        TableColumn<Map.Entry<String, IValue>, String> var =
                new TableColumn<>("Variable");
        var.setCellValueFactory(e ->
                new javafx.beans.property.SimpleStringProperty(e.getValue().getKey()));

        TableColumn<Map.Entry<String, IValue>, IValue> val =
                new TableColumn<>("Value");
        val.setCellValueFactory(e ->
                new javafx.beans.property.SimpleObjectProperty<>(e.getValue().getValue()));

        symTable.getColumns().addAll(var, val);
    }

    private void updateAll() {
        var prgs = ctrl.getRepo().getProgramList();

        prgIds.setItems(FXCollections.observableArrayList(
                prgs.stream().map(ProgramState::getId).toList()
        ));

        nrPrgStates.setText(String.valueOf(prgs.size()));

        if (prgs.isEmpty()) {
            exeStack.getItems().clear();
            symTable.getItems().clear();
            heapTable.getItems().clear();
            out.getItems().clear();
            fileTable.getItems().clear();
            barrierTableView.getItems().clear();
            oneStepBtn.setDisable(true);
            return;
        }

        oneStepBtn.setDisable(false);
        prgIds.getSelectionModel().select(0);
        updateSelectedPrg();

        heapTable.setItems(FXCollections.observableArrayList(
                prgs.get(0).getHeap().getContent().entrySet()
                        .stream().map(e -> Map.entry(e.getKey(), e.getValue()))
                        .toList()
        ));

        out.setItems(FXCollections.observableArrayList(
                prgs.get(0).getOut().getAll()
        ));

        fileTable.setItems(FXCollections.observableArrayList(
                prgs.get(0).getFileTable().getContent().keySet().stream()
                        .map(k -> {
                            if (k instanceof StringValue sv) return sv.getVal();
                            return k.toString();
                        })
                        .toList()
        ));

        barrierTableView.setItems(FXCollections.observableArrayList(
                prgs.get(0).getBarrierTable().getContent().entrySet().stream().toList()
        ));
        barrierTableView.refresh();

    }

    private void updateSelectedPrg() {
        Integer id = prgIds.getSelectionModel().getSelectedItem();
        if (id == null) return;

        ProgramState ps = ctrl.getRepo().getProgramList().stream()
                .filter(p -> p.getId() == id)
                .findFirst().orElse(null);

        if (ps == null) return;

        exeStack.setItems(FXCollections.observableArrayList(
                ps.getExeStack().toListTopToBottom()
                        .stream().map(Object::toString).toList()
        ));

        symTable.setItems(FXCollections.observableArrayList(
                ps.getSymTable().getContent().entrySet()
                        .stream().map(e -> Map.entry(e.getKey(), e.getValue()))
                        .toList()
        ));
    }

    private void runOneStep() {
        try {
            ctrl.oneStepGUI();
            updateAll();
        } catch (Exception ignored) {
        }
    }
}

