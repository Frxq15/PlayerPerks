package me.Frxq15.PlayerPerks.Listeners;

import me.Frxq15.PlayerPerks.Main;
import net.ess3.api.events.AfkStatusChangeEvent;
import net.ess3.api.events.VanishStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class VanishListener implements Listener {
    public static List<String> vanished = new ArrayList<String>();

    @EventHandler
    public void onVanish(VanishStatusChangeEvent e) {
        if(e.equals(true)) {
            vanished.add(e.getAffected().getName());
        }
        vanished.remove(e.getAffected().getName());
    }
}
