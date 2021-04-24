package me.Frxq15.PlayerPerks.Commands;

import me.Frxq15.PlayerPerks.Main;
import me.Frxq15.PlayerPerks.PerkManager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlyCommand implements CommandExecutor {
    public static List<String> flying = new ArrayList<String>();
    @Override
    public boolean onCommand(CommandSender p, Command command, String s, String[] strings) {
        if(!p.hasPermission("playerperks.fly")) {
            p.sendMessage(Main.formatMsg("NO_PERMISSION"));
            return true;
        }

        if(strings.length == 0) {
            if(!(p instanceof Player)) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[PlayerPerks] This command cannot be executed from console.");
                return true;
            }
            Player p2 = (Player) p;
            if(p2.isFlying()) {
                p2.setFlying(false);
                p2.setAllowFlight(false);
                p2.sendMessage(Main.formatMsg("FLY_DISABLED").replace("%player%", Manager.getPlayerMeta(p2)));
                flying.remove(p2.getName());
                return true;
            }
            p2.setAllowFlight(true);
            p2.setFlying(true);
            p2.sendMessage(Main.formatMsg("FLY_ENABLED").replace("%player%", Manager.getPlayerMeta(p2)));
            flying.add(p2.getName());
            return true;
        }
        if(strings.length == 1) {
            Player target = Bukkit.getPlayer(strings[0]);

            if(target == null) {
                p.sendMessage(Main.formatMsg("TARGET_NOT_FOUND"));
                return true;
            }
            if(target.isFlying()) {
                target.setFlying(false);
                target.setAllowFlight(false);
                p.sendMessage(Main.formatMsg("FLY_DISABLED_OTHER").replace("%player%", Manager.getPlayerMeta(target)));
                target.sendMessage(Main.formatMsg("FLY_DISABLED").replace("%player%", Manager.getPlayerMeta(target)));
                flying.remove(target.getName());
                return true;
            }
            target.setAllowFlight(true);
            target.setFlying(true);
            p.sendMessage(Main.formatMsg("FLY_ENABLED_OTHER").replace("%player%", Manager.getPlayerMeta(target)));
            target.sendMessage(Main.formatMsg("FLY_ENABLED").replace("%player%", Manager.getPlayerMeta(target)));
            flying.add(target.getName());
            return true;

        }
        p.sendMessage(Main.colourize("&cUsage: /fly <player>"));
        return true;
    }
}
