package regfx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConnectModel {


    private final ObservableList<String> parameters =
            FXCollections.unmodifiableObservableList(
                    FXCollections.observableArrayList(
                            "hostname",
                            "port",
                            "path")
            );

    private final ObservableList<Pair<String, String>> tablerows = FXCollections.observableArrayList();

    public ObservableList<String> getParameters() {
        return parameters;
    }

    public Optional<String> getUnusedParameter() {
        ArrayList<String> unusedParams = new ArrayList<String>(parameters);
        unusedParams.removeAll(tablerows.stream()
                .map(Pair::getKey)
                .collect(Collectors.toUnmodifiableList()));

        return unusedParams.stream().findFirst();
    }

    public ObservableList<Pair<String, String>> getTablerows() {
        return tablerows;
    }
}
