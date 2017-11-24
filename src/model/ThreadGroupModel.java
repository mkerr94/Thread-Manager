package model;

import javafx.beans.property.SimpleStringProperty;

public class ThreadGroupModel {
    private final SimpleStringProperty parent;
    private final SimpleStringProperty name;

    public ThreadGroupModel(String parent, String name) {
        this.parent = new SimpleStringProperty(parent);
        this.name = new SimpleStringProperty(name);
    }

    public String getParent() {
        return parent.get();
    }
    public String getName() {
        return name.get();
    }

}
