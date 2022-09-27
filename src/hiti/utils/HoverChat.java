package hiti.utils;

import java.io.*;
import org.bukkit.*;
import hiti.main.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import com.google.common.collect.*;
import net.md_5.bungee.api.chat.*;

public class HoverChat {

    private static boolean enable;

    public static void sendHover(final String message, final String show_text) {
        if (HoverChat.enable) {
            final ByteArrayOutputStream b = new ByteArrayOutputStream();
            final DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("sendTextComponent");
                out.writeUTF(message);
                out.writeUTF(show_text);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            if (Bukkit.getOnlinePlayers().size() == 0) {
                Bukkit.getServer().sendPluginMessage((Plugin)BetterPunishments.getInstance(), "BungeeCord", b.toByteArray());
            }
            else {
                ((Player)Iterables.getFirst((Iterable)Bukkit.getOnlinePlayers(), (Object)null)).sendPluginMessage((Plugin)BetterPunishments.getInstance(), "BungeeCord", b.toByteArray());
            }
        }
        else {
            final TextComponent tc = new TextComponent(message);
            tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(show_text).create()));
            broadcast(tc);
        }
    }

    public static void sendMessage(final String message) {
        if (HoverChat.enable) {
            final ByteArrayOutputStream b = new ByteArrayOutputStream();
            final DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("sendMSG");
                out.writeUTF(message);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            if (Bukkit.getOnlinePlayers().size() == 0) {
                Bukkit.getServer().sendPluginMessage((Plugin)BetterPunishments.getInstance(), "BungeeCord", b.toByteArray());
            }
            else {
                ((Player)Iterables.getFirst((Iterable)Bukkit.getOnlinePlayers(), (Object)null)).sendPluginMessage((Plugin)BetterPunishments.getInstance(), "BungeeCord", b.toByteArray());
            }
        }
        else {
            Bukkit.broadcastMessage(message);
        }
    }

    public static void sendMessage(final String message, final String playerName) {
        if (HoverChat.enable) {
            final ByteArrayOutputStream b = new ByteArrayOutputStream();
            final DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("Message");
                out.writeUTF(playerName);
                out.writeUTF(message);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            if (Bukkit.getOnlinePlayers().size() == 0) {
                Bukkit.getServer().sendPluginMessage((Plugin)BetterPunishments.getInstance(), "BungeeCord", b.toByteArray());
            }
            else {
                ((Player)Iterables.getFirst((Iterable)Bukkit.getOnlinePlayers(), (Object)null)).sendPluginMessage((Plugin)BetterPunishments.getInstance(), "BungeeCord", b.toByteArray());
            }
        }
        else if (Bukkit.getPlayer(playerName) != null) {
            Bukkit.getPlayer(playerName).sendMessage(message);
        }
    }

    public static void broadcast(final TextComponent tc) {
        Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage((BaseComponent)tc));
    }

    static {
        HoverChat.enable = BetterPunishments.config.getBoolean("Settings.bungee_module_enable");
    }
}

