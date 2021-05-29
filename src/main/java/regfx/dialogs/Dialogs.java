package regfx.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.util.function.Supplier;

public class Dialogs {

    public static <T> T loadAndShowDialog(String which, Supplier<T> defaultResult) throws IOException {
        FXMLLoader loader = new FXMLLoader(Dialogs.class.getResource(which + ".fxml"));
        DialogPane dp = loader.<DialogPane>load ();
        DialogController<T> controller = loader.<DialogController<T>>getController();

        Dialog dialog = new Dialog<T>();
        dialog.setDialogPane(dp);
        dialog.setResultConverter( bType -> {
            if (bType == ButtonType.APPLY) {
                return controller.getResult();
            } else {
                return defaultResult.get();
            }
        });

        T result  = (T) dialog.showAndWait()
                .filter(response -> response == ButtonType.APPLY)
                .orElseGet(defaultResult);

        return result;
    }
}
