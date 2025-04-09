package com.example.videocalling;

import com.github.sarxos.webcam.Webcam;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;



public class Client {

    private HelloController helloController;
    Socket socketClient;
    Webcam webCam;

    private Button button1;
    Image image;
    Hyperlink hyperlink;

    private ImageView imageView_holder;

    DataOutputStream dataOutputStream;
    DataOutputStream dataOutputStream1;
    Label label;

    public Client(HelloController helloController, Socket socketClient, Button button1, ImageView imageView_holder, Webcam webCam, Hyperlink hyperlink) throws IOException {
        try {
            this.helloController = helloController;
            this.socketClient = socketClient;
            this.imageView_holder = imageView_holder;
            this.webCam = webCam;

            this.button1 = button1;
            this.hyperlink = hyperlink;
            if (dataOutputStream == null) {
                dataOutputStream = new DataOutputStream(socketClient.getOutputStream());
            }
        } catch (IOException ioException){
            ioException.printStackTrace();
            System.out.println("[CLIENT] : "+ioException.getMessage());
        }

        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    hyperlink.setText("Video is displayed below");
                    button1.setText("Connecting to Server");
                    callVideoCall();
                } catch (IOException ioException){
                    ioException.printStackTrace();
                    System.out.println("[CLIENT] : ERROR ENCOUNTERED");
                } catch (InterruptedException interruptedException) {
                    throw new RuntimeException(interruptedException);
                }
            }
        });
    }



    public void callVideoCall() throws IOException, InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendMessageServer();
                } catch (IOException ioException) {
                    throw new RuntimeException(ioException);
                }
            }
        }, 0, 20);
    }

    public void sendMessageServer() throws IOException {
        byte[] bytes;

        try {
            if (socketClient.isClosed()) {
                System.out.println("[CLIENT] : CLOSED CONNECTION");
                return;
            }

            BufferedImage bufferedImage = webCam.getImage();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
            image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView_holder.setImage(image);

            dataOutputStream1 = new DataOutputStream(socketClient.getOutputStream());
            System.out.println("[CLIENT]: Length is "+bytes.length);

            dataOutputStream1.writeInt(bytes.length);
            dataOutputStream1.flush();
            dataOutputStream1.write(bytes);
            dataOutputStream1.flush();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }
}
