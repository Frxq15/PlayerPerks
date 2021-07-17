package me.Frxq15.PlayerPerks.PerkManager;

import me.Frxq15.PlayerPerks.Main;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import com.massivecraft.factions.FPlayer;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.SQLException;
import java.util.Objects;

public class PerkListeners implements Listener {
    @EventHandler
    public void tntDispense(BlockDispenseEvent e) {
        if (e.getBlock().getType() == Material.DISPENSER) {
            if (e.getItem().getType() == Material.TNT) {
                for (World world : Bukkit.getWorlds())
                    for (Entity entity : Bukkit.getWorld(String.valueOf(world)).getEntities()) {
                        if (entity.getType() == EntityType.PRIMED_TNT) {
                            runParticles(entity, 60);
                        }
                    }
            }
        }
    }

    public void runParticles(Entity tnt, int seconds) {
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                int count = seconds * 20;
                if (count-- == 0) {
                    cancel();
                } else {
                    tnt.getWorld().playEffect(tnt.getLocation(), Effect.WITCH_MAGIC, 0);
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("PlayerPerks")), 20L, 20L);
    }
    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if(e.getEntityType() != EntityType.PLAYER) {
            return;
        }
        if (e.getEntity().hasPermission("playerperks.perk.FALL_DAMAGE") && (Math.random() * 100 <= 25)) {
            if(e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager() == null) { return; }
        if(e.getEntityType() != EntityType.PLAYER) {
            return;
        }
        if(e.getDamager().getType() != EntityType.PLAYER) {
            return;
        }
        if (e.getDamager().hasPermission("playerperks.perk.INCREASE_DAMAGE") && (Math.random() * 100 <= 5)) {
            e.setDamage(e.getDamage() * 1.2);
        }
    }
    @EventHandler
    public void onArmorDamage(PlayerItemDamageEvent e) {
        if (e.getPlayer().hasPermission("playerperks.perk.INCREASE_DURABILITY") && (Math.random() * 100 <= 3)) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void Sprinter(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() == null) { return; }
        if(e.getEntityType() != EntityType.PLAYER) { return; }
        if(e.getEntity().getKiller().getType() != EntityType.PLAYER) { return; }
        if (e.getEntity() != null && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();
            if (killer.hasPermission("playerperks.perk.SPRINTER") && (Math.random() * 100 <= 15)) {
                killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 2));
            }
        }
    }
    @EventHandler
    public void EXP_MASTER(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() == null) { return; }
        if(e.getEntityType() != EntityType.PLAYER) { return; }
        if(e.getEntity().getKiller().getType() != EntityType.PLAYER) { return; }
        if (e.getEntity() != null && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();
            if (killer.hasPermission("playerperks.perk.EXP_MASTER") && (Math.random() * 100 <= 20)) {
                killer.giveExp(Manager.randomNumber(750, 3500));
            }
        }
    }
}
