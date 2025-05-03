package com.jhuang.chatservice.strategy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class BroadcastStrategy implements CommandStrategy {
    private static final Logger logger = LogManager.getLogger(BroadcastStrategy.class);
    @Override
    public void handle(String command, String message, Socket socket, ConcurrentHashMap<String, Socket> clients, ServerRunnable currThread) throws IOException {
        clients.forEach((client, socket1) -> {
            try {
                if(!client.equals(currThread.getUserName())){
                    PrintWriter pw = new PrintWriter(socket1.getOutputStream(), true);
                    pw.println("From " + currThread.getUserName() + ": " + message);
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        });
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        pw.println("Message sent.");
    }
}
