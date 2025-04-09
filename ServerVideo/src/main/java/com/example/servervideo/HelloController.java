package com.example.servervideo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    ServerSocket serverSocket;

    @FXML
    Button button;

    @FXML
    private ImageView imageView_Holder;

    private boolean serverRunning = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (serverRunning) {
                    System.out.println("[SERVER-VIDEO] Server is already running.");
                    return;
                }
                serverRunning = true;


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            serverSocket = new ServerSocket(5000);
                            Server server = new Server(serverSocket, imageView_Holder);
                            Platform.runLater(() ->{
                                button.setText("[SERVER-VIDEO: HelloController] WAITING.......");
                                serverRunning = true;
                            });

                            server.receiveMessageFromClient();
                        } catch (IOException ioException) {
                            throw new RuntimeException(ioException);
                        }
                    }
                }).start();
            }
        });
    }

}