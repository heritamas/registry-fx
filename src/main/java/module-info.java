module regfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens regfx to javafx.fxml;
    exports regfx;
}
