package com.jhuang.chatservice.strategy;

import com.jhuang.chatservice.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class CloseStrategy implements CommandStrategy {
    private static final Logger logger = LogManager.getLogger(CloseStrategy.class);
    @Override
    public void handle(String command, String message, Socket socket, ConcurrentHashMap<String, Socket> clients, ServerRunnable currThread) throws IOException {
        logger.info("Closing socket...");
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        if(currThread.getUserName() != null) {
            writer.println("Bye, " + currThread.getUserName() + ".");
            clients.remove(currThread.getUserName());
        }else {
            writer.println("Bye");
        }
        writer.println("Closing socket");
        socket.close();
    }
}
