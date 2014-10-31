package me.Jaaakee224.HubGadgets.event.player;

import me.Jaaakee224.HubGadgets.config.Options;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDropItem implements Listener
{
    @EventHandler
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        final ItemStack current = event.getItemDrop().getItemStack();
        if (current.hasItemMeta() && current.isSimilar(Options.getItem().getItemStack())) {
            event.getItemDrop().remove();
            event.getPlayer().getInventory().setItem(Options.getItem().getSlot(), Options.getItem().getItemStack());
        }
    }
}