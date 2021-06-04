package regfx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Map;

public class SchemaModel {
    ObservableList<Map<SchemaEnum, String>> table = FXCollections.observableArrayList();

    public ObservableList<Map<SchemaEnum, String>> getTable() {
        return table;
    }

    public void setTable(ObservableList<Map<SchemaEnum, String>> table) {
        this.table = table;
    }
}
