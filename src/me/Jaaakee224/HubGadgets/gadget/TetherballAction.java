package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.config.Translation;
import me.Jaaakee224.HubGadgets.handler.Gadget;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class TetherballAction implements Gadget.GadgetAction {
    private final Map<Player, List<Entity>> entities;
    private final Map<Player, List<Block>> blocks;
    
    public TetherballAction() {
        super();
        this.entities = new HashMap<Player, List<Entity>>();
        this.blocks = new HashMap<Player, List<Block>>();
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.HANGING_BREAK && trigger != Gadget.TriggerAction.BLOCK_BREAK && trigger != Gadget.TriggerAction.INTERACT_BLOCK) {
            return false;
        }
        if (trigger == Gadget.TriggerAction.HANGING_BREAK) {
            final HangingBreakEvent breakEvent = (HangingBreakEvent)event;
            final Entity entity = breakEvent.getEntity();
            if (entity instanceof LeashHitch) {
                for (final List<Entity> entities : this.entities.values()) {
                    if (entities.contains(entity)) {
                        return true;
                    }
                }
            }
            return false;
        }
        if (trigger == Gadget.TriggerAction.BLOCK_BREAK) {
            final BlockBreakEvent breakEvent2 = (BlockBreakEvent)event;
            final Block block = breakEvent2.getBlock();
            if (block.getType() == Material.FENCE) {
                for (final List<Block> blocks : this.blocks.values()) {
                    if (blocks.contains(block)) {
                        return true;
                    }
                }
            }
            return false;
        }
        if (trigger == Gadget.TriggerAction.INTERACT_BLOCK) {
            final PlayerInteractEvent interactEvent = (PlayerInteractEvent)event;
            final Player player = interactEvent.getPlayer();
            final Block block2 = interactEvent.getClickedBlock();
            Location location = null;
            if (block2 == null || block2.getType() == Material.AIR || !Gadget.checkEmptyBlocks(location = block2.getLocation().add(0.0, 1.0, 0.0), 8)) {
                player.sendMessage(Translation.getTranslation("target_clean_area"));
                return false;
            }
            final List<Block> blocks2 = new ArrayList<Block>();
            final World world = location.getWorld();
            for (int i = 0; i < 8; ++i) {
                this.addBlock(location.getBlock(), blocks2).setType(Material.FENCE);
                location = location.add(0.0, 1.0, 0.0);
            }
            @SuppressWarnings({ "rawtypes", "unchecked" })
			final LeashHitch leash = (LeashHitch)world.spawn(location.subtract(0.0, 1.0, 0.0), (Class)LeashHitch.class);
            @SuppressWarnings({ "rawtypes", "unchecked" })
			final Chicken chicken = (Chicken)world.spawn(location.add(1.0, -5.0, 0.0), (Class)Chicken.class);
            chicken.setMaxHealth(2048);
            chicken.setHealth(2048);
            chicken.setLeashHolder(leash);
            this.blocks.put(player, blocks2);
            this.entities.put(player, Arrays.asList(leash, chicken));
        }
        return true;
    }
    
    private Block addBlock(final Block block, final List<Block> blocks) {
        blocks.add(block);
        return block;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        for (final Block block : this.blocks.remove(player)) {
            block.setType(Material.AIR);
        }
        for (final Entity entity : this.entities.remove(player)) {
            entity.remove();
        }
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        return false;
    }
}