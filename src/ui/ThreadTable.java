package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.ThreadManager;
import model.ThreadModel;

import java.util.Optional;

class ThreadTable {
    private TableView tableView;
    private ThreadManager threadManager;
    private TextField threadFilterField;
    private ComboBox filterCombo;
    private ObservableList<ThreadModel> dataModel;
    private boolean isSearching;

    /**
     * @param threadFilterField      the textfield the user will use to search threads
     * @param filterCombo            the dropdown list of threadgroups the user can use to filter by threadgroup
     * @requires threadFilterField != null && threadGroupFilterField != null && filterCombo != null
     * @modifies this.threadFilterField, this.threadGroupFilterField, this.filterCombo, this.tableView, this.threadManager
     */
    ThreadTable(TextField threadFilterField, ComboBox filterCombo) {
        tableView = new TableView();
        threadManager = new ThreadManager();
        this.threadFilterField = threadFilterField;
        this.filterCombo = filterCombo;
        isSearching = false;
    }

    /**
     * @return the constructed tableview with appropriate columns
     * @modifies this.tableView, this.dataModel
     */
    @SuppressWarnings("unchecked")
    TableView buildTable() {
        tableView.setEditable(true);
        tableView.setMinWidth(541);
        TableColumn<ThreadModel, String> threadIDCol = new TableColumn<>("ID");
        TableColumn<ThreadModel, String> threadNameCol = new TableColumn<>("Name");
        TableColumn<ThreadModel, String> threadGroupCol = new TableColumn<>("Group");
        TableColumn<ThreadModel, String> threadTypeCol = new TableColumn<>("Type");
        TableColumn<ThreadModel, String> threadPriorityCol = new TableColumn<>("Priority");
        tableView.getColumns().addAll(threadIDCol, threadNameCol, threadGroupCol, threadTypeCol, threadPriorityCol);
        threadIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        threadNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        threadGroupCol.setCellValueFactory(new PropertyValueFactory<>("group"));
        threadTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        threadPriorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        dataModel = FXCollections.observableArrayList();

        tableView.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                } else {
                    // clicking on text part
                    row = (TableRow) node.getParent();
                }
                killThreadDialog((ThreadModel) row.getItem());
            }
        });

        return tableView;
    }

    /**
     * @param selectedRow the row of the tableView the user double-clicked
     * @throws NullPointerException seems to cause a nullp exception in fx classes, unsure how to fix
     * @requires selectedRow != null
     * @effects calls killThread if user selects kill thread.
     */
    private void killThreadDialog(ThreadModel selectedRow) throws NullPointerException {
        assert selectedRow != null
                : "row is null";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thread Manager");
        alert.setHeaderText("Thread: " + selectedRow.getName());
        alert.setContentText("Would you like to kill this thread? ");

        ButtonType killButton = new ButtonType("Kill");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(killButton, cancelButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()){
            if (result.get() == killButton) {
                threadManager.killThread(Long.valueOf(selectedRow.getId()));
            } else {
                alert.close();
            }
        }
    }


    /**
     * @modifies this.tableView, this.threadFilterField, this.threadGroupFilterField, this.dataModel
     * updates the data in the tableview with the latest collection of active threads
     */
    @SuppressWarnings("unchecked")
    void refreshTable() {
        if (threadFilterField.getText().isEmpty()) isSearching = false;
        if (filterCombo.getValue().equals("All") && !isSearching) {
            dataModel.clear();
            for (Thread thread : threadManager.getAllThreads()) {
                String daemon;
                daemon = thread.isDaemon() ? "daemon" : "non-daemon";
                dataModel.add(
                        new ThreadModel(
                                Long.toString(thread.getId()),
                                thread.getName(),
                                thread.getThreadGroup().getName(),
                                daemon,
                                Integer.toString(thread.getPriority())));
            }
            tableView.setItems(dataModel);
            threadFilterField.setOnKeyPressed(event -> searchThreadName());
            filterCombo.valueProperty().addListener(event -> filterByThreadGroup());
        }
        threadFilterField.setOnKeyPressed(event -> searchThreadName());
        filterCombo.valueProperty().addListener(event -> filterByThreadGroup());
    }

    /**
     * @modifies this.threadFilterField
     * Creates a filteredList based on what the user enters into the searchbox. Updates the tableview with the filteredlist
     */
    @SuppressWarnings("unchecked")
    private void searchThreadName() {
        isSearching = true;
        FilteredList<ThreadModel> filteredData = new FilteredList<>(dataModel, p -> true);
        threadFilterField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(thread -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            return thread.getName().toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<ThreadModel> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }


    @SuppressWarnings("unchecked")
    private void filterByThreadGroup() {
        dataModel.clear();
        assert filterCombo.getValue() != null;
        String val = String.valueOf(filterCombo.getValue());
        ThreadGroup[] allGroups = threadManager.getAllThreadGroups();
        ThreadGroup selected = null;
        for (ThreadGroup tg :
                allGroups) {
            if (tg.getName().equals(val)) {
                selected = tg;
            }
        }
        for (Thread thread : threadManager.getAllThreads()) {
            if (thread.getThreadGroup().equals(selected)){
                String daemon;
                daemon = thread.isDaemon() ? "daemon" : "non-daemon";
                dataModel.add(
                        new ThreadModel(
                                Long.toString(thread.getId()),
                                thread.getName(),
                                thread.getThreadGroup().getName(),
                                daemon,
                                Integer.toString(thread.getPriority())));
            }
        }
        tableView.setItems(dataModel);
    }

    boolean isSearching() {
        return isSearching;
    }

}
