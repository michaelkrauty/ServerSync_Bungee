package me.michaelkrauty.ServerSync_Bungee.user;

import me.michaelkrauty.ServerSync_Bungee.Main;
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
    public String nickname;
    public Date muted;
    public int mute_time;
    public Date banned;
    public int ban_time;

    public User(Main main, ProxiedPlayer player) {
        this.main = main;
        this.player = player;
        ArrayList<Object> data = main.sql.getUserData(player.getUniqueId().toString());
        if ((Integer) data.get(0) != 0)
            muted = new Date((Integer) data.get(0));
        mute_time = (Integer) data.get(1);
        banned = new Date((Integer) data.get(2));
        ban_time = (Integer) data.get(3);
        nickname = (String) data.get(4);
    }

    public void save() {
        main.sql.saveUser(this);
    }
}
