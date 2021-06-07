package me.Frxq15.PlayerPerks.SQLManager;

import me.Frxq15.PlayerPerks.Main;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private final static Map<UUID, PlayerData> players = new HashMap<>();

    private final UUID uuid;
    private String afkmsg = "";

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        players.put(uuid, this);
    }
    public UUID getUuid() { return uuid; }
    public void setAFKMessage(String msg) {
        this.afkmsg = msg;
    }
    public String getAFKMessage() { return afkmsg; }
    public static void removePlayerData(UUID uuid) { players.remove(uuid); }

    public void uploadPlayerData(Main Main) {
        Bukkit.getScheduler().runTaskAsynchronously(Main, () -> {
                Main.getSqlHelper().setAFKMessage(uuid, afkmsg);
        });
    }

    public static PlayerData getPlayerData(Main Main, UUID uuid) {
        if (!players.containsKey(uuid)) {
            PlayerData playerData = new PlayerData(uuid);
            playerData.setAFKMessage(Main.getSqlHelper().getAFKMessage(uuid));
        }
        return players.get(uuid);
    }

    public static Map<UUID, PlayerData> getAllPlayerData() {
        return players;
    }

}
