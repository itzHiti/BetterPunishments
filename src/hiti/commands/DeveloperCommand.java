package hiti.commands;

import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.*;
import hiti.main.*;
import org.bukkit.*;
import net.md_5.bungee.api.chat.*;
import hiti.cooldown.*;

public class DeveloperCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        TextComponent tc = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7[&e" + BetterPunishments.getInstance().getName() + "&7] Плагин разработан itzHiti"));
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&eНажмите, чтобы перейти на страницу ВК разработчика")).create()));
        tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "vk.com/hitioff"));
        commandSender.sendMessage("");
        Bukkit.getPlayer(commandSender.getName()).spigot().sendMessage((BaseComponent)tc);
        commandSender.sendMessage("");
        return false;
    }
}