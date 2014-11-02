package me.Jaaakee224.HubGadgets.menu;

import me.Jaaakee224.HubGadgets.config.Options;
import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.handler.Menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GadgetsMenuAction implements Menu.MenuAction {
    @Override
    public void onInventoryClick(final Player player, final ItemStack item, final int slot, final Menu menu, final Menu.MenuPage page) {
        if (item != null && item.getType() != Material.AIR) {
            if (item.getType() == Material.ARROW) {
                menu.open(player, (slot == 41) ? (page.getId() + 1) : (page.getId() - 1));
            }
            else {
                if (item.getType() == Material.GLASS) {
                    final Gadget gadget = Gadget.getPlayerGadget(player);
                    if (gadget != null) {
                        gadget.finish(player);
                    }
                    player.getInventory().setItem(Options.getGadgets().getSlot(), (ItemStack)null);
                }
                else {
                    final Gadget[] values;
                    final int length = (values = Gadget.values()).length;
                    int i = 0;
                    while (i < length) {
                        final Gadget gadget = values[i];
                        if (gadget.isEnable() && gadget.getItem().isSimilar(item)) {
                            if (gadget.check(player, false)) {
                                player.getInventory().setItem(Options.getGadgets().getSlot(), item);
                                break;
                            }
                            break;
                        }
                        else {
                            ++i;
                        }
                    }
                }
                player.closeInventory();
            }
        }
    }
}