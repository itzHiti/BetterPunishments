package hiti.commands;

import org.bukkit.command.*;
import hiti.cooldown.*;
import hiti.main.*;
import hiti.api.*;
import org.bukkit.*;
import hiti.utils.*;

public class BanCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String l, final String[] args) {
        if (!sender.hasPermission("hiti.ban")) {
            sender.sendMessage(ConfigUtil.getMSG("no_permission"));
            return false;
        }
        if (!CooldownManager.hasCdw(sender.getName(), "ban")) {
            sender.sendMessage(ConfigUtil.getMSG("cooldown_ban_command").replace("{time}", TimeUtils.getTimeС(CooldownManager.getLeftTime(sender.getName(), "ban"))));
            return false;
        }
        if (args.length < 3) {
            sender.sendMessage(ConfigUtil.getMSG("usage_ban_command"));
            return false;
        }
        if (args[0].toLowerCase().equalsIgnoreCase(sender.getName().toLowerCase())) {
            sender.sendMessage(ConfigUtil.getMSG("sender_is_target_ban"));
            return false;
        }
        if (BanManager.isBan(args[0])) {
            sender.sendMessage(ConfigUtil.getMSG("player_is_banned").replace("{target}", args[0]));
            return false;
        }
        if (BetterPunishments.rules.getString("rules." + args[1].replace(".", "|")) == null) {
            sender.sendMessage(ConfigUtil.getMSG("rules_is_null").replace("{rules}", args[1]));
            return false;
        }
        if (!BetterPunishments.rules.getStringList("rules." + args[1].replace(".", "|") + ".type").contains("ban")) {
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
        BanManager.createBan(args[0], sender.getName(), args[1], 0L, sb.substring(0, sb.length() - 1).toString(), BanType.BAN);
        sender.sendMessage(ConfigUtil.getMSG("successfully_ban").replace("{target}", args[0]));
        HoverChat.sendHover(ConfigUtil.getMSG("banned_broadcast").replace("{admin}", sender.getName()).replace("{target}", args[0]), ConfigUtil.getMSGHover("ban_text").replace("{number-rules}", args[1]).replace("{rules}", ChatColor.stripColor(BetterPunishments.rules.getString("rules." + args[1].replace(".", "|") + ".text").replace("&", "§"))).replace("{comment}", sb.substring(0, sb.length() - 1).toString()));
        if (Bukkit.getPlayerExact(args[0]) != null) {
            Bukkit.getPlayerExact(args[0]).kickPlayer(ConfigUtil.getMSGInfo("ban_text").replace("{admin}", sender.getName()).replace("{comment}", sb.substring(0, sb.length() - 1).toString()).replace("{number-rules}", args[1]).replace("{rules}", BetterPunishments.rules.getString("rules." + args[1].replace(".", "|") + ".text")));
        }
        if (!sender.hasPermission("Bans.ban-cooldown.bypass") && BetterPunishments.config.getString("Cooldown-Manager.ban." + VaultPermission.getGroupName(Bukkit.getPlayerExact(sender.getName()))) != null) {
            CooldownManager.createCooldown(sender.getName(), "ban", TimeUtils.longTime(BetterPunishments.config.getString("Cooldown-Manager.ban." + VaultPermission.getGroupName(Bukkit.getPlayer(sender.getName())))));
        }
        return false;
    }
}

