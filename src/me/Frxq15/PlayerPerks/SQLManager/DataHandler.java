package me.Frxq15.PlayerPerks.SQLManager;

import me.Frxq15.PlayerPerks.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.UUID;

public class DataHandler implements Listener {

    private final Main plugin;

    public DataHandler(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) throws SQLException, ClassNotFoundException {
        UUID uuid = event.getUniqueId();
        String name = event.getName();

        Main.getInstance().checkConnection();
        plugin.getSqlHelper().createPlayer(uuid, name);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSqlHelper().updatePlayerName(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                UUID uuid = event.getPlayer().getUniqueId();

                PlayerData playerData = PlayerData.getPlayerData(plugin, uuid);
                    plugin.getSqlHelper().setAFKMessage(uuid, playerData.getAFKMessage());
                    PlayerData.removePlayerData(uuid);
            });
    }
}
