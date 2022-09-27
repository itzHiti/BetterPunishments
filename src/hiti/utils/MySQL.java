package hiti.utils;

import java.util.*;
import hiti.main.*;
import hiti.api.*;
import java.sql.*;
import org.bukkit.*;
import org.bukkit.plugin.*;

public class MySQL extends Thread {

    private String host;
    private String username;
    private String password;
    private String table;
    private Connection connection;
    private String port;

    public MySQL(final String host, final String username, final String password, final String table, final String port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.table = table;
        this.port = port;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            ReflectiveOperationException e = ex;
            e.printStackTrace();
        }
        this.execute("CREATE TABLE IF NOT EXISTS `Ban` (`id` int NOT NULL AUTO_INCREMENT, `username` varchar(255) NOT NULL UNIQUE, `admin` varchar(255) NOT NULL, `reason` varchar(255) NOT NULL, `time` LONG NOT NULL, `comment` varchar(255) NOT NULL, `type` varchar(255) NOT NULL, `expire` LONG NOT NULL,  PRIMARY KEY (`id`)) DEFAULT CHARSET=utf8 COLLATE utf8_bin AUTO_INCREMENT=0");
        this.execute("CREATE TABLE IF NOT EXISTS `Mute` (`id` int NOT NULL AUTO_INCREMENT, `username` varchar(255) NOT NULL UNIQUE, `admin` varchar(255) NOT NULL, `reason` varchar(255) NOT NULL, `time` LONG NOT NULL, `comment` varchar(255) NOT NULL, `type` varchar(255) NOT NULL, `expire` LONG NOT NULL, PRIMARY KEY (`id`)) DEFAULT CHARSET=utf8 COLLATE utf8_bin AUTO_INCREMENT=0");
    }

    public static String strip(String str) {
        str = str.replaceAll("<[^>]*>", "");
        str = str.replace("\\", "\\\\");
        str = str.trim();
        return str;
    }

    public Connection getConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed() && this.connection.isValid(20)) {
                return this.connection;
            }
            final Properties p = new Properties();
            p.setProperty("user", this.username);
            p.setProperty("password", this.password);
            p.setProperty("useUnicode", "true");
            p.setProperty("characterEncoding", "cp1251");
            p.setProperty("autoReconnect", "true");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.table, p);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connection;
    }

    public void execute(final String query) {
        try {
            final PreparedStatement statement = this.getConnection().prepareStatement(query);
            statement.execute();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasAccountBan(final String playerName) {
        try {
            return BetterPunishments.sql.getConnection().createStatement().executeQuery("SELECT * FROM `Ban` WHERE `username`='" + playerName.toLowerCase() + "'").next();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean hasAccountMute(final String playerName) {
        try {
            return BetterPunishments.sql.getConnection().createStatement().executeQuery("SELECT * FROM `Mute` WHERE `username`='" + playerName.toLowerCase() + "'").next();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void loadBan(final String playerName) {
        if (BanManager.getBans().containsKey(playerName.toLowerCase())) {
            return;
        }
        try {
            final ResultSet result = BetterPunishments.sql.getConnection().createStatement().executeQuery("SELECT * FROM `Ban` WHERE `username`='" + playerName.toLowerCase() + "'");
            if (result.next()) {
                BanManager.addBan(result.getString("username"), result.getString("admin"), result.getString("reason"), result.getLong("time"), result.getString("comment"), Enum.valueOf(BanType.class, result.getString("type")), System.currentTimeMillis());
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void loadMute(final String playerName) {
        if (BanManager.getBans().containsKey(playerName.toLowerCase())) {
            return;
        }
        try {
            final ResultSet result = BetterPunishments.sql.getConnection().createStatement().executeQuery("SELECT * FROM `Mute` WHERE `username`='" + playerName.toLowerCase() + "'");
            if (result.next()) {
                BanManager.addBan(result.getString("username"), result.getString("admin"), result.getString("reason"), result.getLong("time"), result.getString("comment"), Enum.valueOf(BanType.class, result.getString("type")), System.currentTimeMillis());
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean hasBan(final String playerName) {
        try {
            return BetterPunishments.sql.getConnection().createStatement().executeQuery("SELECT * FROM `Ban` WHERE `username`='" + playerName.toLowerCase() + "'").next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasMute(final String playerName) {
        try {
            return BetterPunishments.sql.getConnection().createStatement().executeQuery("SELECT * FROM `Mute` WHERE `username`='" + playerName.toLowerCase() + "'").next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect() {
        try {
            this.getConnection().close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadBans() {
        try {
            ResultSet result = BetterPunishments.getInstance().getMySQL().getConnection().createStatement().executeQuery("SELECT * FROM `Ban`");
            while (result.next()) {
                BanManager.addBan(result.getString("username"), result.getString("admin"), result.getString("reason"), result.getLong("time"), result.getString("comment"), Enum.valueOf(BanType.class, result.getString("type")), result.getLong("expire"));
            }
            result = BetterPunishments.getInstance().getMySQL().getConnection().createStatement().executeQuery("SELECT * FROM `Mute`");
            while (result.next()) {
                BanManager.addMute(result.getString("username"), result.getString("admin"), result.getString("reason"), result.getLong("time"), result.getString("comment"), Enum.valueOf(BanType.class, result.getString("type")), result.getLong("expire"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBans() {
    }

    @Override
    public void run() {
        Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)BetterPunishments.getInstance(), () -> {
            try {
                BanManager.getMutes().clear();
                BanManager.getBans().clear();
                ResultSet result = BetterPunishments.getInstance().getMySQL().getConnection().createStatement().executeQuery("SELECT * FROM `Ban`");
                while (result.next()) {
                    if (BanType.TEMPBAN == Enum.valueOf(BanType.class, result.getString("type")) && result.getLong("time") < 0L) {
                        this.execute("DELETE FROM `Ban` WHERE `username`='" + result.getString("username") + "'");
                    }
                    BanManager.addBan(result.getString("username"), result.getString("admin"), result.getString("reason"), result.getLong("time"), result.getString("comment"), Enum.valueOf(BanType.class, result.getString("type")), System.currentTimeMillis());
                }
                ResultSet result2 = BetterPunishments.getInstance().getMySQL().getConnection().createStatement().executeQuery("SELECT * FROM `Mute`");
                while (result2.next()) {
                    if (BanType.TEMPMUTE == Enum.valueOf(BanType.class, result2.getString("type")) && result2.getLong("time") < 0L) {
                        this.execute("DELETE FROM `Mute` WHERE `player`='" + result2.getString("username") + "'");
                    }
                    BanManager.addMute(result2.getString("username"), result2.getString("admin"), result2.getString("reason"), result2.getLong("time"), result2.getString("comment"), Enum.valueOf(BanType.class, result2.getString("type")), System.currentTimeMillis());
                }
                result2.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }, 0L, 100L);
    }
}
