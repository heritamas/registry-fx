package regfx.dialogs;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import regfx.model.ConnectModel;
import regfx.model.Pair;

public class ConnectController implements DialogController<Map<String, String>> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    Logger log = Logger.getLogger("regfx");

    ConnectModel model = new ConnectModel();

    @FXML
    private DialogPane connectDialog;

    @FXML
    private TableView<Pair<String, String>> parameterTable;

    @FXML
    private TableColumn<Pair<String, String>, String> parameterColumn;

    @FXML
    private TableColumn<Pair<String, String>, String> valueColumn;

    @FXML
    void initialize() {
        assert connectDialog != null : "fx:id=\"connectDialog\" was not injected: check your FXML file 'dialog-connect.fxml'.";
        assert parameterTable != null : "fx:id=\"content\" was not injected: check your FXML file 'dialog-connect.fxml'.";
        assert parameterColumn != null : "fx:id=\"propertyColumn\" was not injected: check your FXML file 'dialog-connect.fxml'.";
        assert valueColumn != null : "fx:id=\"valueColumn\" was not injected: check your FXML file 'dialog-connect.fxml'.";

        connectDialog.lookupButton(ButtonType.NEXT).addEventFilter(ActionEvent.ACTION, event -> {
            model.getUnusedParameter().ifPresent(key -> {
                model.getTablerows().add(new Pair<String, String>(key, "***"));
            });
            event.consume();
        });

        parameterColumn.setCellFactory(ComboBoxTableCell.forTableColumn(model.getParameters()));
        parameterColumn.setCellValueFactory( new PropertyValueFactory<Pair<String, String>, String>("key"));
        parameterColumn.setOnEditCommit(event -> {
            Pair<String, String> pair = event.getTableView().getItems().get(event.getTablePosition().getRow());
            pair.setKey(event.getNewValue());
        });

        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setCellValueFactory( cdf -> {
            return new SimpleStringProperty(cdf.getValue().getValue());
        });
        valueColumn.setOnEditCommit(event -> {
            Pair<String, String> pair = event.getTableView().getItems().get(event.getTablePosition().getRow());
            pair.setValue(event.getNewValue());
        });

        parameterTable.setItems(model.getTablerows());
    }

    @Override
    public Map<String, String> getResult() {
        model.getTablerows()
                .stream()
                .map(Pair::toString)
                .forEach(log::info);

        return model.getTablerows().stream().collect(
                Collectors.toUnmodifiableMap(Pair::getKey, Pair::getValue)
        );

    }
}
