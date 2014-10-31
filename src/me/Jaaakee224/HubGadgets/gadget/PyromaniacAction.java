package me.Jaaakee224.HubGadgets.gadget;

import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.ParticleEffect;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class PyromaniacAction implements Gadget.GadgetAction
{
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        return true;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        player.setFireTicks(Integer.MAX_VALUE);
        final Location location = player.getLocation().add(0.0, 1.0, 0.0);
        ParticleEffect.LAVA.display(location, 0.0f, 0.2f, 0.0f, 0.0f, 1);
        ParticleEffect.SMOKE.display(location, 0.0f, 0.2f, 0.0f, 0.0f, 1);
        return false;
    }
}