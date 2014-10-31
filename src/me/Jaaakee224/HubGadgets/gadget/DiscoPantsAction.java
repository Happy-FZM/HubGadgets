package me.Jaaakee224.HubGadgets.gadget;

import java.util.HashMap;
import java.util.Map;

import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.ItemBuilder;
import me.Jaaakee224.HubGadgets.util.MathUtils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class DiscoPantsAction implements Gadget.GadgetAction
{
    private static final String TITLE;
    private final Map<Player, ItemStack> leggings;
    
    static {
        TITLE = ChatColor.GREEN + "Disco Pants";
    }
    
    public DiscoPantsAction() {
        super();
        this.leggings = new HashMap<Player, ItemStack>();
    }
    
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT && trigger != Gadget.TriggerAction.INVENTORY_DROP && trigger != Gadget.TriggerAction.INVENTORY_CLICK) {
            return false;
        }
        if (trigger == Gadget.TriggerAction.INTERACT) {
            final Player player = ((PlayerEvent)event).getPlayer();
            this.leggings.put(player, player.getInventory().getLeggings());
        }
        else if (trigger == Gadget.TriggerAction.INVENTORY_DROP) {
            final PlayerDropItemEvent dropEvent = (PlayerDropItemEvent)event;
            final ItemStack item = dropEvent.getItemDrop().getItemStack();
            if (!item.hasItemMeta() || !item.getItemMeta().getDisplayName().equals(DiscoPantsAction.TITLE)) {
                return false;
            }
        }
        else if (trigger == Gadget.TriggerAction.INVENTORY_CLICK) {
            final InventoryClickEvent clickEvent = (InventoryClickEvent)event;
            final ItemStack item = clickEvent.getCurrentItem();
            if (item == null || !item.hasItemMeta() || !item.getItemMeta().getDisplayName().equals(DiscoPantsAction.TITLE)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        player.getInventory().setLeggings(this.leggings.remove(player));
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        if (ticks % 5L == 0L) {
            final Color color = Color.fromRGB(MathUtils.random.nextInt(255), MathUtils.random.nextInt(255), MathUtils.random.nextInt(255));
            final ItemStack leggings = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherColor(color).setTitle(DiscoPantsAction.TITLE).build();
            player.getInventory().setLeggings(leggings);
        }
        return false;
    }
}