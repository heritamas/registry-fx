/**
 * Sample Skeleton for 'regfx-main.fxml' Controller Class
 */

package regfx;

import com.hortonworks.registries.schemaregistry.SchemaBranch;
import com.hortonworks.registries.schemaregistry.SchemaMetadataInfo;
import com.hortonworks.registries.schemaregistry.SchemaVersionInfo;
import com.hortonworks.registries.schemaregistry.utils.ObjectMapperUtils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import regfx.dialogs.Dialogs;
import regfx.model.MainModel;
import regfx.model.SchemaBranches;
import regfx.model.SchemaEnum;
import regfx.model.SchemaModel;
import regfx.model.SchemaVersions;
import regfx.model.Schemas;
import regfx.model.VersionEnum;
import regfx.model.VersionModel;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;

public class MainController {

    private Logger log = Logger.getLogger("regfx");
    private MainModel model = new MainModel();
    private SchemaModel schemaModel = new SchemaModel();
    private VersionModel versionModel = new VersionModel();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private TableView<Map<SchemaEnum, String>> schemaTable;

    @FXML
    private TableColumn<Map, String> idColumn;

    @FXML
    private TableColumn<Map, String> nameColumn;

    @FXML
    private TableColumn<Map, String> descColumn;

    @FXML
    private TableColumn<Map, String> typeColumn;

    @FXML
    private TableColumn<Map, String> compColumn;

    @FXML
    private Label versionsLabel;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private TableView<Map<VersionEnum, String>> versionTable;

    @FXML
    private TableColumn<Map, String> versionIdColumn;

    @FXML
    private TableColumn<Map, String> versionVersionColumn;

    @FXML
    private TableColumn<Map, String> versionShemaTextColumn;


    @FXML // fx:id="registryMenu"
    private Menu registryMenu; // Value injected by FXMLLoader

    @FXML // fx:id="connectMenu"
    private MenuItem connectMenu; // Value injected by FXMLLoader

    @FXML
    private Menu preferencesMenu;

    @FXML // fx:id="quitMenu"
    private MenuItem quitMenu; // Value injected by FXMLLoader

    private Map<String, String> schemaRegistryProps = new HashMap<>();
    private ReadOnlyObjectProperty<String> selectedBranch;
    private ReadOnlyObjectProperty<Map<SchemaEnum, String>> selectedSchema;

    public MainController() throws BackingStoreException { }

    @FXML
    void connectToRegistry(ActionEvent event) throws IOException {
        Map<String, String> props = Dialogs.loadAndShowDialog("dialog-connect", HashMap<String, String>::new);

        if (props.size() != 0 && props.containsKey("hostname") && props.containsKey("port")) {
            model.addPreferences(props.get("hostname") + ":" + props.get("port"), props);
        } else {
            log.warning(String.format("Not enough connection parameters: %s", props));
            return;
        }

        try {
            connectToRegistry(props);
        } catch (Exception e) {
            log.throwing("MainController", "connectToRegistry", e);
        }
    }

    void connectToRegistry(Map<String, String> props) {
        Optional<Schemas> result = HttpUtil.Rest.of(props, "/api/v1/schemaregistry/schemas").execute(Schemas.class);
        result.ifPresent(schemas -> {
            updateCurrentRegistry(props);
            schemaModel.getTable().clear();
            for (SchemaMetadataInfo metadataInfo : schemas.entities) {
                Map<SchemaEnum, String> map = new EnumMap<>(SchemaEnum.class);
                map.put(SchemaEnum.ID, String.valueOf(metadataInfo.getId()));
                map.put(SchemaEnum.NAME, metadataInfo.getSchemaMetadata().getName());
                map.put(SchemaEnum.DESCRIPTION, metadataInfo.getSchemaMetadata().getDescription());
                map.put(SchemaEnum.TYPE, metadataInfo.getSchemaMetadata().getType());
                map.put(SchemaEnum.COMPATIBILITY, metadataInfo.getSchemaMetadata().getCompatibility().name());
                schemaModel.getTable().add(map);
            }
        });

    }

