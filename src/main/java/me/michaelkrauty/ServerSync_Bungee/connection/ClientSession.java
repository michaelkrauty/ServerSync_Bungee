package me.michaelkrauty.ServerSync_Bungee.connection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.michaelkrauty.ServerSync_Bungee.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created on 3/21/2015.
 *
 * @author michaelkrauty
 */
public class ClientSession implements Runnable {

    private final Main main;
    private final Socket socket;
    public PrintWriter out;

    public ClientSession(Main main, Socket socket) {
        this.main = main;
        this.socket = socket;
        main.getProxy().getScheduler().runAsync(main, this);
    }

    public void run() {
        main.getLogger().info("Connection get: " + socket.getInetAddress());
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            String input;
            while ((input = in.readLine()) != null) {
                try {
                    main.getLogger().info("DATA: " + input);
                    JsonParser parser = new JsonParser();
                    JsonObject obj = parser.parse(input).getAsJsonObject();
                    String uuid = "";
                    if (obj.get("uuid") != null)
                        uuid = obj.get("uuid").getAsString();
                    String message = obj.get("message").getAsString();
                    JsonObject output = new JsonObject();
                    output.addProperty("action", 0);
                    if (uuid.isEmpty())
                        output.addProperty("message", message);
                    else
                        output.addProperty("message", uuid + ": " + message);
                    out.println(output);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
