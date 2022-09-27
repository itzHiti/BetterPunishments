package hiti.utils;

import net.milkbowl.vault.permission.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import hiti.main.*;

public class VaultPermission
{
    private static Permission perm;

    public static boolean setupPermission() {
        final RegisteredServiceProvider<Permission> rsp = (RegisteredServiceProvider<Permission>)Bukkit.getServicesManager().getRegistration((Class)Permission.class);
        VaultPermission.perm = (Permission)rsp.getProvider();
        return VaultPermission.perm != null;
    }

    public static String getGroupName(final Player p) {
        return VaultPermission.perm.getPrimaryGroup(p);
    }

    public static boolean isHigherGroup(final Player player, final Player player1) {
        final String groupPlayer = VaultPermission.perm.getPrimaryGroup(player);
        final String groupSender = VaultPermission.perm.getPrimaryGroup(player1);
        return !player1.isOp() && (player.isOp() || BetterPunishments.config.getInt("groups-priority." + groupSender) < BetterPunishments.config.getInt("groups-priority." + groupPlayer));
    }

    static {
        VaultPermission.perm = null;
    }
}

