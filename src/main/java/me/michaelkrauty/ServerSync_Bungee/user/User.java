package me.michaelkrauty.ServerSync_Bungee.user;

import me.michaelkrauty.ServerSync_Bungee.Main;
import me.michaelkrauty.ServerSync_Bungee.channels.Channel;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created on 3/21/2015.
 *
 * @author michaelkrauty
 */
public class User {

    public final ProxiedPlayer player;
    private final Main main;
    public String lastName;
    public String nickname;
    public Date muted;
    public int mute_time;
    public String mute_reason;
    public Date banned;
    public int ban_time;
    public String ban_reason;
    public Channel channel;

    public User(Main main, ProxiedPlayer player) {
        this.main = main;
        this.player = player;
        main.sql.checkUser(player.getUniqueId().toString());
        ArrayList<Object> data = main.sql.getUserData(player.getUniqueId().toString());
        muted = (Date) data.get(0);
        mute_time = (Integer) data.get(1);
        banned = (Date) data.get(2);
        ban_time = (Integer) data.get(3);
        lastName = (String) data.get(4);
        nickname = (String) data.get(5);
        channel = main.channels.getDefaultChannel();
    }

    public void save() {
        main.sql.saveUser(this);
    }

    public String getName() {
        if (nickname == null)
            return player.getName();
        return nickname;
    }

    public boolean isBanned() {
        return banned != null;
    }

    public String toString() {
        return "[player_bungee_uuid:" + player.getUniqueId().toString() + ", nickname:" + nickname + ", lastname:" + lastName + ", muted:" + muted + ", mute_time:" + mute_time + ", banned:" + banned + ", ban_time:" + ban_time + ", channel:" + channel + "]";
    }
}
