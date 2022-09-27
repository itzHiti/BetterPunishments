package hiti.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import hiti.main.*;
import hiti.utils.*;

public class ReloadCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (commandSender instanceof Player && !BetterPunishments.config.getStringList("admins_list").contains(commandSender.getName())) {
            commandSender.sendMessage(ConfigUtil.getMSG("no_permission"));
            return false;
        }
        BetterPunishments.config = ConfigUtil.getFile("config.yml");
        BetterPunishments.messages = ConfigUtil.getFile("messages.yml");
        BetterPunishments.rules = ConfigUtil.getFile("rules.yml");
        commandSender.sendMessage(ConfigUtil.getMSG("reload_configurations"));
        return false;
    }
}
