/**
 * Sample Skeleton for 'regfx-main.fxml' Controller Class
 */

package regfx;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import regfx.dialogs.ConnectController;
import regfx.dialogs.Dialogs;

public class MainController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="registryMenu"
    private Menu registryMenu; // Value injected by FXMLLoader

    @FXML // fx:id="connectMenu"
    private MenuItem connectMenu; // Value injected by FXMLLoader

    @FXML // fx:id="quitMenu"
    private MenuItem quitMenu; // Value injected by FXMLLoader

    @FXML
    void connectToRegistry(ActionEvent event) throws IOException {
        Map<String, String> props = Dialogs.loadAndShowDialog("dialog-connect", HashMap<String, String>::new);

        //TODO
    }

    @FXML
    void quitFromApp(ActionEvent event) {
        Platform.exit();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert registryMenu != null : "fx:id=\"registryMenu\" was not injected: check your FXML file 'regfx-main.fxml'.";
        assert connectMenu != null : "fx:id=\"connectMenu\" was not injected: check your FXML file 'regfx-main.fxml'.";
        assert quitMenu != null : "fx:id=\"quitMenu\" was not injected: check your FXML file 'regfx-main.fxml'.";

    }
}
