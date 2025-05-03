package com.jhuang.chatservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static com.jhuang.chatservice.PropertyUtils.getProperty;

public class Client {
    private static final Logger logger = LogManager.getLogger(Client.class);
    private Socket socket;
    public static void main(String[] args) {
        logger.info("Starting client...");
        Scanner scanner = new Scanner(System.in);
        try(Socket socket = new Socket("localhost", Integer.parseInt(getProperty("port", "server.properties")))){
            String command = "";
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            PrintWriter printWriter = new PrintWriter(outputStream, true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Thread responseThread = new Thread(() -> {
                while (!socket.isClosed()) {
                    try {
                        String response = bufferedReader.readLine();
                        logger.info("Received response: {}", response);
                        if(socket.isClosed()) break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            responseThread.start();
            while (!socket.isClosed()) {
                command = scanner.nextLine();
                printWriter.println(command);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
