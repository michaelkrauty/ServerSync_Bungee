package me.michaelkrauty.ServerSync_Bungee.connection;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import me.michaelkrauty.ServerSync_Bungee.Main;
import me.michaelkrauty.ServerSync_Bungee.channels.Channel;
import me.michaelkrauty.ServerSync_Bungee.user.User;

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
public class MCServerSession implements Runnable {

    private final Main main;
    private final Socket socket;
    public PrintWriter out;

    public MCServerSession(Main main, Socket socket) {
        this.main = main;
        this.socket = socket;
        main.getProxy().getScheduler().runAsync(main, this);
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            String input;
            while ((input = in.readLine()) != null) {
                try {
                    JsonParser parser = new JsonParser();
                    JsonObject obj = parser.parse(input).getAsJsonObject();
                    String action;
                    if (obj.get("action") == null)
                        throw new JsonSyntaxException("action is null");
                    action = obj.get("action").getAsString();

                    if (action.equalsIgnoreCase("chat")) {
                        User user = main.users.get(main.getProxy().getPlayer(obj.get("player").getAsString()));
                        Channel channel = user.channel;
                        main.getLogger().info("[CHAT] " + user.player.getName() + ": " + obj.get("message").getAsString());

                        for (MCServerSession session : main.connectionHandler.mcServerConnections) {
                            JsonObject out = new JsonObject();
                            out.addProperty("action", "chat");
                            JsonArray to = new JsonArray();
                            for (User u : main.users.getUsers()) {
                                if (u.channel.send.contains(channel))
                                    to.add(new JsonParser().parse(u.player.getName()));
                            }
                            out.add("to", to);
                            String message = channel.format
                                    .replace("{player}", user.getName())
                                    .replace("{message}", obj.get("message").getAsString());


                            out.addProperty("message", message);
                            session.out.println(out);
                        }
                    } else if (action.equalsIgnoreCase("ban")) {

                    } else if (action.equalsIgnoreCase("mute")) {
                        for (MCServerSession session : main.connectionHandler.mcServerConnections) {
                            session.out.println(obj);
                        }
                    }










                    /*
                    String playerName = "";
                    if (obj.get("player") != null)
                        playerName = obj.get("player").getAsString();
                    String message = obj.get("message").getAsString();

                    ProxiedPlayer player = main.getProxy().getPlayer(playerName);

                    User user = main.users.get(player);

                    Channel channel = user.channel;

                    String format = channel.format;

                    for (Channel out : channel.send) {
                        for (User usr : main.users.getUsers()) {
                            if (usr.channel == out) {
                                TextComponent msg = new TextComponent();
                                TextComponent name = new TextComponent();
                                if (user.nickname != null) {
                                    name.setText(user.nickname);
                                    name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(user.player.getName()).create()));
                                } else
                                    name.setText(user.player.getName());
                                msg.setText("");
                                msg.addExtra(name);
                                msg.addExtra(": ");
                                msg.addExtra(message);

                                usr.player.sendMessage(ChatMessageType.CHAT, msg);
                            }
                        }
                    }
                    */

                    /*
                    JsonObject output = new JsonObject();
                    output.addProperty("action", 0);
                    if (playerName.isEmpty())
                        output.addProperty("message", message);
                    else
                        output.addProperty("message", playerName + ": " + message);
                    out.println(output);
                    */
                } catch (JsonSyntaxException e) {
                    main.getLogger().warning("Malformed JSON from " + socket.getLocalAddress());
                    main.getLogger().warning("Message: " + e.getMessage());
                    main.getLogger().warning("Localized Message: " + e.getLocalizedMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ignored) {
        }

    }
}
