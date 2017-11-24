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

    public void setType(String type) {
        this.type.set(type);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getGroup() {
        return group.get();
    }

    public void setGroup(String group) {
        this.group.set(group);
    }

    public String getPriority() {
        return priority.get();
    }

    public void setPriority(String priority) {
        this.priority.set(priority);
    }
}
