package hiti.cooldown;

import com.google.common.collect.*;

public class CooldownManager {

    public static TreeBasedTable<String, String, Cooldown> accs = TreeBasedTable.create();

    public static void createCooldown(final String playerName, final String name, final long time) {
        final Cooldown c = new Cooldown(playerName.toLowerCase(), name, time);
        CooldownManager.accs.put(playerName.toLowerCase(), name, c);
    }

    public static boolean hasCdw(final String playerName, final String name) {
        final Cooldown c = CooldownManager.accs.get(playerName, name);
        if (c == null) {
            return true;
        }
        if (c.isLeft()) {
            CooldownManager.accs.remove(playerName.toLowerCase(), name);
            return false;
        }
        return true;
    }

    public static long getLeftTime(final String playerName, final String name) {
        return CooldownManager.accs.get(playerName, name).getLeftTime();
    }
}