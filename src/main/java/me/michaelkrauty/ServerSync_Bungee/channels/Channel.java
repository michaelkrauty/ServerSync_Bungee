package me.michaelkrauty.ServerSync_Bungee.channels;

import me.michaelkrauty.ServerSync_Bungee.Main;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;

/**
 * Created on 3/23/2015.
 *
 * @author michaelkrauty
 */
public class Channel {

    public final String name;
    public final Main main;
    public boolean isDefault;
    public String format;
    public ArrayList<Channel> send = new ArrayList<Channel>();
    public ArrayList<Channel> receive = new ArrayList<Channel>();

    public Channel(Main main, String name) {
        this.main = main;
        this.name = name;
    }

    public void load() {
        Configuration info = main.channelsFile.getChannel(name);
        if (info.getString("default") != null)
            isDefault = info.getBoolean("default");

        if (info.getString("format") != null)
            format = info.getString("format");

        if (info.getStringList("send").isEmpty())
            send.add(this);
        else
            for (String n : info.getStringList("send"))
                send.add(main.channels.getChannel(n));

        if (info.getStringList("receive").isEmpty())
            receive.add(this);
        else
            for (String n : info.getStringList("receive"))
                receive.add(main.channels.getChannel(n));
    }

    public String toString() {
        return "[name:" + name + "]";
    }
}
