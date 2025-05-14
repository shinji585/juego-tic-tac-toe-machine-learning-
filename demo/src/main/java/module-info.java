module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // Solo se abre el paquete View a JavaFX para reflexión (FXML, Application)
    opens com.example.View to javafx.fxml, javafx.graphics;

    // Solo se exporta View si otras partes externas lo usan (por ejemplo App.java está fuera de View)
    exports com.example.View;
}
