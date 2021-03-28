package me.Frxq15.PlayerPerks.GUIManager;

import me.Frxq15.PlayerPerks.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ConfirmGUI extends GUITemplate{
    private Player p;
    private String perk;
    private ConfirmedAction action;
    public ConfirmGUI(Player player, String perk, ConfirmedAction action) {
        super(1, Main.formatMsg("CONFIRMATION_GUI" + ".GUI_TITLE"));
        this.p = player;
        this.perk = perk;
        this.action = action;
        initializeItems();
    }
    public void initializeItems() {
        setItem(4, getPerk(), player -> {
            action.confirm();
            delete();
        });
        for (int slot = 0; slot < 4; slot++) {
            setItem(slot, Approve(), player -> {
                action.confirm();
                delete();
            });
        }
        for (int slot = 5; slot < 9; slot++) {
            setItem(slot, Decline(), player -> {
                p.getOpenInventory().close();
            });
        }
    }
    public ItemStack Approve() {
        List<String> lore = new ArrayList<String>();
        final ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        final ItemMeta meta = item.getItemMeta();
            lore.add(Main.colourize("&7Click to confirm this purchase"));

        meta.setDisplayName(Main.colourize("&a&nConfirm Purchase"));
        meta.setLore(Main.colourize(lore));
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack Decline() {
        List<String> lore = new ArrayList<String>();
        String material = Main.getInstance().getConfig().getString("PERKS" + "."+perk + ".ITEM");
        final ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        final ItemMeta meta = item.getItemMeta();
        lore.add(Main.colourize("&7Click to cancel this purchase"));

        meta.setDisplayName(Main.colourize("&c&nCancel Purchase"));
        meta.setLore(Main.colourize(lore));
        item.setItemMeta(meta);
        return item;
    }
    public String getClickStatus(String perk, Player p) {  return Main.getInstance().getConfig().getString("GUI" + ".LORE_OUTCOMES" + ".CLICK_TO_PURCHASE"); }
    public ItemStack getPerk() {
        List<String> lore = new ArrayList<String>();
        Integer cost = Main.getInstance().getConfig().getInt("PERKS" + "."+perk + ".COST");
        String material = Main.getInstance().getConfig().getString("PERKS" + "."+perk + ".ITEM");
        final ItemStack item = new ItemStack(Material.valueOf(material), 1);
        String name = Main.getInstance().getConfig().getString("PERKS" + "."+perk + ".NAME");
        final ItemMeta meta = item.getItemMeta();
        String fcost = String.format("%,d", cost);

        for(String lines : Main.getInstance().getConfig().getStringList("CONFIRMATION_GUI" + ".PERKS" + "."+perk + ".LORE")) {
            lore.add(Main.colourize(lines)
                    .replace("%duration%", "Permanent").replace("%cost%", "$"+fcost)
                    .replace("%lore_outcome%", getClickStatus(perk, p)));
        }
        meta.setDisplayName(Main.colourize(name));

        meta.setLore(Main.colourize(lore));
        item.setItemMeta(meta);
        return item;
    }
    public interface ConfirmedAction {
        void confirm();
    }
}
