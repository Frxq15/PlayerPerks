package me.Frxq15.PlayerPerks.Commands;

import me.Frxq15.PlayerPerks.Main;
import me.Frxq15.PlayerPerks.SQLManager.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class clearAFKmessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender p, Command command, String s, String[] strings) {
        if(!p.hasPermission("playerperks.command.setafkmessage")) {
            p.sendMessage(Main.formatMsg("NO_PERMISSION"));
            return true;
        }
        if(strings.length == 0) {
            if(!(p instanceof Player)) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[PlayerPerks] This command cannot be executed from console.");
                return true;
            }
            Player p2 = (Player) p;
            PlayerData playerData = PlayerData.getPlayerData(Main.getInstance(), p2.getUniqueId());
            playerData.setAFKMessage("&7is now afk.");
            p2.sendMessage(Main.formatMsg("AFK_MESSAGE_CLEARED"));
            return true;
        }
        if(strings.length == 1) {
            if(!p.hasPermission("playerperks.clearafkmessage.others")) {
                p.sendMessage(Main.formatMsg("NO_PERMISSION"));
                return true;
            }
            Player target = Bukkit.getPlayer(strings[0]);
            if(target == null) {
                p.sendMessage(Main.formatMsg("TARGET_NOT_FOUND"));
                return true;
            }
            PlayerData playerData = PlayerData.getPlayerData(Main.getInstance(), target.getUniqueId());
            playerData.setAFKMessage("&7is now afk.");
            p.sendMessage(Main.formatMsg("AFK_MESSAGE_CLEARED_OTHER").replace("%player%", target.getName()));
            target.sendMessage(Main.formatMsg("AFK_MESSAGE_CLEARED"));
            return true;

        }
        p.sendMessage(Main.colourize("&cUsage: /clearafkmessage <player>"));
        return true;
    }
}
