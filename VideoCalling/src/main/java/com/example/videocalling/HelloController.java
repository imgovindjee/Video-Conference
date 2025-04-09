package com.example.videocalling;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Button button;
    @FXML
    private ImageView imageView_holder;
    @FXML
    private Hyperlink hyperlink;
    @FXML
    private Label statusLabel;  // Make sure this matches fx:id in FXML


    Socket socket;
    Webcam webCam;
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webCam = Webcam.getDefault();
        webCam.setViewSize(new Dimension(640, 480));
        webCam.open();

        try {
            socket = new Socket("127.0.0.1", 5000);
            System.out.println("[CLIENT] : CONNECTED TO SERVER......");
//            Client client = new Client(this, socket, button, imageView_holder, webCam, hyperlink);
//            client.callVideoCall();


            Platform.runLater(() -> {
                try {
                    Client client = new Client(this, socket, button, imageView_holder, webCam, hyperlink);
                    client.callVideoCall();
                } catch (IOException | InterruptedException e) {
//                    OPTIONAL
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Connection Error");
                        alert.setHeaderText("Could not connect to server");
                        alert.setContentText("Please make sure the server is running on localhost:5000");
                        alert.show();
                    });
                    return;
                }
            });
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }
}