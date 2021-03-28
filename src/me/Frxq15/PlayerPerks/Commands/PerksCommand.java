package me.Frxq15.PlayerPerks.Commands;

import me.Frxq15.PlayerPerks.GUIManager.PerksMenu;
import me.Frxq15.PlayerPerks.Main;
import me.Frxq15.PlayerPerks.PerkManager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerksCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[PlayerPerks] This command cannot be executed from console.");
            return true;
        }
        Player p = (Player) commandSender;

        if(!p.hasPermission("playerperks.perks")) {
            p.sendMessage(Main.formatMsg("NO_PERMISSION"));
            return true;
        }
        if(strings.length == 0) {
            new PerksMenu(p).open(p);
        }
        new PerksMenu(p).open(p);
        return true;
    }
}
