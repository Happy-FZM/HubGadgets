package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.MathUtils;
import me.Jaaakee224.HubGadgets.util.ParticleEffect;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

public class FlyingCarpetAction implements Gadget.GadgetAction
{
    private final Map<Player, FallingBlock> carpets;
    private final Map<Player, List<FallingBlock>> entities;
    
    public FlyingCarpetAction() {
        super();
        this.carpets = new HashMap<Player, FallingBlock>();
        this.entities = new HashMap<Player, List<FallingBlock>>();
    }
    
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT && trigger != Gadget.TriggerAction.ENTITY_CHANGE_BLOCK && trigger != Gadget.TriggerAction.VEHICLE_EXIT) {
            return false;
        }
        if (trigger == Gadget.TriggerAction.ENTITY_CHANGE_BLOCK) {
            final EntityChangeBlockEvent changeBlock = (EntityChangeBlockEvent)event;
            if (changeBlock.getEntity() instanceof FallingBlock) {
                final FallingBlock falling = (FallingBlock)changeBlock.getEntity();
                if (falling.getMaterial() == Material.CARPET) {
                    for (final Map.Entry<Player, List<FallingBlock>> entry : this.entities.entrySet()) {
                        if (entry.getValue().contains(falling)) {
                            gadget.finish(entry.getKey());
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        if (trigger == Gadget.TriggerAction.INTERACT) {
            final Player player = ((PlayerEvent)event).getPlayer();
            final World world = player.getWorld();
            final Block block = player.getTargetBlock((HashSet)null, 4);
            final Location location = block.getLocation().add(0.0, 10.0, 0.0);
            final List<FallingBlock> entities = new ArrayList<FallingBlock>();
            final byte data = (byte)MathUtils.random.nextInt(15);
            for (int i = 0; i < 9; ++i) {
                if (i % 3 == 0) {
                    location.add(1.0, 0.0, -2.0);
                }
                else {
                    location.add(0.0, 0.0, 1.0);
                }
                final FallingBlock falling2 = world.spawnFallingBlock(location, Material.CARPET, data);
                falling2.setDropItem(false);
                falling2.setVelocity(new Vector(0, 0, 0));
                if (i == 1) {
                    falling2.setPassenger(player);
                    this.carpets.put(player, falling2);
                }
                entities.add(falling2);
            }
            this.entities.put(player, entities);
        }
        return true;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        final List<FallingBlock> entities = this.entities.remove(player);
        if (entities != null) {
            for (final FallingBlock entity : entities) {
                if (!entity.isDead()) {
                    entity.remove();
                }
            }
        }
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        if (ticks % 5L == 0L) {
            final FallingBlock carpet = this.carpets.get(player);
            final List<FallingBlock> entities = this.entities.get(player);
            if (carpet == null || carpet.isDead() || carpet.isOnGround() || entities == null) {
                return true;
            }
            if (carpet.getPassenger() != null) {
                final Location location = player.getLocation();
                final Vector direction = location.getDirection();
                for (final FallingBlock entity : entities) {
                    if (entity.isDead() || entity.isOnGround()) {
                        return true;
                    }
                    if (carpet != entity && entity.getPassenger() == null) {
                        for (final Entity nearby : entity.getNearbyEntities(2.0, 1.0, 2.0)) {
                            if (nearby instanceof Player && player != nearby) {
                                if (nearby.getVehicle() != null) {
                                    continue;
                                }
                                entity.setPassenger(nearby);
                                break;
                            }
                        }
                    }
                    entity.setVelocity(direction);
                    ParticleEffect.ENCHANTMENT_TABLE.display(entity.getLocation(), 0.5f, 0.5f, 0.5f, 0.0f, 3);
                }
                carpet.setVelocity(direction);
            }
        }
        return false;
    }
}