package com.jhuang.chatservice.strategy;

import java.net.Socket;

public abstract class ServerRunnable implements Runnable {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    abstract public void run();
}
