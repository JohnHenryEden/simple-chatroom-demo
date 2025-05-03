package com.jhuang.chatservice.strategy;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class SetNameStrategy implements CommandStrategy {
    @Override
    public void handle(String command, String message, Socket socket, ConcurrentHashMap<String, Socket> clients, ServerRunnable currThread) throws IOException {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        if (message != null && !message.isEmpty()) {
            if(clients.containsKey(message)) {
                printWriter.println("Name " + message + " already exists, please choose another name.");
            }else{
                clients.put(message, socket);
                currThread.setUserName(message);
                Thread.currentThread().setName(message);
                printWriter.println("Welcome, " + message + ".");
            }
        }else{
            printWriter.println("Name is empty, please enter name again.");
        }
    }
}
