package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.MathUtils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

public class CatapultAction implements Gadget.GadgetAction
{
    private final Map<Player, List<Ocelot>> entities;
    
    public CatapultAction() {
        super();
        this.entities = new HashMap<Player, List<Ocelot>>();
    }
    
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT) {
            return false;
        }
        final Player player = ((PlayerEvent)event).getPlayer();
        final World world = player.getWorld();
        final Location location = player.getLocation();
        final List<Ocelot> entities = new ArrayList<Ocelot>();
        for (int i = 0; i < MathUtils.random(4, 6); ++i) {
            final float x = -4.0f + (float)(Math.random() * 9.0);
            final float z = -4.0f + (float)(Math.random() * 9.0);
            final Ocelot cat = (Ocelot)world.spawn(location, (Class)Ocelot.class);
            cat.setCatType(Ocelot.Type.values()[MathUtils.random.nextInt(Ocelot.Type.values().length)]);
            cat.setTamed(true);
            cat.setBaby();
            cat.setVelocity(new Vector(x, 0.0f, z));
            entities.add(cat);
        }
        this.entities.put(player, entities);
        return true;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        final List<Ocelot> entities = this.entities.remove(player);
        if (entities != null) {
            for (final Ocelot cat : entities) {
                final Location location = cat.getLocation();
                if (!cat.isDead()) {
                    cat.remove();
                    location.getWorld().createExplosion(location, 0.0f);
                }
            }
        }
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        return false;
    }
}