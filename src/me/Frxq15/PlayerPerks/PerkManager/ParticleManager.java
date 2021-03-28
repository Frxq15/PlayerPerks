package me.Frxq15.PlayerPerks.PerkManager;

import me.Frxq15.PlayerPerks.Commands.FlyCommand;
import me.Frxq15.PlayerPerks.Main;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

public class ParticleManager implements Runnable{
        @Override
        public void run() {
            for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (FlyCommand.flying.contains(p.getName()) && p.isFlying() && (!Manager.isVanished(p)) && p.hasPermission("playerperks.perk.FLY_PARTICLES")) {
                        final Location loc = p.getLocation();
                        p.getWorld().playEffect(loc, Effect.WITCH_MAGIC, 0);
                }
            }
        }
}
