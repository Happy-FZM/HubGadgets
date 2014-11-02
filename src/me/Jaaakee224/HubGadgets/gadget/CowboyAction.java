package me.Jaaakee224.HubGadgets.gadget;

import me.Jaaakee224.HubGadgets.handler.Gadget;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class CowboyAction implements Gadget.GadgetAction
{
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT_ENTITY) {
            return false;
        }
        final PlayerInteractEntityEvent interactEvent = (PlayerInteractEntityEvent)event;
        final Player player = interactEvent.getPlayer();
        final Entity entity = interactEvent.getRightClicked();
        if (entity.getPassenger() == null) {
            entity.setPassenger(player);
        }
        return true;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        final Entity vehicle = player.getVehicle();
        if (vehicle != null && vehicle.getPassenger() == player) {
            vehicle.eject();
            
        }
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        return ticks % 20L == 0L && player.getVehicle() == null;
    }
}