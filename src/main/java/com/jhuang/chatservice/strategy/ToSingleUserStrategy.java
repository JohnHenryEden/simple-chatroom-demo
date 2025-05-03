package com.jhuang.chatservice.strategy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ToSingleUserStrategy implements CommandStrategy {
    private static final Logger logger = LogManager.getLogger(ToSingleUserStrategy.class);

    @Override
    public void handle(String command, String message, Socket socket, ConcurrentHashMap<String, Socket> clients, ServerRunnable currThread) throws IOException {
        String[] userAndMessage = message.split(" ", 2);

        String target = userAndMessage[0];
        String actualMessage = userAndMessage[1];
        clients.forEach((client, socket1) -> {
            try {
                if(client.equals(target)){
                    PrintWriter pw = new PrintWriter(socket1.getOutputStream(), true);
                    pw.println("From " + currThread.getUserName() + ": " + actualMessage);
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
