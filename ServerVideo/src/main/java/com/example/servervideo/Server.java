package com.example.servervideo;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    @FXML
    private ImageView imageView_Holder;
    private ServerSocket serverSocket;
    Socket socket;

    DataInputStream dataInputStream;

    public Server (@NotNull ServerSocket serverSocket, ImageView imageView_Holder) {
        try {
            this.serverSocket = serverSocket;
            this.imageView_Holder = imageView_Holder;
            this.socket = serverSocket.accept();
            System.out.println("[SERVER-VIDEO: SERVER] WAIT.....");
            System.out.println("[SERVER-VIDEO: SERVER] CONNECTED.....");
        } catch (IOException ioException) {
            System.err.println("[SERVER-VIDEO: SERVER] ERROR..");
            throw new RuntimeException(ioException);
        }
    }


    public void receiveMessageFromClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    System.out.println("[SERVER-VIDEO: SERVER] SEND PICTURE");
                    System.out.println("[SERVER-VIDEO: SERVER] WAITING......");
                    System.out.println("[SERVER-VIDEO: SERVER] WAIT OVER.....");
                    System.out.println("[SERVER-VIDEO: SERVER] --> 1");

                    int length;
                    try {
                        dataInputStream = new DataInputStream(socket.getInputStream());

                        int a;
                        length = dataInputStream.readInt();
                        System.out.println("[SERVER-VIDEO: SERVER] length is "+length);

                        byte[] bytes = new byte[length];
                        dataInputStream.readFully(bytes, 0, length);

                        Image image;
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                        BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
                        image = SwingFXUtils.toFXImage(bufferedImage, null);

                        imageView_Holder.setImage(image);
                        System.out.println("[SERVER-VIDEO: SERVER] --> "+image.getWidth());
                    } catch (IOException ioException) {
                        throw new RuntimeException(ioException);
                    }
                }
            }
        }).start();
    }
}
