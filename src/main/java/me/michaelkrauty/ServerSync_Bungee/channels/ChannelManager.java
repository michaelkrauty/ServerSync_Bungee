package me.michaelkrauty.ServerSync_Bungee.channels;

import me.michaelkrauty.ServerSync_Bungee.Main;

import java.util.ArrayList;

/**
 * Created on 3/23/2015.
 *
 * @author michaelkrauty
 */
public class ChannelManager {

    private final Main main;

    private ArrayList<Channel> channels = new ArrayList<Channel>();

    public ChannelManager(Main main) {
        this.main = main;
        loadChannels();
    }

    public void loadChannels() {
        channels.clear();
        for (String channelName : main.channelsFile.getChannels()) {
            Channel channel = new Channel(main, channelName);
            channels.add(channel);
        }
        for (Channel channel : channels)
            channel.load();
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public Channel getChannel(String name) {
        for (Channel channel : channels)
            if (channel.name.equalsIgnoreCase(name))
                return channel;
        return null;
    }

    public Channel getDefaultChannel() {
        for (Channel channel : channels)
            if (channel.isDefault)
                return channel;
        return getChannels().get(0);
    }
}
