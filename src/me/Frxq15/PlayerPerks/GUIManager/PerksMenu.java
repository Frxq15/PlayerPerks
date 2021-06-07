package me.Frxq15.PlayerPerks.GUIManager;
import me.Frxq15.PlayerPerks.PerkManager.Manager;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import me.Frxq15.PlayerPerks.Main;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PerksMenu extends GUITemplate {
    private Player p;

    public PerksMenu(Player player) {
        super(5, Main.formatMsg("GUI" + ".GUI_TITLE"));
        this.p = player;
        initializeItems(player);
    }
    public void initializeItems(Player p) {
        Main.getInstance().getConfig().getConfigurationSection("PERKS").getKeys(false).forEach(perk -> {
            initializeItem(perk);
        });
        for (int slot = 36; slot < 45; slot++) {
            setItem(slot, pane(15));
        }
        setItem(40, unlocks(p));
    }
    public void initializeItem(String perk) {
        OfflinePlayer op = p;
        double balance = Main.getEconomy().getBalance(p.getName());
        this.setItem(getPerkSlot(perk), getPerk(perk));
        setItem(getPerkSlot(perk), getPerk(perk), player -> {
            ConfirmGUI confirm = new ConfirmGUI(p, perk, () -> {
               purchasePerk(p, perk);
            });
            if (getPerkBool(perk, p)) {
                p.sendMessage(Main.formatMsg("PERK_ALREADY_UNLOCKED"));
                p.getOpenInventory().close();
                return;
            }
            if(balance < getPerkCost(perk)) {
                this.setItem(getPerkSlot(perk), PurchaseFailed(perk));
                p.sendMessage(Main.formatMsg("PURCHASE_FAILED"));
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    initializeItem(perk);
                    return;
                }, 20L * 1);
            }
            if(balance >= getPerkCost(perk)) {
                confirm.open(player);
            }
        });
    }
    public static ItemStack pane(int id) {
        final ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) id);
        return item;
    }
    protected ItemStack getPerk(String perk) {
        List<String> lore = new ArrayList<String>();
        Integer cost = Main.getInstance().getConfig().getInt("PERKS" + "."+perk + ".COST");
        String material = Main.getInstance().getConfig().getString("PERKS" + "."+perk + ".ITEM");
        final ItemStack item = new ItemStack(Material.valueOf(material), 1);
        String name = Main.getInstance().getConfig().getString("PERKS" + "."+perk + ".NAME");
        final ItemMeta meta = item.getItemMeta();
        String fcost = String.format("%,d", cost);
        for(String lines : Main.getInstance().getConfig().getStringList("PERKS" + "."+perk + ".LORE")) {
            lore.add(Main.colourize(lines).replace("%status%", getUnlockedStatus(perk, p)).replace("%cost%", "$"+fcost)
                    .replace("%lore_outcome%", getClickStatus(perk, p)));
        }
        meta.setDisplayName(Main.colourize(name));
        if(getPerkBool(perk, p)) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setLore(Main.colourize(lore));
        item.setItemMeta(meta);
        return item;
    }
    public Integer getPerkSlot(String perk) {
        return Main.getInstance().getConfig().getInt("PERKS" + "."+perk + ".SLOT");
    }
    public String getUnlockedStatus(String perk, Player p) {
        boolean status = p.hasPermission("playerperks.perk."+perk);
        if(status) {
            return Main.getInstance().getConfig().getString("GUI" + ".LORE_OUTCOMES" + ".STATUS_UNLOCKED");
        }
        return Main.getInstance().getConfig().getString("GUI" + ".LORE_OUTCOMES" + ".STATUS_LOCKED");
    }
    public String getClickStatus(String perk, Player p) {
        OfflinePlayer op = p;
        double balance = Main.getEconomy().getBalance(p.getName());
        boolean status = p.hasPermission("playerperks.perk."+perk);
        if(Main.getInstance().getConfig().getBoolean("PERKS." +perk + ".ENABLED") == false) {
            return Main.getInstance().getConfig().getString("GUI" + ".LORE_OUTCOMES" + ".CURRENTLY_DISABLED");
        }
        if(status) {
            return Main.getInstance().getConfig().getString("GUI" + ".LORE_OUTCOMES" + ".ALREADY_UNLOCKED");
        }
        if(balance < getPerkCost(perk)) {
            return Main.getInstance().getConfig().getString("GUI" + ".LORE_OUTCOMES" + ".CANT_AFFORD");
        }
        return Main.getInstance().getConfig().getString("GUI" + ".LORE_OUTCOMES" + ".CLICK_TO_PURCHASE");
    }
    public Boolean getPerkBool(String perk, Player p) {
        boolean status = p.hasPermission("playerperks.perk."+perk);
        if(status) {
            return true;
        }
        return false;
    }
    public double getPerkCost(String perk) {
        return Main.getInstance().getConfig().getInt("PERKS" + "."+perk + ".COST");
    }
    protected ItemStack PurchaseFailed(String perk) {
        List<String> lore = new ArrayList<String>();
        Material material = Material.BARRIER;
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        lore.add(Main.colourize("&7You cannot afford to purchase this perk."));
        meta.setDisplayName(Main.colourize("&c&nPurchase Failed"));
        meta.setLore(Main.colourize(lore));
        item.setItemMeta(meta);
        return item;
    }
    public void addPerk(Player p, String perk) {
        User user = LuckPermsProvider.get().getUserManager().getUser(p.getName());
        user.data().add(Node.builder("playerperks.perk."+perk).build());
        LuckPermsProvider.get().getUserManager().saveUser(user);

        if(perk.equalsIgnoreCase("SELL_MULTIPLIER")) {
            Manager.setSellMultiplier(p, 1.1);
            return;
        }
        if(perk.equalsIgnoreCase("PURCHASE_DISCOUNT")) {
            Manager.setBuyMultiplier(p, 0.95);
            return;
        }
        if(perk.equalsIgnoreCase("POWER_BOOST")) {
            Manager.powerBoost(p, 50);
            return;
        }
        if(perk.equalsIgnoreCase("CUSTOM_AFK_MESSAGE")) {
            user.data().add(Node.builder("playerperks.command.setafkmessage").build());
            return;
        }

    }
    public void purchasePerk(Player p, String perk) {
        OfflinePlayer op = p;
        Integer cost = Main.getInstance().getConfig().getInt("PERKS" + "."+perk + ".COST");
        p.sendMessage(Main.formatMsg("PERK_PURCHASED").replace("%perk%", perk));
        addPerk(p, perk);
        Main.getEconomy().withdrawPlayer(op, cost);
        delete();
    }
    public static ItemStack unlocks(Player p) {
        AtomicInteger unlocked = new AtomicInteger(0);
        AtomicInteger locked = new AtomicInteger(0);
        Main.getInstance().getConfig().getConfigurationSection("PERKS").getKeys(false).forEach(perk -> {
            if(p.hasPermission("playerperks.perk."+perk)) {
                unlocked.incrementAndGet();
            }
        });
        Main.getInstance().getConfig().getConfigurationSection("PERKS").getKeys(false).forEach(perk -> {
            if(!p.hasPermission("playerperks.perk."+perk)) {
                locked.incrementAndGet();
            }
        });
        List<String> lore = new ArrayList<String>();
        ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName(Main.colourize("&b&nPerk Unlocks"));
        lore.add("&7Listed below is the status of all of your perks.");
        lore.add("");
        lore.add("&aUnlocked:");
        Main.getInstance().getConfig().getConfigurationSection("PERKS").getKeys(false).forEach(perk -> {
            if(p.hasPermission("playerperks.perk."+perk)) {
                lore.add("&a&l • &7"+perk);
            }
        });
        if(unlocked.get() == 0) {
            lore.add("&a&l • &7None");
        }
        lore.add("");
        lore.add("&cLocked:");
        Main.getInstance().getConfig().getConfigurationSection("PERKS").getKeys(false).forEach(perk -> {
            if(!p.hasPermission("playerperks.perk."+perk)) {
                lore.add("&c&l • &7"+perk);
            }
        });
        if(locked.get() == 0) {
            lore.add("&c&l • &7None");
        }
        lore.add("");
        lore.add("&7Unlock perks by purchasing them through this menu.");
        meta.setLore(Main.colourize(lore));
        book.setItemMeta(meta);


        return book;
    }
}
