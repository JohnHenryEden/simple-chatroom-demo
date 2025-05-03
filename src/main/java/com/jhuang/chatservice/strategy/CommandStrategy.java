package com.jhuang.chatservice.strategy;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public interface CommandStrategy {
    void handle(String command, String message, Socket socket, ConcurrentHashMap<String, Socket> clients, ServerRunnable currThread) throws IOException;
}
