package ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.ThreadManager;

import java.util.Timer;
import java.util.TimerTask;

public class ThreadDisplay extends Application {
    private ThreadManager threadManager;
    private ThreadTable threadTable;

    @Override
    public void init(){
        threadManager = new ThreadManager();
    }

    @Override
    public void start(Stage primaryStage) {
        TextField threadFilterField = new TextField();
        TextField threadGroupFilterField = new TextField();
        ComboBox filterBox = buildFilterBox();
        threadTable = new ThreadTable(threadFilterField, threadGroupFilterField, filterBox);
        TableView tableView = threadTable.buildTable();
        threadTable.refreshTable();
        final HBox searchBar = buildSearchBar(threadFilterField, threadGroupFilterField, filterBox);
        final HBox titleBar = buildTitleBar();
        final VBox tableBox = buildTableBox(tableView);
        HBox threadButtons = buildThreadButtons();

        Group root = new Group();
        int WINDOW_WIDTH = 850;
        int WINDOW_HEIGHT = 710;
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        ((Group) scene.getRoot()).getChildren().addAll(titleBar, searchBar, tableBox, threadButtons);

        autoRefresh();

        primaryStage.setTitle("Thread Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void autoRefresh() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                threadTable.refreshTable();
                System.out.println("refreshing...");
            }
        }, 0, 1500);
    }

    private ComboBox buildFilterBox() {
        ThreadGroup[] allGroups = threadManager.getAllThreadGroups();
        ObservableList<String> groupOptions = FXCollections.observableArrayList();
        for (ThreadGroup tg :
                allGroups) {
            groupOptions.add(tg.getName());
        }
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(groupOptions);
        return comboBox;
    }

    private VBox buildTableBox(TableView tableView) {
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(170, 0, 0, 150));
        vBox.getChildren().addAll(tableView);
        return vBox;
    }

    private HBox buildTitleBar() {
        HBox titleBar = new HBox();
        titleBar.setSpacing(5);
        titleBar.setPadding(new Insets(40, 0, 0, 325));
        Text titleLabel = new Text("Thread Manager");
        titleLabel.setStyle("-fx-font: 24 arial;");
        titleBar.getChildren().addAll(titleLabel);
        return titleBar;
    }

    private HBox buildSearchBar(TextField threadFilterField, TextField threadGroupFilterField, ComboBox filterBox) {
        HBox searchBar = new HBox();
        Text searchLabel = new Text("Search Thread: ");
        Text searchGroupLabel = new Text("Search Group: ");
        searchBar.getChildren().addAll(searchLabel, threadFilterField, searchGroupLabel, threadGroupFilterField, filterBox);
        searchBar.setSpacing(5);
        searchBar.setPadding(new Insets(120, 0, 0, 130));
        return searchBar;
    }

    private HBox buildThreadButtons(){
        HBox hBox = new HBox();
        Button startThreadButton = new Button("New Thread");
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> threadTable.refreshTable());
        startThreadButton.setOnAction(event -> {
            Thread thread = threadManager.createThread("MyThread");
            thread.start();
        });
        hBox.getChildren().addAll(startThreadButton, refreshButton);
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(600, 0, 0, 150));
        return hBox;
    }
}
