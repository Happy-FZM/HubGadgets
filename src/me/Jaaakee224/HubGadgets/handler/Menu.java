package me.Jaaakee224.HubGadgets.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.HubGadgetsPlugin;
import me.Jaaakee224.HubGadgets.config.Translation;
import me.Jaaakee224.HubGadgets.util.ItemBuilder;
import me.Jaaakee224.HubGadgets.util.MathUtils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Menu implements Listener {
    private final String title;
    private final List<MenuPage> pages;
    private final ItemStack[] buttons;
    private final MenuAction action;
    
    public Menu(final String title, final ItemStack[] buttons, final MenuAction action) {
        super();
        this.title = title;
        this.buttons = buttons;
        this.pages = new ArrayList<MenuPage>();
        this.action = action;
        int current = 0;
        for (int i = 0, maxPages = MathUtils.ceil(buttons.length / 21) + 1; i < maxPages; ++i) {
            final int id = i + 1;
            final Map<Integer, ItemStack> indexes = this.getButtonsInRange(current, current + 21);
            if (id > 1 || id < maxPages) {
                if (id > 1) {
                    indexes.put(39, new ItemBuilder(Material.ARROW).setTitle(StringUtils.join(Translation.getTranslation("inventory.prev"), ", ").replace("[page]", new StringBuilder(String.valueOf(id - 1)).toString())).build());
                }
                indexes.put(40, new ItemBuilder(Material.GLASS).setTitle(StringUtils.join(Translation.getTranslation("inventory.clear"), ", ")).build());
                if (id < maxPages) {
                    indexes.put(41, new ItemBuilder(Material.ARROW).setTitle(StringUtils.join(Translation.getTranslation("inventory.next"), ", ").replace("[page]", new StringBuilder(String.valueOf(id + 1)).toString())).build());
                }
            }
            this.pages.add(new MenuPage(id, title, indexes));
            current += 21;
        }
        Bukkit.getPluginManager().registerEvents(this, HubGadgetsPlugin.i);
    }
    
    public Map<Integer, ItemStack> getButtonsInRange(final int min, final int max) {
        final Map<Integer, ItemStack> indexes = new HashMap<Integer, ItemStack>();
        final ItemStack[] buttons = Arrays.copyOfRange(this.buttons, min, max);
        int itemSlot = 10;
        int currentLine = 2;
        ItemStack[] array;
        for (int length = (array = buttons).length, i = 0; i < length; ++i) {
            final ItemStack button = array[i];
            if (itemSlot == currentLine * 9 - 1) {
                itemSlot += 2;
                ++currentLine;
            }
            indexes.put(itemSlot, button);
            ++itemSlot;
        }
        return indexes;
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        final String title = inventory.getTitle();
        if (title.equals(this.title)) {
            event.setCancelled(true);
            if (event.getSlot() == event.getRawSlot() && event.getWhoClicked() instanceof Player) {
                final Player player = (Player)event.getWhoClicked();
                for (final MenuPage page : this.pages) {
                    if (page.isSame(inventory)) {
                        this.action.onInventoryClick(player, event.getCurrentItem(), event.getSlot(), this, page);
                    }
                }
            }
        }
    }
    
    public void open(final Player player, final int pageId) {
        for (final MenuPage page : this.pages) {
            if (page.id == pageId) {
                page.open(player);
            }
        }
    }
    
    public static class MenuPage {
        private final int id;
        private final String title;
        private final Map<Integer, ItemStack> buttons;
        private ItemStack[] contents;
        
        public MenuPage(final int id, final String title, final Map<Integer, ItemStack> buttons) {
            super();
            this.id = id;
            this.title = title;
            this.buttons = buttons;
        }
        
        public void open(final Player player) {
            final Inventory inventory = Bukkit.createInventory(player, 54, this.title);
            for (final Map.Entry<Integer, ItemStack> entry : this.buttons.entrySet()) {
                inventory.setItem(entry.getKey(), entry.getValue());
            }
            player.openInventory(inventory);
        }
        
        public boolean isSame(final Inventory inventory) {
            for (int i = 0; i < 54; ++i) {
                final ItemStack item = inventory.getContents()[i];
                if (item != null && !item.isSimilar(this.buttons.get(i))) {
                    return false;
                }
            }
            return true;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getTitle() {
            return this.title;
        }
        
        public Map<Integer, ItemStack> getButtons() {
            return this.buttons;
        }
        
        public ItemStack[] getContents() {
            return this.contents;
        }
        
        public void setContents(final ItemStack[] contents) {
            this.contents = contents;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof MenuPage)) {
                return false;
            }
            final MenuPage other = (MenuPage)o;
            if (!other.canEqual(this)) {
                return false;
            }
            if (this.getId() != other.getId()) {
                return false;
            }
            final Object this$title = this.getTitle();
            final Object other$title = other.getTitle();
            
            Label_0078: {
                if (this.title == null) {
                    if (other.title == null) {
                        break Label_0078;
                    }
                }
                else if (this$title.equals(other$title)) {
                    break Label_0078;
                }
                return false;
            }
            
            final Object this$buttons = this.getButtons();
            final Object other$buttons = other.getButtons();
            if (this$buttons == null) {
                if (other$buttons == null) {
                    return Arrays.deepEquals(this.getContents(), other.getContents());
                }
            }
            else if (this$buttons.equals(other$buttons)) {
                return Arrays.deepEquals(this.getContents(), other.getContents());
            }
            return false;
        }
        
        protected boolean canEqual(final Object other) {
            return other instanceof MenuPage;
        }
        
        @Override
        public int hashCode() {
            @SuppressWarnings("unused")
			final int PRIME = 59;
            int result = 1;
            result = result * 59 + this.getId();
            final Object $title = this.getTitle();
            result = result * 59 + (($title == null) ? 0 : $title.hashCode());
            final Object $buttons = this.getButtons();
            result = result * 59 + (($buttons == null) ? 0 : $buttons.hashCode());
            result = result * 59 + Arrays.deepHashCode(this.getContents());
            return result;
        }
        
        @Override
        public String toString() {
            return "Menu.MenuPage(id=" + this.getId() + ", title=" + this.getTitle() + ", buttons=" + this.getButtons() + ", contents=" + Arrays.deepToString(this.getContents()) + ")";
        }
    }
    
    public interface MenuAction
    {
        void onInventoryClick(Player p0, ItemStack p1, int p2, Menu p3, MenuPage p4);
    }
}