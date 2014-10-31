package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.config.Translation;
import me.Jaaakee224.HubGadgets.handler.Gadget;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerEvent;

public class KawarimiAction implements Gadget.GadgetAction
{
    private final Map<Player, List<Block>> blocks;
    
    public KawarimiAction() {
        super();
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
            if (block.getType() == Material.LOG) {
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
            this.addBlock(location.getBlock(), blocks).setType(Material.LOG);
            this.addBlock(location.add(0.0, 1.0, 0.0).getBlock(), blocks).setType(Material.LOG);
            this.addBlock(location.add(0.0, 1.0, 0.0).getBlock(), blocks).setType(Material.LOG);
            this.blocks.put(player, blocks);
        }
        return true;
    }
    
    private Block addBlock(final Block block, final List<Block> blocks) {
        blocks.add(block);
        return block;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        final List<Block> blocks = this.blocks.remove(player);
        if (blocks != null) {
            for (final Block block : blocks) {
                block.setType(Material.AIR);
            }
        }
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        return false;
    }
}