    void getSchemaVersion(Map<SchemaEnum, String> metadata, String branch) {

        Optional<SchemaVersions> result = HttpUtil.Rest.of(
                schemaRegistryProps,
                "/api/v1/schemaregistry/schemas/" + metadata.get(SchemaEnum.NAME) + "/versions",
                "branch=" + branch)
                .execute(SchemaVersions.class);

        result.ifPresent(schemaVersions -> {
            versionModel.getTable().clear();
            for (SchemaVersionInfo schemaVersion : schemaVersions.entities) {
                Map<VersionEnum, String> map = new EnumMap<>(VersionEnum.class);
                map.put(VersionEnum.VERSION_ID, String.valueOf(schemaVersion.getId()));
                map.put(VersionEnum.VERSION_VERSION, String.valueOf(schemaVersion.getVersion()));
                map.put(VersionEnum.SCHEMATEXT, schemaVersion.getSchemaText());
                versionModel.getTable().add(map);
            }
        });
    }

    private void getSchemaBranches(Map<SchemaEnum, String> metadata) {

        Optional<SchemaBranches> result = HttpUtil.Rest.of(
                schemaRegistryProps,
                "/api/v1/schemaregistry/schemas/" + metadata.get(SchemaEnum.NAME) + "/branches")
                .execute(SchemaBranches.class);
        
        result.ifPresent(schemaBranches -> {
            schemaModel.getBranchNames().clear();
            for (SchemaBranch schemaBranch : schemaBranches.entities) {
                schemaModel.getBranchNames().add(schemaBranch.getName());
            }
            choiceBox.getSelectionModel().selectFirst();
        });
    }

    private void updateCurrentRegistry(Map<String, String> props) {
        schemaRegistryProps.clear();
        schemaRegistryProps.putAll(props);
    }

    @FXML
    void quitFromApp(ActionEvent event) {
        Platform.exit();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws BackingStoreException {
        assert registryMenu != null : "fx:id=\"registryMenu\" was not injected: check your FXML file 'regfx-main.fxml'.";
        assert connectMenu != null : "fx:id=\"connectMenu\" was not injected: check your FXML file 'regfx-main.fxml'.";
        assert preferencesMenu != null : "fx:id=\"preferencesMenu\" was not injected: check your FXML file 'regfx-main.fxml'.";
        assert quitMenu != null : "fx:id=\"quitMenu\" was not injected: check your FXML file 'regfx-main.fxml'.";

        model.addPreferencesListener(createPrefsListener());
        model.readPreferences();
        
        schemaTable.setItems(schemaModel.getTable());
        idColumn.setCellValueFactory(new MapValueFactory<String>(SchemaEnum.ID));
        nameColumn.setCellValueFactory(new MapValueFactory<String>(SchemaEnum.NAME));
        descColumn.setCellValueFactory(new MapValueFactory<String>(SchemaEnum.DESCRIPTION));
        typeColumn.setCellValueFactory(new MapValueFactory<String>(SchemaEnum.TYPE));
        compColumn.setCellValueFactory(new MapValueFactory<String>(SchemaEnum.COMPATIBILITY));
        schemaTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            getSchemaBranches(newValue);
            getSchemaVersion(newValue, selectedBranch.getValue());
        });
        selectedSchema = schemaTable.getSelectionModel().selectedItemProperty();
        
        versionTable.setItems(versionModel.getTable());
        versionIdColumn.setCellValueFactory(new MapValueFactory<String>(VersionEnum.VERSION_ID));
        versionVersionColumn.setCellValueFactory(new MapValueFactory<String>(VersionEnum.VERSION_VERSION));
        versionShemaTextColumn.setCellValueFactory(new MapValueFactory<String>(VersionEnum.SCHEMATEXT));
        versionsLabel.textProperty().bind(Bindings.size(versionModel.getTable()).asString());
        choiceBox.setItems(schemaModel.getBranchNames());
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            getSchemaVersion(selectedSchema.get(), newValue);
                });
        selectedBranch = choiceBox.getSelectionModel().selectedItemProperty();

    }

    private MapChangeListener<? super String,? super Map<String, String>> createPrefsListener() {
        return chg -> {
            if (chg.wasAdded()) {
                addMenuItem(chg.getKey(), chg.getValueAdded());
            } else if (chg.wasRemoved()) {
                preferencesMenu.getItems().removeIf(mi -> mi.getText().contains(chg.getKey()));
            }
        };
    }

    private void addMenuItem(String key, Map<String, String> props) {
        MenuItem connectItem = new MenuItem("Connect to " + key);
        connectItem.setOnAction(event -> connectToRegistry(props));

        MenuItem deleteItem = new MenuItem("Delete " + key);
        deleteItem.setOnAction(event -> model.removePreferences(key));

        preferencesMenu.getItems().addAll(connectItem, deleteItem);
    }
}
