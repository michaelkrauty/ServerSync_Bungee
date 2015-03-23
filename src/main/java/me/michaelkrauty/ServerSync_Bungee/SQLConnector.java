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
            PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + prefix + "users` (uuid varchar(256) PRIMARY KEY, banned timestamp, ban_time int(255), muted timestamp, mute_time int(255), nickname varchar(256), admin tinyint(1));");
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkUser(String uuid) {
        try {
            // TODO
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO `" + prefix + "users` (`uuid`, `banned`, `ban_time`, `muted`, `mute_time`, `nickname`) VALUES (?,null,null,null,null,null);");
            stmt.setString(1, uuid);
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Object> getUserData(String uuid) {
        try {
            ArrayList<Object> r = new ArrayList<Object>();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `" + prefix + "users` WHERE uuid=?");
            stmt.setString(1, uuid);
            ResultSet result = stmt.executeQuery();
            r.add(result.getInt("banned"));
            r.add(result.getInt("ban_time"));
            r.add(result.getInt("muted"));
            r.add(result.getInt("mute_time"));
            r.add(result.getString("nickname"));
            r.add(result.getBoolean("admin"));
            return r;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUser(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE `" + prefix + "users` SET `banned`=?, `ban_time`=?, `muted`=?, `mute_time`=?, `nickname`=? WHERE `uuid`=?");
            stmt.setDate(1, user.banned);
            stmt.setInt(2, user.ban_time);
            stmt.setDate(3, user.muted);
            stmt.setInt(4, user.mute_time);
            stmt.setString(5, user.nickname);
            stmt.setString(6, user.player.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test() {
        try {
            // TODO
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
