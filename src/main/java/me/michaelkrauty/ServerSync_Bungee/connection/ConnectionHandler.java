package me.michaelkrauty.ServerSync_Bungee.connection;

import me.michaelkrauty.ServerSync_Bungee.Main;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created on 3/21/2015.
 *
 * @author michaelkrauty
 */
public class ConnectionHandler implements Runnable {

    private final Main main;
    public ArrayList<MCServerSession> mcServerConnections = new ArrayList<MCServerSession>();

    public ConnectionHandler(Main main) {
        this.main = main;
        main.getProxy().getScheduler().runAsync(main, this);
    }

    public void run() {
        try {
            ServerSocket socket = new ServerSocket(53844);
            main.getLogger().info("Listening on " + socket.getLocalSocketAddress());
            Socket client;
            while ((client = socket.accept()) != null)
                mcServerConnections.add(new MCServerSession(main, client));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
