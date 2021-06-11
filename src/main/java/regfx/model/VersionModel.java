package regfx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Map;

public class VersionModel {
    ObservableList<Map<VersionEnum, String>> table = FXCollections.observableArrayList();

    public ObservableList<Map<VersionEnum, String>> getTable() {
        return table;
    }
}
