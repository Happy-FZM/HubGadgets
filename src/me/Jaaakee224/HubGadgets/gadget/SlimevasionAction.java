package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.MathUtils;
import me.Jaaakee224.HubGadgets.util.ParticleEffect;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

public class SlimevasionAction implements Gadget.GadgetAction
{
    private final Map<Player, List<Slime>> entities;
    
    public SlimevasionAction() {
        super();
        this.entities = new HashMap<Player, List<Slime>>();
    }
    
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT && trigger != Gadget.TriggerAction.ENTITY_DAMAGE) {
            return false;
        }
        if (trigger == Gadget.TriggerAction.ENTITY_DAMAGE) {
            final Entity entity = ((EntityDamageEvent)event).getEntity();
            if (entity instanceof Slime) {
                final Slime slime = (Slime)entity;
                for (final Map.Entry<Player, List<Slime>> entries : this.entities.entrySet()) {
                    final List<Slime> entities = entries.getValue();
                    if (entities.remove(slime)) {
                        if (slime.getSize() > 1) {
                            for (int j = 0; j < 4 - slime.getSize(); ++j) {
                                entities.add(this.spawnSlime(slime.getWorld(), slime.getLocation(), slime.getSize() - 1));
                            }
                        }
                        slime.remove();
                        return true;
                    }
                }
            }
            return false;
        }
        if (trigger == Gadget.TriggerAction.INTERACT) {
            final Player player = ((PlayerEvent)event).getPlayer();
            final World world = player.getWorld();
            final Location location = player.getLocation();
            final List<Slime> entities2 = new ArrayList<Slime>();
            for (int i = 0; i < 5; ++i) {
                entities2.add(this.spawnSlime(world, location, 3));
            }
            this.entities.put(player, entities2);
        }
        return true;
    }
    
    private Slime spawnSlime(final World world, final Location location, final int size) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
		final Slime slime = (Slime)world.spawn(location, (Class)Slime.class);
        slime.setSize(size);
        slime.setCustomNameVisible(false);
        slime.setVelocity(new Vector(MathUtils.random(-2, 1), MathUtils.random(size - 1, size + 1), MathUtils.random(-1, 2)));
        return slime;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        final List<Slime> entities = this.entities.remove(player);
        if (entities != null) {
            for (final Entity entity : entities) {
                if (!entity.isDead()) {
                    entity.remove();
                }
            }
        }
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        if (ticks % 20L == 0L) {
            final List<Slime> entities = this.entities.get(player);
            if (entities == null || entities.isEmpty()) {
                return true;
            }
            for (final Slime entity : new ArrayList<Slime>(entities)) {
                if (entity.isDead()) {
                    return true;
                }
                if (entity.isOnGround()) {
                    entity.remove();
                    entities.remove(entity);
                }
                ParticleEffect.SLIME.display(entity.getLocation(), 0.5f, 0.5f, 0.5f, 0.2f, 10);
            }
        }
        return false;
    }
}