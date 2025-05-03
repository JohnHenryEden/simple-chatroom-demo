package com.jhuang.chatservice.strategy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class RepeatMessageStrategy implements CommandStrategy {
    private static final Logger logger = LogManager.getLogger(RepeatMessageStrategy.class);
    @Override
    public void handle(String command, String message, Socket socket, ConcurrentHashMap<String, Socket> clients, ServerRunnable currThread) throws IOException {
        logger.info("Repeating back to client...");
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream, true);
        writer.println(command + " " + message);
    }
}
