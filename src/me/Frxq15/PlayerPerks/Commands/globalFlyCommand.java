package me.Frxq15.PlayerPerks.Commands;

import me.Frxq15.PlayerPerks.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

public class globalFlyCommand implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender p, Command command, java.lang.String s, java.lang.String[] strings) {
        if(!p.hasPermission("playerperks.globalfly")) {
            p.sendMessage(Main.formatMsg("NO_PERMISSION"));
            return true;
        }
        if(strings.length != 1) {
            p.sendMessage(Main.colourize("&cUsage: /globalfly <on/off>"));
            return true;
        }
        String arg = strings[0];
        switch (arg.toLowerCase()) {
            case "on":
                if(Main.getGFly()) {
                    p.sendMessage(Main.formatMsg("GFLY_ALREADY_ENABLED"));
                    return true;
                }
                Main.setGFly(true);
                p.sendMessage(Main.formatMsg("GFLY_ENABLED"));
                return true;
            case "off":
                if(Main.getGFly() == false) {
                    p.sendMessage(Main.formatMsg("GFLY_ALREADY_DISABLED"));
                    return true;
                }
                Main.setGFly(false);
                p.sendMessage(Main.formatMsg("GFLY_DISABLED"));
                return true;
            default:
                p.sendMessage(Main.colourize("&cUsage: /globalfly <on/off>"));
                return true;

        }
    }
    public void disableFly() {
        final int[] count = {10};
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (count[0] == 0) {
                    // Starting
                    Bukkit.broadcastMessage(Main.formatMsg("GFLY_BC_DISABLED"));
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        if((!p.hasPermission("playerperks.fly") && (p.isFlying()))) {
                            p.setFlying(false);
                        }
                    }
                    cancel();

                } else {
                    Bukkit.broadcastMessage(Main.colourize("&7Disabling fly in &b"+count[0] + " &7seconds."));
                    count[0]--;
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("PlayerPerks")), 20L, 20L);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if((Main.getGFly()) && (!p.hasPermission("playerperks.fly") && (p.isFlying()))) {
            p.setFlying(false);
        }
    }
}
