package me.Jaaakee224.HubGadgets.config;

import java.io.File;
import java.io.IOException;

import me.Jaaakee224.HubGadgets.HubGadgetsPlugin;
import me.Jaaakee224.HubGadgets.util.ItemBuilder;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Options {
    private static Item item;
    private static Item gadgets;
    private static String translation;
    
    public static void load(final File file) {
        final FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.options().copyDefaults(true);
        config.addDefault("item.enable", true);
        config.addDefault("item.id", 33);
        config.addDefault("item.data", 0);
        config.addDefault("item.slot", 8);
        config.addDefault("gadgets.slot", 4);
        config.addDefault("gadgets.slot", 4);
        config.addDefault("translation", "en");
        try {
            config.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        Options.gadgets = new Item(config.getInt("gadgets.slot"), true, null);
        Options.translation = config.getString("translation");
        
        Translation.load(new File(HubGadgetsPlugin.i.getDataFolder(), "translations_" + getTranslation() + ".yml"));
        @SuppressWarnings("deprecation")
		final ItemBuilder builder = new ItemBuilder(Material.getMaterial(config.getInt("item.id")), 1, (short)config.getInt("item.data")).setTitle(ChatColor.translateAlternateColorCodes('&', StringUtils.join(Translation.getTranslation("item.title"), ", ")));
        String[] translation;
        for (int length = (translation = Translation.getTranslation("item.description")).length, i = 0; i < length; ++i) {
            final String lore = translation[i];
            builder.addLore(ChatColor.translateAlternateColorCodes('&', lore));
        }
        Options.item = new Item(config.getInt("item.slot"), config.getBoolean("item.enable"), builder.build());
    }
    
    public static Item getItem() {
        return Options.item;
    }
    
    public static Item getGadgets() {
        return Options.gadgets;
    }
    
    public static String getTranslation() {
        return Options.translation;
    }
    
    public static class Item {
        private int slot;
        private boolean enable;
        private ItemStack itemStack;
        
        public int getSlot() {
            return this.slot;
        }
        
        public boolean isEnable() {
            return this.enable;
        }
        
        public ItemStack getItemStack() {
            return this.itemStack;
        }
        
        public void setSlot(final int slot) {
            this.slot = slot;
        }
        
        public void setEnable(final boolean enable) {
            this.enable = enable;
        }
        
        public void setItemStack(final ItemStack itemStack) {
            this.itemStack = itemStack;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Item)) {
                return false;
            }
            final Item other = (Item)o;
            if (!other.canEqual(this)) {
                return false;
            }
            if (this.getSlot() != other.getSlot()) {
                return false;
            }
            if (this.isEnable() != other.isEnable()) {
                return false;
            }
            final Object this$itemStack = this.getItemStack();
            final Object other$itemStack = other.getItemStack();
            if (this$itemStack == null) {
                if (other$itemStack == null) {
                    return true;
                }
            }
            else if (this$itemStack.equals(other$itemStack)) {
                return true;
            }
            return false;
        }
        
        protected boolean canEqual(final Object other) {
            return other instanceof Item;
        }
        
        @Override
        public int hashCode() {
            int result = 1;
            result = result * 59 + this.getSlot();
            result = result * 59 + (this.isEnable() ? 79 : 97);
            final Object $itemStack = this.getItemStack();
            result = result * 59 + (($itemStack == null) ? 0 : $itemStack.hashCode());
            return result;
        }
        
        @Override
        public String toString() {
            return "Options.Item(slot=" + this.getSlot() + ", enable=" + this.isEnable() + ", itemStack=" + this.getItemStack() + ")";
        }
        
        public Item(final int slot, final boolean enable, final ItemStack itemStack) {
            super();
            this.slot = slot;
            this.enable = enable;
            this.itemStack = itemStack;
        }
    }
}