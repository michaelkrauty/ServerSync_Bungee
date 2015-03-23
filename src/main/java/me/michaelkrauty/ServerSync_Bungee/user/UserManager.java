package me.michaelkrauty.ServerSync_Bungee.user;

import me.michaelkrauty.ServerSync_Bungee.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

/**
 * Created on 3/21/2015.
 *
 * @author michaelkrauty
 */
public class UserManager {

    private final Main main;

    private ArrayList<User> users = new ArrayList<User>();

    public UserManager(Main main) {
        this.main = main;
    }

    public User get(ProxiedPlayer player) {
        for (User user : users) {
            if (user.player == player)
                return user;
        }
        User user = new User(main, player);
        users.add(user);
        return user;
    }

    public void remove(ProxiedPlayer player) {
        for (User user : users) {
            if (user.player == player)
                users.remove(user);
        }
    }
}
