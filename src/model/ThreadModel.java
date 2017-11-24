package model;

import javafx.beans.property.SimpleStringProperty;

public class ThreadModel {
    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty group;
    private final SimpleStringProperty type;
    private final SimpleStringProperty priority;


    public ThreadModel(String id, String name, String group, String type, String priority) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.group = new SimpleStringProperty(group);
        this.type = new SimpleStringProperty(type);
        this.priority = new SimpleStringProperty(priority);
    }

    public String getType() {
        return type.get();
    }

    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getGroup() {
        return group.get();
    }

    public String getPriority() {
        return priority.get();
    }

}
