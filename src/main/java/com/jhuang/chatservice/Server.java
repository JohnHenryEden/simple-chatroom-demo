package com.jhuang.chatservice;

import com.jhuang.chatservice.strategy.CommandStrategy;
import com.jhuang.chatservice.strategy.ServerRunnable;
import com.jhuang.chatservice.strategy.StrategyFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.jhuang.chatservice.PropertyUtils.getProperty;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private ServerSocket serverSocket;
    private ConcurrentHashMap<String, Socket> clients = new ConcurrentHashMap<>();

    public void startServer() throws IOException {

        try(ServerSocket serverSocket = new ServerSocket(Integer.parseInt(getProperty("port", "server.properties")))){
            this.serverSocket = serverSocket;
            while(true){
                Socket socket = serverSocket.accept();
                InetAddress address = socket.getLocalAddress();
                Thread thread = new Thread(new ServerRunnable() {
                    @Override
                    public void run() {
                        try {
                            logger.info("{} has connected.", address.getHostName());
                            logger.info("Current thread: {}", Thread.currentThread().getName());
                            logger.info("All threads: {}", Thread.activeCount());
                            while(!socket.isClosed()){
                                handleConnection(socket, this);
                            }

                        } catch (IOException e) {
                            logger.error(e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                });
                thread.start();
            }
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }
    public void handleConnection(Socket socket, ServerRunnable currThread) throws IOException {
        if(!socket.isClosed()){
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            if(currThread.getUserName() == null) {
                pw.println("Hi, please input username with setname command.");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String nextline = reader.readLine();
            if(nextline != null && !nextline.isEmpty()){
                String[] parts = nextline.split(" ", 2);
                String command = parts[0];
                String message = "";
                if(parts.length == 2){
                    message = parts[1];
                }
                logger.info(nextline);
                StrategyFactory strategyFactory = new StrategyFactory();
                CommandStrategy strategy = strategyFactory.createCommandStrategy(command);
                strategy.handle(command, message, socket, clients, currThread);
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            logger.info("Starting server...");
            server.startServer();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
