package com.oasys.observer.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles connecting as a client to a server.
 */
public class ClientConnector {

    private final MethodHandler methodHandler;
    private static final Logger LOG = LoggerFactory.getLogger(ClientConnector.class);
    private static ServerHandler serverHandler;

    public ClientConnector(MethodHandler methodHandler) {
        this.methodHandler = methodHandler;
        methodHandler.setClientConnector(this);
    }

    /** initializes a serverHandler, which will connect the Client to the Server and continuously listen
     * to Server messages on a new thread. The start() method of the thread starts the run() method of the class in
     * the thread. In this case the serverHandler.**/
    public void startConnection(String ip, int port) {
       serverHandler = new ServerHandler(ip, port, methodHandler);
       Thread t = new Thread(serverHandler);
       t.start();
    }
    /** a message is sent from client to Server. It's delegated to the serverHandler**/
    public static void sendMessage(String msg) {
          serverHandler.sendMessage(msg);
    }

    /**
     * Close the connection to the server.
     */
    public void close() {
       this.serverHandler.close();

    }

    /**
     * Get the serverHandler.
     * @return
     */
    public ServerHandler getServerHandler(){
        return serverHandler;
    }

    /**
     * Set the serverHandler.
     * @param serverHandler The serverHandler to set to.
     */
    public static void setServerHandler(ServerHandler serverHandler) {
        ClientConnector.serverHandler = serverHandler;
    }
}