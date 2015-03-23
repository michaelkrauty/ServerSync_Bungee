package me.michaelkrauty.ServerSync_Bungee;

import me.michaelkrauty.ServerSync_Bungee.config.ChannelsFile;
import me.michaelkrauty.ServerSync_Bungee.config.ConfigFile;
import me.michaelkrauty.ServerSync_Bungee.connection.ConnectionHandler;
import me.michaelkrauty.ServerSync_Bungee.user.UserManager;
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
    public ChannelsFile channels;
    public UserManager users;
    public SQLConnector sql;

    public void onEnable() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        connectionHandler = new ConnectionHandler(this);
        config = new ConfigFile(this);
        channels = new ChannelsFile(this);
        users = new UserManager(this);
        sql = new SQLConnector(this);
        //sql.test();
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        users.get(event.getPlayer());
    }
}
