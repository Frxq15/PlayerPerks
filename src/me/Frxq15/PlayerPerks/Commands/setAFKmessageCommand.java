package me.Frxq15.PlayerPerks.Commands;

import me.Frxq15.PlayerPerks.Main;
import me.Frxq15.PlayerPerks.PerkManager.Manager;
import me.Frxq15.PlayerPerks.SQLManager.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class setAFKmessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender c, Command command, String s, String[] strings) {
        if (!(c instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[PlayerPerks] This command cannot be executed from console.");
            return true;
        }
        if (!c.hasPermission("playerperks.command.setafkmessage")) {
            c.sendMessage(Main.formatMsg("AFK_MESSAGE_LOCKED"));
            return true;
        }
        if (strings.length > 0) {
            Player p = (Player) c;
            PlayerData playerData = PlayerData.getPlayerData(Main.getInstance(), p.getUniqueId());
                String msg = getFinalArg(strings, 0);
                playerData.setAFKMessage(msg);
                p.sendMessage(Main.formatMsg("AFK_MESSAGE_SET").replace("%message%", Main.colourize(msg)));
                return true;
        }
        c.sendMessage(Main.colourize("&cUsage: /setafkmessage <message>"));
        return true;
    }
    public static String getFinalArg(String[] args, int start) {
        StringBuilder bldr = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i != start) {
                bldr.append(" ");
            }
            bldr.append(args[i]);
        }
        return bldr.toString();
    }
}
