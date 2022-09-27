package hiti.api;

import java.util.concurrent.*;
import hiti.utils.*;
import hiti.main.*;

public class BanManager {

    private static ConcurrentHashMap<String, Ban> bans;
    private static ConcurrentHashMap<String, Ban> mutes;

    public static Ban createBan(final String playerName, final String admin, final String reason, final long time, final String comment, final BanType type) {
        final Ban ban = new Ban(MySQL.strip(playerName.toLowerCase()), MySQL.strip(admin), MySQL.strip(reason), time, MySQL.strip(comment), type, System.currentTimeMillis());
        BanManager.bans.put(MySQL.strip(playerName.toLowerCase()), ban);
        BetterPunishments.getInstance().getMySQL().execute("INSERT INTO `Ban` (`username`, `admin`, `reason`, `time`, `comment`, `type`, `expire`) VALUES ('" + MySQL.strip(playerName.toLowerCase()) + "','" + MySQL.strip(admin) + "','" + MySQL.strip(reason) + "','" + time + "','" + MySQL.strip(comment) + "','" + type + "','" + System.currentTimeMillis() + "')");
        return ban;
    }

    public static Ban addBan(final String playerName, final String admin, final String reason, final long time, final String comment, final BanType type, final long expire) {
        final Ban ban = new Ban(MySQL.strip(playerName.toLowerCase()), MySQL.strip(admin), MySQL.strip(reason), time, MySQL.strip(comment), type, expire);
        BanManager.bans.put(MySQL.strip(playerName.toLowerCase()), ban);
        return ban;
    }

    public static void unBan(final String playerName) {
        BanManager.bans.remove(playerName.toLowerCase());
        BetterPunishments.getInstance().getMySQL().execute("DELETE FROM `Ban` WHERE `username`='" + MySQL.strip(playerName.toLowerCase()) + "'");
    }

    public static Ban createMute(final String playerName, final String admin, final String reason, final long time, final String comment, final BanType type) {
        final Ban ban = new Ban(MySQL.strip(playerName.toLowerCase()), MySQL.strip(admin), MySQL.strip(reason), time, MySQL.strip(comment), type, System.currentTimeMillis());
        BanManager.mutes.put(MySQL.strip(playerName.toLowerCase()), ban);
        BetterPunishments.getInstance().getMySQL().execute("INSERT INTO `Mute` (`username`, `admin`, `reason`, `time`, `comment`, `type`, `expire`) VALUES ('" + MySQL.strip(playerName.toLowerCase()) + "','" + MySQL.strip(admin) + "','" + MySQL.strip(reason) + "','" + time + "','" + MySQL.strip(comment) + "','" + type + "','" + System.currentTimeMillis() + "')");
        return ban;
    }

    public static void editBan(final Ban ban) {
        final Ban bban = new Ban(MySQL.strip(ban.getName()), MySQL.strip(ban.getAdmin()), MySQL.strip(ban.getReason()), ban.getTimeLeft(), MySQL.strip(ban.getComment()), ban.getType(), ban.getExpire());
        BetterPunishments.getInstance().getMySQL().execute("UPDATE `Ban` SET `time`='" + bban.getTimeLeft() + "' WHERE `username`='" + MySQL.strip(bban.getName()) + "'");
        BanManager.bans.remove(bban.getName().toLowerCase());
        BanManager.bans.put(MySQL.strip(bban.getName().toLowerCase()), bban);
    }

    public static void editMute(final Ban ban) {
        final Ban bban = new Ban(MySQL.strip(ban.getName()), MySQL.strip(ban.getAdmin()), MySQL.strip(ban.getReason()), ban.getTimeLeft(), MySQL.strip(ban.getComment()), ban.getType(), ban.getExpire());
        BetterPunishments.getInstance().getMySQL().execute("UPDATE `Mute` SET `time`='" + bban.getTimeLeft() + "' WHERE `username`='" + MySQL.strip(bban.getName()) + "'");
        BanManager.mutes.remove(bban.getName().toLowerCase());
        BanManager.mutes.put(MySQL.strip(bban.getName().toLowerCase()), bban);
    }

    public static Ban addMute(final String playerName, final String admin, final String reason, final long time, final String comment, final BanType type, final long expire) {
        final Ban ban = new Ban(MySQL.strip(playerName.toLowerCase()), MySQL.strip(admin), MySQL.strip(reason), time, MySQL.strip(comment), type, expire);
        BanManager.mutes.put(MySQL.strip(playerName.toLowerCase()), ban);
        return ban;
    }

    public static void unMute(final String playerName) {
        BanManager.mutes.remove(playerName.toLowerCase());
        BetterPunishments.getInstance().getMySQL().execute("DELETE FROM `Mute` WHERE `username`='" + MySQL.strip(playerName.toLowerCase()) + "'");
    }

    public static ConcurrentHashMap<String, Ban> getBans() {
        return BanManager.bans;
    }

    public static ConcurrentHashMap<String, Ban> getMutes() {
        return BanManager.mutes;
    }

    public static Ban getBan(final String playerName) {
        return BanManager.bans.get(MySQL.strip(playerName.toLowerCase()));
    }

    public static Ban getMute(final String playerName) {
        return BanManager.mutes.get(MySQL.strip(playerName.toLowerCase()));
    }

    public static boolean isBan(final String playerName) {
        final Ban ban = BanManager.bans.get(playerName.toLowerCase());
        if (ban == null) {
            return false;
        }
        if (ban.getType() == BanType.TEMPBAN && ban.isLeft()) {
            unBan(playerName);
            return false;
        }
        return true;
    }

    public static boolean isMute(final String playerName) {
        final Ban mute = BanManager.mutes.get(playerName.toLowerCase());
        if (mute == null) {
            return false;
        }
        if (mute.getType() == BanType.TEMPMUTE && mute.isLeft()) {
            unMute(playerName);
            return false;
        }
        return true;
    }

    static {
        BanManager.bans = new ConcurrentHashMap<String, Ban>();
        BanManager.mutes = new ConcurrentHashMap<String, Ban>();
    }
}
