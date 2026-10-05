package com.oasys.server.presentation;
import com.oasys.server.logic.ServerConnector;

public class Launcher {
    public static void main(String[] args) {
        new Thread(() -> {
            HelloApplication.main(args);
        }).start();

        new Thread(() -> {
            ServerConnector.main(args);
        }).start();
    }
}
