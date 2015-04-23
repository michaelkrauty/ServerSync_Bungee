package me.michaelkrauty.ServerSync_Bungee;

import me.michaelkrauty.ServerSync_Bungee.channels.ChannelManager;
import me.michaelkrauty.ServerSync_Bungee.config.ChannelsFile;
import me.michaelkrauty.ServerSync_Bungee.config.ConfigFile;
import me.michaelkrauty.ServerSync_Bungee.connection.ConnectionHandler;
import me.michaelkrauty.ServerSync_Bungee.user.UserManager;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

/**
 * Created on 3/21/2015.
 *
 * @author michaelkrauty
 */
public class Main extends Plugin implements Listener {

    public ConnectionHandler connectionHandler;
    public ConfigFile config;
    public ChannelsFile channelsFile;
    public UserManager users;
    public ChannelManager channels;
    public SQLConnector sql;

    public void onEnable() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        getProxy().getPluginManager().registerListener(this, this);
        connectionHandler = new ConnectionHandler(this);
        config = new ConfigFile(this);
        channelsFile = new ChannelsFile(this);
        users = new UserManager(this);
        channels = new ChannelManager(this);
        sql = new SQLConnector(this);
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        users.get(event.getPlayer());
        getLogger().info("User Info: " + users.get(event.getPlayer()).toString());
        // TODO: disconnect
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent event) {
        users.remove(event.getPlayer());
    }
}
