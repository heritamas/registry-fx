package regfx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Map;

public class SchemaModel {
    ObservableList<Map<SchemaEnum, String>> table = FXCollections.observableArrayList();
    ObservableList<String> branches = FXCollections.observableArrayList();


    public ObservableList<Map<SchemaEnum, String>> getTable() {
        return table;
    }

    public ObservableList<String> getBranches() {
        return branches;
    }
}
