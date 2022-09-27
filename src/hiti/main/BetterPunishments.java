package hiti.main;

import hiti.commands.*;
import org.bukkit.plugin.java.*;
import org.bukkit.configuration.file.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import hiti.utils.*;
import org.bukkit.event.*;
import org.bukkit.command.*;
import hiti.api.*;

public class BetterPunishments extends JavaPlugin {

    public static MySQL sql;
    private static BetterPunishments inst;
    public static FileConfiguration config;
    public static FileConfiguration rules;
    public static FileConfiguration messages;

    public void onEnable() {
        (BetterPunishments.inst = this).saveDefaultConfig();
        BetterPunishments.config = ConfigUtil.getFile("config.yml");
        BetterPunishments.messages = ConfigUtil.getFile("messages.yml");
        BetterPunishments.rules = ConfigUtil.getFile("rules.yml");
        Bukkit.getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
        System.out.println("[" + this.getDescription().getName() + "] DataBase loading..");
        (BetterPunishments.sql = new MySQL(BetterPunishments.config.getString("MySQL.host"), BetterPunishments.config.getString("MySQL.username"), BetterPunishments.config.getString("MySQL.password"), BetterPunishments.config.getString("MySQL.database"), BetterPunishments.config.getString("MySQL.port"))).loadBans();
        System.out.println("[" + this.getDescription().getName() + "] " + "DataBase loading finish!");
        VaultPermission.setupPermission();
        Bukkit.getPluginManager().registerEvents((Listener)new BetterListeners(), (Plugin)this);
        this.getCommand("ban").setExecutor((CommandExecutor)new BanCommand());
        this.getCommand("kick").setExecutor((CommandExecutor)new KickCommand());
        this.getCommand("rules").setExecutor((CommandExecutor)new RulesCommand());
        this.getCommand("unban").setExecutor((CommandExecutor)new UnbanCommand());
        this.getCommand("tempban").setExecutor((CommandExecutor)new TempbanCommand());
        this.getCommand("mute").setExecutor((CommandExecutor)new MuteCommand());
        this.getCommand("baninfo").setExecutor((CommandExecutor)new BaninfoCommand());
        this.getCommand("unmute").setExecutor((CommandExecutor)new UnmuteCommand());
        this.getCommand("tempmute").setExecutor((CommandExecutor)new TempmuteCommand());
        this.getCommand("kill").setExecutor((CommandExecutor)new KillCommand());
        this.getCommand("reload").setExecutor((CommandExecutor)new ReloadCommand());
        this.getCommand("whocreatedbans").setExecutor((CommandExecutor)new DeveloperCommand());
        new MySQL(BetterPunishments.config.getString("MySQL.host"), BetterPunishments.config.getString("MySQL.username"), BetterPunishments.config.getString("MySQL.password"), BetterPunishments.config.getString("MySQL.database"), BetterPunishments.config.getString("MySQL.port")).start();
        System.out.println("[" + this.getDescription().getName() + "] " + "Enabled!");
    }

    public void onDisable() {
        try {
            for (final Ban ban : BanManager.getBans().values()) {
                if (ban.getType() == BanType.TEMPBAN && ban.isLeft()) {
                    BanManager.unBan(ban.getName());
                }
            }
            for (final Ban ban : BanManager.getMutes().values()) {
                if (ban.getType() == BanType.TEMPMUTE && ban.isLeft()) {
                    BanManager.unMute(ban.getName());
                }
            }
            BetterPunishments.sql.disconnect();
        }
        catch (Exception ex) {}
        System.out.println("[" + this.getDescription().getName() + "] " + "Disabled!");
    }

    public static BetterPunishments getInstance() {
        return BetterPunishments.inst;
    }

    public MySQL getMySQL() {
        return BetterPunishments.sql;
    }
}
