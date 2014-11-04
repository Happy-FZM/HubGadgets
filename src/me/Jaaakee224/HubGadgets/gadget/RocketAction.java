package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.ParticleEffect;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

public class RocketAction implements Gadget.GadgetAction {
	
    private final Map<Player, List<Entity>> entities;
    private final Map<Player, FallingBlock> rockets;
    
    public RocketAction() {
        super();
        this.entities = new HashMap<Player, List<Entity>>();
        this.rockets = new HashMap<Player, FallingBlock>();
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT && trigger != Gadget.TriggerAction.ENTITY_CHANGE_BLOCK) {
            return false;
        }
        if (trigger == Gadget.TriggerAction.ENTITY_CHANGE_BLOCK) {
            final EntityChangeBlockEvent changeBlock = (EntityChangeBlockEvent)event;
            if (changeBlock.getEntity() instanceof FallingBlock) {
                final FallingBlock falling = (FallingBlock)changeBlock.getEntity();
                if (falling.getMaterial() == Material.WOOD || falling.getMaterial() == Material.IRON_BLOCK) {
                    for (final List<Entity> entities : this.entities.values()) {
                        if (entities.contains(falling)) {
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
            final Location rocketLocation = player.getLocation().add(0.0, 2.0, 0.0);
            final List<Entity> entities2 = new ArrayList<Entity>();
            final FallingBlock rocket = this.addEntity(world.spawnFallingBlock(rocketLocation, Material.IRON_BLOCK, (byte)0), entities2);
            this.addEntity(world.spawnFallingBlock(rocketLocation.clone().subtract(0.0, 1.0, 0.0), Material.IRON_BLOCK, (byte)0), entities2);
            this.addEntity(world.spawnFallingBlock(rocketLocation.clone().add(1.0, -2.0, 0.0), Material.WOOD, (byte)0), entities2);
            this.addEntity(world.spawnFallingBlock(rocketLocation.clone().add(0.0, -2.0, 1.0), Material.WOOD, (byte)0), entities2);
            this.addEntity(world.spawnFallingBlock(rocketLocation.clone().add(-1.0, -2.0, 0.0), Material.WOOD, (byte)0), entities2);
            this.addEntity(world.spawnFallingBlock(rocketLocation.clone().add(0.0, -2.0, -1.0), Material.WOOD, (byte)0), entities2);
            player.teleport(rocketLocation.add(0.0, 1.0, 0.0));
            rocket.setPassenger(player);
            this.rockets.put(player, rocket);
            this.entities.put(player, entities2);
        }
        return true;
    }
    
    private FallingBlock addEntity(final FallingBlock entity, final List<Entity> entities) {
        entity.setDropItem(false);
        entity.setVelocity(new Vector(0, 1, 0));
        entities.add(entity);
        return entity;
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        if (ticks % 5L == 0L) {
            final FallingBlock rocket = this.rockets.get(player);
            final List<Entity> entities = this.entities.get(player);
            if (rocket == null || rocket.isDead() || entities == null || player.getVehicle() != rocket) {
                return true;
            }
            final Location location = rocket.getLocation().subtract(0.0, 2.0, 0.0);
            ParticleEffect.LAVA.display(location, 0.2f, 0.0f, 0.2f, 1.0f, 30);
            ParticleEffect.SMOKE.display(location, 0.2f, 0.0f, 0.2f, 1.0f, 30);
            for (final Entity entity : entities) {
                if (entity.isDead()) {
                    return true;
                }
                entity.setVelocity(new Vector(0, 1, 0));
            }
            if (ticks % 10L == 0L) {
                player.playSound(location, Sound.BAT_LOOP, 5.0f, 5.0f);
            }
        }
        return false;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        final FallingBlock rocket = this.rockets.remove(player);
        if (rocket != null && !rocket.isDead()) {
            final Location location = rocket.getLocation();
            ParticleEffect.LARGE_EXPLODE.display(location, 0.0f, 0.0f, 0.0f, 0.0f, 1);
            location.getWorld().playSound(location, Sound.EXPLODE, 1.0f, 0.5f);
        }
        final List<Entity> entities = this.entities.remove(player);
        if (entities != null) {
            for (final Entity entity : entities) {
                if (!entity.isDead()) {
                    entity.remove();
                }
            }
        }
    }
}