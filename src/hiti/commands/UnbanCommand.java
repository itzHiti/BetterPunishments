package hiti.commands;

import org.bukkit.command.*;
import hiti.cooldown.*;
import hiti.api.*;
import hiti.main.*;
import org.bukkit.*;
import hiti.utils.*;

public class UnbanCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String l, final String[] args) {
        if (!sender.hasPermission("hiti.unban")) {
            sender.sendMessage(ConfigUtil.getMSG("no_permission"));
            return false;
        }
        if (!CooldownManager.hasCdw(sender.getName(), "unban")) {
            sender.sendMessage(ConfigUtil.getMSG("cooldown_unban_command").replace("{time}", TimeUtils.getTimeС(CooldownManager.getLeftTime(sender.getName(), "unban"))));
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage(ConfigUtil.getMSG("usage_unban_command"));
            return false;
        }
        if (!BanManager.isBan(args[0])) {
            sender.sendMessage(ConfigUtil.getMSG("player_null_unban").replace("{target}", args[0]));
            return false;
        }
        BanManager.unBan(args[0]);
        if (sender.getName().toLowerCase().equalsIgnoreCase(args[0].toLowerCase())) {
            sender.sendMessage(ConfigUtil.getMSG("successfully_unban_me"));
            HoverChat.sendMessage(ConfigUtil.getMSG("unbanned_broadcast_me").replace("{admin}", sender.getName()));
        }
        else {
            sender.sendMessage(ConfigUtil.getMSG("successfully_unban").replace("{target}", args[0]));
            HoverChat.sendMessage(ConfigUtil.getMSG("unbanned_broadcast").replace("{admin}", sender.getName()).replace("{target}", args[0]));
        }
        if (!sender.hasPermission("Bans.unban-cooldown.bypass") && BetterPunishments.config.getString("Cooldown-Manager.unban." + VaultPermission.getGroupName(Bukkit.getPlayerExact(sender.getName()))) != null) {
            CooldownManager.createCooldown(sender.getName(), "unban", TimeUtils.longTime(BetterPunishments.config.getString("Cooldown-Manager.unban." + VaultPermission.getGroupName(Bukkit.getPlayer(sender.getName())))));
        }
        return false;
    }
}

