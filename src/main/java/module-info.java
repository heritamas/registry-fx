module regfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.prefs;
    requires java.net.http;
    requires schema.registry.common;
    requires com.fasterxml.jackson.databind;

    opens regfx to javafx.fxml, com.fasterxml.jackson.databind;
    opens regfx.dialogs to javafx.fxml;
    opens regfx.model to javafx.base;
    exports regfx;
}
