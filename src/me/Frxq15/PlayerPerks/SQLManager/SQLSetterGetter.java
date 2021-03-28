package me.Frxq15.PlayerPerks.SQLManager;

import me.Frxq15.PlayerPerks.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLSetterGetter {

    static Main plugin = Main.getPlugin(Main.class);

    public static boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void createPlayer(final UUID uuid, String name) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            if (!playerExists(uuid)) {
                PreparedStatement insert = plugin.getConnection()
                        .prepareStatement("INSERT INTO " + plugin.table + "(uuid,player,afkmsg) VALUES (?,?,?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, name);
                insert.setString(3, "&7is now afk.");
                insert.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerName(Player player) {
        try {
            PreparedStatement selectPlayer = plugin.getConnection().prepareStatement("SELECT * FROM `" + plugin.table + "` WHERE uuid = ?;");
            selectPlayer.setString(1, player.getUniqueId().toString());
            ResultSet playerResult = selectPlayer.executeQuery();

            if (playerResult.next() && !playerResult.getString("player").equals(player.getName())) {
                PreparedStatement updateName = plugin.getConnection().prepareStatement("UPDATE `"+plugin.table + "` SET player = ? WHERE uuid = ?;");
                updateName.setString(1, player.getName());
                updateName.setString(2, player.getUniqueId().toString());
                updateName.executeUpdate();
            }

            playerResult.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String table) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try {
                PreparedStatement statement = plugin.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + table + "` (uuid VARCHAR(36) PRIMARY KEY, player VARCHAR(16), afkmsg VARCHAR(36));");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void setAFKMessage(UUID uuid, String message) {
        if(!playerExists(uuid)) {
            return;
        }
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE " + plugin.table + " SET afkmsg=? WHERE UUID=?");
            statement.setString(1, message);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getAFKMessage(UUID uuid) {
        if(!playerExists(uuid)) {
            return "&7is now afk.";
        }

        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getString("afkmsg");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return " &7is now afk.";
    }
    public void deleteTable() {
        try {
            plugin.getConnection().prepareStatement("DROP TABLE IF EXISTS " + plugin.table).executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
