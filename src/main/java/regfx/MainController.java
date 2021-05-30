/**
 * Sample Skeleton for 'regfx-main.fxml' Controller Class
 */

package regfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import regfx.dialogs.Dialogs;
import regfx.model.MainModel;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;

public class MainController {

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

    public MainController() throws BackingStoreException {
    }

    @FXML
    void connectToRegistry(ActionEvent event) throws IOException {
        Map<String, String> props = Dialogs.loadAndShowDialog("dialog-connect", HashMap<String, String>::new);

        model.addPreferences(props.get("hostname") + ":" + props.get("port"), props);
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

        model.setPreferenceItems(preferencesMenu.getItems());
        model.readPreferences();

    }
}
