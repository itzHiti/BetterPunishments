package hiti.commands;

import org.bukkit.command.*;
import hiti.utils.*;
import hiti.main.*;
import java.util.*;

public class RulesCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String l, final String[] args) {
        if (!sender.hasPermission("hiti.rules")) {
            sender.sendMessage(ConfigUtil.getMSG("no_permission"));
            return false;
        }
        final ArrayList<String> list = new ArrayList<String>();
        for (final String d : BetterPunishments.rules.getConfigurationSection("rules").getKeys(false)) {
            list.add(d.replace("|", "."));
        }
        int page = 1;
        if (args.length != 0) {
            try {
                page = Math.abs(Integer.parseInt(args[0]));
            }
            catch (Exception e) {
                sender.sendMessage(ConfigUtil.getMSG("page_null_number"));
                return false;
            }
            if (page == 0) {
                page = 1;
            }
        }
        final int pages = (int)Math.ceil(list.size() / 5.0);
        if (page > pages) {
            sender.sendMessage(ConfigUtil.getMSG("null_page"));
            return false;
        }
        page = (page - 1) * 5;
        final StringBuffer sb = new StringBuffer();
        for (int p = page, i = 0; p < list.size() && i != 5; ++p, ++i) {
            sb.append(ConfigUtil.getMSG("line_list_format").replace("{number-rules}", list.get(p)).replace("{rules}", BetterPunishments.rules.getString("rules." + list.get(p).replace(".", "|") + ".text")).replace("{values}", BetterPunishments.rules.getStringList("rules." + list.get(p).replace(".", "|") + ".type").toString().replace("[", "").replace("]", ""))).append("\n");
        }
        sender.sendMessage(ConfigUtil.getMSG("list_format").replace("{page}", Integer.toString((int)(Math.ceil(page / 5.0) + 1.0))).replace("{pages}", Integer.toString(pages)).replace("{list}", sb.toString()));
        return false;
    }
}
