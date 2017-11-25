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

    /**
     * @modifies this.threadManager
     */
    @Override
    public void init() {
        threadManager = new ThreadManager();
    }

    /**
     * Builds the initial UI, the table and gets the data for the table.
     * @param primaryStage the stage to hold the UI elements
     * @requires threadManager != null
     * @modifies this.threadFilterField, this.threadTable
     */
    @Override
    public void start(Stage primaryStage) {
        TextField threadFilterField = new TextField();
        ComboBox<String> groupCombo = buildFilterBox();
        threadTable = new ThreadTable(threadFilterField, groupCombo);
        TableView tableView = threadTable.buildTable();
        threadTable.refreshTable();
        final HBox searchBar = buildSearchBar(threadFilterField, groupCombo);
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

    /**
     * @requires threadTable != null
     * @modifies this.threadTable
     * @effects threadTable data is updated to latest collection of active threads
     * Uses a timer task to periodically refresh the table data every 1.5 seconds
     */
    private void autoRefresh() {
        if (!threadTable.isSearching()) {
            Timer timer = new Timer();
            int REFRESH_DELAY = 1000; // ms
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    threadTable.refreshTable();
                }
            }, 0, REFRESH_DELAY);
        }
    }

    /**
     * @return the populated combobox with all the threadgroups
     * @requires threadManager != null
     * @effects builds a combobox containing all active threadgroups
     */
    private ComboBox<String> buildFilterBox() {
        ThreadGroup[] allGroups = threadManager.getAllThreadGroups();
        ObservableList<String> groupOptions = FXCollections.observableArrayList();
        groupOptions.add("All");
        for (ThreadGroup tg :
                allGroups) {
            groupOptions.add(tg.getName());
        }
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(groupOptions);
        comboBox.getSelectionModel().select(groupOptions.get(0));
        return comboBox;
    }


    /**
     * @param tableView the tableView which will be held in the vBox container
     * @return a VBox containing the tableview
     * @requires tableView != null
     * @effects builds container to hold the tableview
     */
    private VBox buildTableBox(TableView tableView) {
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(170, 0, 0, 150));
        vBox.getChildren().addAll(tableView);
        return vBox;
    }

    /**
     * @return HBox with a title
     * @effects builds an HBox containing the title
     */
    private HBox buildTitleBar() {
        HBox titleBar = new HBox();
        titleBar.setSpacing(5);
        titleBar.setPadding(new Insets(40, 0, 0, 325));
        Text titleLabel = new Text("Thread Manager");
        titleLabel.setStyle("-fx-font: 24 arial;");
        titleBar.getChildren().addAll(titleLabel);
        return titleBar;
    }

    /**
     * @param threadFilterField      textfield for searching threads
     * @param filterBox              combobox for filtering by threadgroup
     * @return the constructed HBox
     * @requires threadFilterField != null && != null && filterBox != null
     * @effects builds an HBox containing a textfield for searching threads
     */
    private HBox buildSearchBar(TextField threadFilterField, ComboBox<String> filterBox) {
        HBox searchBar = new HBox();
        Text searchLabel = new Text("Search Thread: ");
        searchBar.getChildren().addAll(searchLabel, threadFilterField, filterBox);
        searchBar.setSpacing(5);
        searchBar.setPadding(new Insets(120, 0, 0, 40));
        return searchBar;
    }

    /**
     * @return an HBox containing implemented buttons for refreshing and starting threads
     * @effects Builds an HBox to contain the buttons for refreshing and adding new threads
     */
    private HBox buildThreadButtons() {
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
