package hiti.commands;


import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.*;
import hiti.cooldown.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import hiti.main.*;
import net.md_5.bungee.api.*;
import hiti.utils.*;

public class KickCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String l, final String[] args) {
        if (!sender.hasPermission("hiti.kick")) {
            sender.sendMessage(ConfigUtil.getMSG("no_permission"));
            return false;
        }
        if (!CooldownManager.hasCdw(sender.getName(), "kick")) {
            sender.sendMessage(ConfigUtil.getMSG("cooldown_kick_command").replace("{time}", TimeUtils.getTimeС(CooldownManager.getLeftTime(sender.getName(), "kick"))));
            return false;
        }
        if (args.length < 3) {
            sender.sendMessage(ConfigUtil.getMSG("usage_kick_command"));
            return false;
        }
        final Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(ConfigUtil.getMSG("player_is_offline").replace("{target}", args[0]));
            return false;
        }
        if (args[0].toLowerCase().equalsIgnoreCase(sender.getName().toLowerCase())) {
            sender.sendMessage(ConfigUtil.getMSG("sender_is_target_kick"));
            return false;
        }
        if (sender instanceof Player && VaultPermission.isHigherGroup(player, Bukkit.getPlayerExact(sender.getName()))) {
            sender.sendMessage(ConfigUtil.getMSG("higher_group_kick").replace("{target}", player.getName()));
            return false;
        }
        if (BetterPunishments.rules.getString("rules." + args[1].replace(".", "|")) == null) {
            sender.sendMessage(ConfigUtil.getMSG("rules_is_null").replace("{rules}", args[1]));
            return false;
        }
        if (!BetterPunishments.rules.getStringList("rules." + args[1].replace(".", "|") + ".type").contains("kick")) {
            sender.sendMessage(ConfigUtil.getMSG("corresponds_not_paragraph"));
            return false;
        }
        final StringBuffer sb = new StringBuffer();
        for (int i = 2; i < args.length; ++i) {
            sb.append(args[i]).append(" ");
        }
        if (sb.substring(0, sb.length() - 1).toString().length() < 3) {
            sender.sendMessage(ConfigUtil.getMSG("comment_little"));
            return false;
        }
        player.kickPlayer(ConfigUtil.getMSGInfo("kick_text").replace("{admin}", sender.getName()).replace("{number-rules}", args[1]).replace("{rules}", ChatColor.stripColor(BetterPunishments.rules.getString("rules." + args[1].replace(".", "|") + ".text").replace("&", "§"))).replace("{comment}", sb.substring(0, sb.length() - 1).toString()));
        sender.sendMessage(ConfigUtil.getMSG("successfully_kick").replace("{target}", args[0]));
        HoverChat.sendHover(ConfigUtil.getMSG("kicked_broadcast").replace("{admin}", sender.getName()).replace("{target}", player.getName()), ConfigUtil.getMSGHover("kick_text").replace("{number-rules}", args[1]).replace("{rules}", ChatColor.stripColor(BetterPunishments.rules.getString("rules." + args[1].replace(".", "|") + ".text").replace("&", "§"))).replace("{comment}", sb.substring(0, sb.length() - 1).toString()));
        if (!sender.hasPermission("Bans.kick-cooldown.bypass") && BetterPunishments.config.getString("Cooldown-Manager.kick." + VaultPermission.getGroupName(Bukkit.getPlayerExact(sender.getName()))) != null) {
            CooldownManager.createCooldown(sender.getName(), "kick", TimeUtils.longTime(BetterPunishments.config.getString("Cooldown-Manager.kick." + VaultPermission.getGroupName(Bukkit.getPlayer(sender.getName())))));
        }
        return false;
    }
}

