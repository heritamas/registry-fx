/**
 * Sample Skeleton for 'regfx-main.fxml' Controller Class
 */

package regfx;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hortonworks.registries.schemaregistry.SchemaMetadataInfo;
import com.hortonworks.registries.schemaregistry.utils.ObjectMapperUtils;
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import regfx.dialogs.Dialogs;
import regfx.model.MainModel;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;

class Schemas {
    @JsonProperty
    List<SchemaMetadataInfo> entities;
}

public class MainController {

    private Logger log = Logger.getLogger("regfx");
    private MainModel model = new MainModel();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="registryMenu"
    private Menu registryMenu; // Value injected by FXMLLoader

    @FXML // fx:id="connectMenu"
    private MenuItem connectMenu; // Value injected by FXMLLoader

    @FXML
    private Menu preferencesMenu;

    @FXML // fx:id="quitMenu"
    private MenuItem quitMenu; // Value injected by FXMLLoader

    public MainController() throws BackingStoreException { }

    @FXML
    void connectToRegistry(ActionEvent event) throws IOException {
        Map<String, String> props = Dialogs.loadAndShowDialog("dialog-connect", HashMap<String, String>::new);

        if (props.size() != 0) {
            model.addPreferences(props.get("hostname") + ":" + props.get("port"), props);
        }

        try {
            connectToRegistry(props);
        } catch (Exception e) {
            log.throwing("MainController", "connectToRegistry", e);
        }
    }

    void connectToRegistry(Map<String, String> props)  {
        String hostname = props.get("hostname");
        String port = props.get("port");
        String path = props.get("path");

        log.info("Connecting ot registry ...");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http", null, hostname, Integer.parseInt(port), path, null, null))
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Schemas result = ObjectMapperUtils.deserialize(response.<String>body(), Schemas.class);
                result.entities.size();
            }

        } catch (Exception e) {
            log.throwing("MainController", "connectToRegistry", e);
        }

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
