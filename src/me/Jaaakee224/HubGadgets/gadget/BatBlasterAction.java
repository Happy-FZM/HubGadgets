package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.handler.Gadget;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

public class BatBlasterAction implements Gadget.GadgetAction {
    private final Map<Player, List<Bat>> entities;
    
    public BatBlasterAction() {
        super();
        this.entities = new HashMap<Player, List<Bat>>();
    }
    
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT) {
            return false;
        }
        final Player player = ((PlayerEvent)event).getPlayer();
        final World world = player.getWorld();
        final Location location = player.getLocation().add(0.0, 2.0, 0.0);
        final List<Bat> entities = new ArrayList<Bat>();
        for (int i = 0; i < 10; ++i) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
			final Bat bat = (Bat)world.spawn(location, (Class)Bat.class);
            bat.setNoDamageTicks(Integer.MAX_VALUE);
            bat.setVelocity(location.getDirection());
            entities.add(bat);
        }
        player.playSound(location, Sound.BAT_LOOP, 1.0f, 1.0f);
        this.entities.put(player, entities);
        return true;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        final List<Bat> entities = this.entities.remove(player);
        if (entities != null) {
            for (final Bat entity : entities) {
                if (!entity.isDead()) {
                    entity.remove();
                }
            }
        }
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        final List<Bat> entities = this.entities.get(player);
        if (entities == null) {
            return true;
        }
        final Location location = player.getLocation();
        final List<Entity> targets = new ArrayList<Entity>();
        for (final Bat entity : entities) {
            entity.setVelocity(location.getDirection());
            for (final Entity nearby : entity.getNearbyEntities(0.5, 1.0, 0.5)) {
                if (nearby instanceof Player && nearby != player) {
                    if (targets.contains(nearby)) {
                        continue;
                    }
                    targets.add(nearby);
                    nearby.playEffect(EntityEffect.HURT);
                    nearby.setVelocity(location.getDirection().add(new Vector(0.0, 0.8, 0.0)));
                }
            }
        }
        return false;
    }
}