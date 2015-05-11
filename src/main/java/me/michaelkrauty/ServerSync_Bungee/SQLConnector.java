package me.michaelkrauty.ServerSync_Bungee;

import me.michaelkrauty.ServerSync_Bungee.user.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created on 3/21/2015.
 *
 * @author michaelkrauty
 */
public class SQLConnector {

    private final String jdbc;
    private final String user;
    private final String pass;
    private final String prefix;
    private Main main;
    private Connection connection;

    public SQLConnector(Main main) {
        this.main = main;
        jdbc = "jdbc:mysql://" + main.config.getDBHost() + ":" + main.config.getDBPort() + "/" + main.config.getDBName();
        user = main.config.getDBUser();
        pass = main.config.getDBPass();
        prefix = main.config.getDBPrefix();
        openConnection();
        checkTables();
    }

    public synchronized void openConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                main.getLogger().info("Connecting to database...");
                connection = DriverManager.getConnection(jdbc, user, pass);
                main.getLogger().info("Connected to database.");
            }
        } catch (Exception e) {
            main.getLogger().log(Level.SEVERE, "Connection to database failed!");
            e.printStackTrace();
        }
    }

    public synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    main.getLogger().info("Closed connection to database.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void checkTables() {
        openConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + prefix + "users` (uuid varchar(256) PRIMARY KEY, banned date, ban_time int(255), ban_reason varchar(256), muted date, mute_time int(255), mute_reason varchar(256), lastname varchar(256), nickname varchar(256));");
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkUser(String uuid) {
        if (getUserData(uuid) == null) {
            try {
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO `" + prefix + "users` (`uuid`, `banned`, `ban_time`, `ban_reason`, `muted`, `mute_time`, `mute_reason`, `lastname`, `nickname`) VALUES (?,null,-1,null,null,-1,null,null,null);");
                stmt.setString(1, uuid);
                stmt.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Object> getUserData(String uuid) {
        try {
            ArrayList<Object> r = new ArrayList<Object>();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `" + prefix + "users` WHERE uuid=?");
            stmt.setString(1, uuid);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                r.add(result.getDate("banned"));
                r.add(result.getInt("ban_time"));
                r.add(result.getString("ban_reason"));
                r.add(result.getDate("muted"));
                r.add(result.getInt("mute_time"));
                r.add(result.getString("mute_reason"));
                r.add(result.getString("lastname"));
                r.add(result.getString("nickname"));
                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUser(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE `" + prefix + "users` SET `banned`=?, `ban_time`=-1, `ban_reason`=NULL `muted`=?, `mute_time`=-1, `mute_reason`=NULL `lastname`=?, `nickname`=? WHERE `uuid`=?");
            stmt.setDate(1, user.banned);
            stmt.setDate(2, user.muted);
            stmt.setString(3, user.nickname);
            stmt.setString(4, user.lastName);
            stmt.setString(5, user.player.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
