module com.example.videocalling {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires org.jetbrains.annotations;
    requires webcam.capture;

    opens com.example.videocalling to javafx.graphics, javafx.fxml;
}
