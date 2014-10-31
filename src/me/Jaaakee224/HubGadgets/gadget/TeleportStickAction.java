package me.Jaaakee224.HubGadgets.gadget;

import java.util.HashSet;

import me.Jaaakee224.HubGadgets.config.Translation;
import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.ParticleEffect;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

public class TeleportStickAction implements Gadget.GadgetAction
{
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT) {
            return false;
        }
        final Player player = ((PlayerEvent)event).getPlayer();
        Block block = player.getTargetBlock((HashSet)null, 50);
        Location location = block.getLocation();
        if (block.getType() == Material.AIR) {
            player.sendMessage(Translation.getTranslation("target_clean_area"));
            return false;
        }
        if (!Gadget.checkEmptyBlocks(location, 2)) {
            for (int i = location.getBlockY(); i < 255; ++i) {
                location = location.add(0.0, 1.0, 0.0);
                if (location.getBlock().getType() == Material.AIR) {
                    block = location.getBlock();
                    break;
                }
            }
            if (block.getType() != Material.AIR) {
                player.sendMessage(Translation.getTranslation("target_clean_area"));
                return false;
            }
        }
        if (!block.isEmpty()) {
            location = location.add(0.0, 1.0, 0.0);
        }
        final Location playerLocation = player.getLocation();
        location.setYaw(playerLocation.getYaw());
        location.setPitch(playerLocation.getPitch());
        player.teleport(location);
        player.playSound(location, Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
        ParticleEffect.HAPPY_VILLAGER.display(location.add(0.0, 1.0, 0.0), 0.3f, 0.5f, 0.3f, 0.0f, 20);
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