package me.Jaaakee224.HubGadgets.gadget;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import me.Jaaakee224.HubGadgets.config.Translation;
import me.Jaaakee224.HubGadgets.handler.Gadget;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class PigsFlyAction implements Gadget.GadgetAction
{
    private final Map<Player, Pig> entities;
    
    public PigsFlyAction() {
        super();
        this.entities = new HashMap<Player, Pig>();
    }
    
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.ENTITY_DAMAGE && trigger != Gadget.TriggerAction.INTERACT && trigger != Gadget.TriggerAction.VEHICLE_EXIT) {
            return false;
        }
        if (trigger == Gadget.TriggerAction.ENTITY_DAMAGE) {
            final Entity entity = ((EntityDamageEvent)event).getEntity();
            if (entity instanceof Pig && this.entities.values().contains(entity)) {
                entity.remove();
                return true;
            }
            return false;
        }
        else {
            if (trigger == Gadget.TriggerAction.VEHICLE_EXIT) {
                final VehicleExitEvent exitEvent = (VehicleExitEvent)event;
                final Player player = (Player)exitEvent.getExited();
                final Entity vehicle = exitEvent.getVehicle();
                if (vehicle instanceof Pig) {
                    final Entity pig = this.entities.get(player);
                    if (pig != null && pig == vehicle) {
                        gadget.finish(player);
                        return true;
                    }
                }
                return false;
            }
            if (trigger == Gadget.TriggerAction.INTERACT) {
                final Player player2 = ((PlayerEvent)event).getPlayer();
                final Block block = player2.getTargetBlock((HashSet)null, 4);
                final Location location = block.getLocation().add(0.0, 10.0, 0.0);
                if (!Gadget.checkEmptyBlocks(location, 2)) {
                    player2.sendMessage(Translation.getTranslation("target_clean_area"));
                    return false;
                }
                final Pig pig2 = (Pig)location.getWorld().spawn(location.add(0.0, 1.0, 0.0), (Class)Pig.class);
                pig2.setNoDamageTicks(Integer.MAX_VALUE);
                pig2.setPassenger(player2);
                this.entities.put(player2, pig2);
            }
            return true;
        }
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        final Entity entity = this.entities.remove(player);
        if (entity != null && !entity.isDead()) {
            entity.remove();
        }
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        if (ticks % 5L == 0L) {
            final Pig pig = this.entities.get(player);
            if (pig == null || pig.isDead()) {
                return true;
            }
            if (pig.getPassenger() != null) {
                pig.setVelocity(player.getLocation().getDirection());
            }
        }
        return false;
    }
}