package me.Frxq15.PlayerPerks.PerkManager;

import com.massivecraft.factions.FPlayer;
import me.Frxq15.PlayerPerks.Main;
import me.Frxq15.PlayerPerks.SQLManager.PlayerData;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.modifier.PriceModifierActionType;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataType;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.matcher.NodeMatcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Manager {

    public static void setSellMultiplier(Player p, double modifier) {
        ShopGuiPlusApi.setPriceModifier(p, PriceModifierActionType.SELL, modifier);
    }
    public static void setBuyMultiplier(Player p, double modifier) {
        ShopGuiPlusApi.setPriceModifier(p, PriceModifierActionType.BUY, modifier);
    }
    public static void powerBoost(Player p, double boost) {
        FPlayer player = Main.getFPlayer(p);
        player.setPowerBoost(boost);
    }
    public static String getAFKMessage(Player p) {
        PlayerData playerData = PlayerData.getPlayerData(Main.getInstance(), p.getUniqueId());
        return Main.colourize(AFKPrefix(p.getName()) + playerData.getAFKMessage());
    }
    public static boolean isVanished(Player p) {
        if(Main.getInstance().getEssentialsPlayer(p).isVanished()) {
            return true;
        }
        return false;
    }
    public static String AFKPrefix(String p) {
        return "&f[&bFactions&f] &7(AFK) &b"+p+ ": &f";
    }
    public static String getPlayerMeta(Player p) {
        return p.getDisplayName();
    }
    public static int randomNumber(int min, int max) {
        Random r = new Random();
        int low = min;
        int high = max;
        int result = r.nextInt(high - low) + low;
        return result;
    }
}
