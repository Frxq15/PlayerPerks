package me.Frxq15.PlayerPerks;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import me.Frxq15.PlayerPerks.Commands.*;
import me.Frxq15.PlayerPerks.Listeners.MenuListeners;
import me.Frxq15.PlayerPerks.Listeners.VanishListener;
import me.Frxq15.PlayerPerks.PerkManager.PerkListeners;
import me.Frxq15.PlayerPerks.PerkManager.ParticleManager;
import me.Frxq15.PlayerPerks.SQLManager.DataHandler;
import me.Frxq15.PlayerPerks.SQLManager.PlayerData;
import me.Frxq15.PlayerPerks.SQLManager.SQLSetterGetter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Main extends JavaPlugin {
    private SQLSetterGetter sqlHelper;
    private static Main instance;
    private Connection connection;
    public String host, database, username, password, table;
    public int port;
    private static Economy econ = null;
    public static boolean gfly;


    public void onEnable() {
        instance = this;
        sqlHelper = new SQLSetterGetter();
        saveDefaultConfig();
        setupEconomy();
        SQLSetup();
        SQLSetterGetter.createTable(table);
        getCommand("perks").setExecutor(new PerksCommand());
        getCommand("globalfly").setExecutor(new globalFlyCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("setafkmessage").setExecutor(new setAFKmessageCommand());
        getCommand("clearafkmessage").setExecutor(new clearAFKmessageCommand());
        Bukkit.getServer().getPluginManager().registerEvents(new globalFlyCommand(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new VanishListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new DataHandler(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new MenuListeners(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PerkListeners(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, (Runnable)new ParticleManager(), 0L, (long)1);
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[PlayerPerks] Plugin enabled.");
        try {
            startSavingTask();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void onDisable() {
        PlayerData.getAllPlayerData().forEach((uuid, playerData) -> sqlHelper.setAFKMessage(uuid, playerData.getAFKMessage()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[PlayerPerks] Plugin disabled.");
    }
    public static Main getInstance() { return instance; }
    public static String formatMsg(String input) {  return ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString(input)); }
    public static String colourize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
    public static List<String> colourize(List<String> input) {
        List<String> newList = new ArrayList<>();
        for(String entry : input) {
            newList.add(colourize(entry));
        }
        return newList;
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[PlayerPerks] Vault dependency not found, plugin disabling.");
            getServer().getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("PlayerPerks"));
            return true;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public static Economy getEconomy() {
        return econ;
    }
    public User getEssentialsPlayer(Player p) {
        Essentials ess = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        return ess.getUser(p);
    }
    public static FPlayer getFPlayer(Player p) {
        return FPlayers.getInstance().getByPlayer(p);
    }
    public void SQLSetup() {
        host = Main.getInstance().getConfig().getString("DATABASE." + "HOST");
        port = Main.getInstance().getConfig().getInt("DATABASE." + "PORT");
        database = Main.getInstance().getConfig().getString("DATABASE." + "DATABASE");
        username = Main.getInstance().getConfig().getString("DATABASE." + "USERNAME");
        password = Main.getInstance().getConfig().getString("DATABASE." + "PASSWORD");
        table = Main.getInstance().getConfig().getString("DATABASE." + "TABLE");

        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password="
                        + password));
                Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[PlayerPerks] MySQL Connected successfully.");

            }

        }catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[PlayerPerks] Please setup your MySQL database in the config.yml.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
    public void checkConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            SQLSetup();
        }
    }
    private void startSavingTask() throws SQLException {
        checkConnection();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> PlayerData.getAllPlayerData().forEach((uuid, playerData) -> sqlHelper.setAFKMessage(uuid, playerData.getAFKMessage())), 20L * 60L * 5L, 20L * 60L * 5L);
    }
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public SQLSetterGetter getSqlHelper() {
        return sqlHelper;
    }
    public static void setGFly(boolean status) {
        gfly = status;
    }
    public static boolean getGFly() {
        return gfly;
    }
}
