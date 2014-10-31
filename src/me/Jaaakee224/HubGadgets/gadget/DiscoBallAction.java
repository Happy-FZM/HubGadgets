package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.config.Translation;
import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.ParticleEffect;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerEvent;

public class DiscoBallAction implements Gadget.GadgetAction {
    private final Map<Player, Location> effects;
    private final Map<Player, List<Block>> blocks;
    
    public DiscoBallAction() {
        super();
        this.effects = new HashMap<Player, Location>();
        this.blocks = new HashMap<Player, List<Block>>();
    }
    
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.BLOCK_BREAK && trigger != Gadget.TriggerAction.INTERACT) {
            return false;
        }
        if (trigger == Gadget.TriggerAction.BLOCK_BREAK) {
            final BlockBreakEvent breakEvent = (BlockBreakEvent)event;
            final Block block = breakEvent.getBlock();
            if (!breakEvent.getPlayer().isOp() && (block.getType() == Material.ICE || block.getType() == Material.STEP)) {
                for (final List<Block> blocks : this.blocks.values()) {
                    if (blocks.contains(block)) {
                        return true;
                    }
                }
            }
            return false;
        }
        if (trigger == Gadget.TriggerAction.INTERACT) {
            final Player player = ((PlayerEvent)event).getPlayer();
            final Location location = player.getLocation();
            if (!Gadget.checkEmptyBlocks(location, 3)) {
                player.sendMessage(Translation.getTranslation("target_clean_area"));
                return false;
            }
            final List<Block> blocks = new ArrayList<Block>();
            player.playEffect(location, Effect.RECORD_PLAY, Material.RECORD_4);
            this.addBlock(location.add(0.0, 5.0, 0.0).getBlock(), blocks).setType(Material.STAINED_GLASS);
            this.blocks.put(player, blocks);
            this.effects.put(player, location.getBlock().getLocation().add(0.5, 0.5, 0.5));
        }
        return true;
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public void onFinish(final Player player, final Gadget gadget) {
        this.effects.remove(player);
        final List<Block> blocks = this.blocks.remove(player);
        if (blocks != null) {
            for (final Block block : blocks) {
                block.setType(Material.AIR);
                player.playEffect(player.getLocation().add(0.0, 5.0, 0.0), Effect.RECORD_PLAY, 0);
            }
        }
    }
    
    private Block addBlock(final Block block, final List<Block> blocks) {
        blocks.add(block);
        return block;
    }
   
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        if (ticks % 20L == 0L) {
            final Location effect = this.effects.get(player);
            if (effect == null) {
                return true;
            }
            ParticleEffect.FIREWORKS_SPARK.display(effect, 0.0f, 0.0f, 0.0f, 0.2f, 10);
        }
        return false;
    }
}