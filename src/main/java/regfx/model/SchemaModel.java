package regfx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Map;

public class SchemaModel {
    ObservableList<Map<SchemaEnum, String>> table = FXCollections.observableArrayList();
    ObservableList<String> branchNames = FXCollections.observableArrayList();

    public ObservableList<String> getBranchNames() {
        return branchNames;
    }

    public ObservableList<Map<SchemaEnum, String>> getTable() {
        return table;
    }
    
}
