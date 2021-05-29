module regfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens regfx to javafx.fxml;
    opens regfx.dialogs to javafx.fxml;
    opens regfx.model to javafx.base;
    exports regfx;
}
