package me.Jaaakee224.HubGadgets.gadget;

import me.Jaaakee224.HubGadgets.HubGadgetsPlugin;
import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.FireworkEffectPlayer;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

public class RailgunAction implements Gadget.GadgetAction
{
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT) {
            return false;
        }
        final Player player = ((PlayerEvent)event).getPlayer();
        final Location eyeLocation = player.getEyeLocation();
        final double px = eyeLocation.getX();
        final double py = eyeLocation.getY();
        final double pz = eyeLocation.getZ();
        final double yaw = Math.toRadians(eyeLocation.getYaw() + 90.0f);
        final double pitch = Math.toRadians(eyeLocation.getPitch() + 90.0f);
        final double x = Math.sin(pitch) * Math.cos(yaw);
        final double y = Math.sin(pitch) * Math.sin(yaw);
        final double z = Math.cos(pitch);
        for (int i = 1; i < 50; ++i) {
            final Location location = new Location(player.getWorld(), px + i * x, py + i * z, pz + i * y);
            if (location.getBlock().getType().isSolid()) {
                break;
            }
            try {
                FireworkEffectPlayer.playFirework(HubGadgetsPlugin.i, player.getWorld(), location);
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 0.1f, 2.0f);
        return true;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        return false;
    }
}