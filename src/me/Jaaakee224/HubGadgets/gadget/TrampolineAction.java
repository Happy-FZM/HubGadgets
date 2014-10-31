package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.config.Translation;
import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.DirectionUtils;
import me.Jaaakee224.HubGadgets.util.LocationUtils;
import me.Jaaakee224.HubGadgets.util.MathUtils;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class TrampolineAction implements Gadget.GadgetAction
{
    public static final char[] SCHEMA;
    public final Map<Player, List<Block>> playerBlocks;
    public final Map<Player, Location> playerTrampoline;
    
    static {
        SCHEMA = new char[] { 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'B', 'B', 'B', 'B', 'B', 'A', 'A', 'B', 'B', 'B', 'B', 'B', 'A', 'A', 'B', 'B', 'B', 'B', 'B', 'A', 'A', 'B', 'B', 'B', 'B', 'B', 'A', 'A', 'B', 'B', 'B', 'B', 'B', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A' };
    }
    
    public TrampolineAction() {
        super();
        this.playerBlocks = new HashMap<Player, List<Block>>();
        this.playerTrampoline = new HashMap<Player, Location>();
    }
    
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.BLOCK_BREAK && trigger != Gadget.TriggerAction.INTERACT) {
            return false;
        }
        if (trigger == Gadget.TriggerAction.BLOCK_BREAK) {
            final BlockBreakEvent breakEvent = (BlockBreakEvent)event;
            final Block block = breakEvent.getBlock();
            if (!breakEvent.getPlayer().isOp() && (block.getType() == Material.WOOL || block.getType() == Material.WOOD_STAIRS)) {
                for (final List<Block> blocks : this.playerBlocks.values()) {
                    if (blocks.contains(block)) {
                        return true;
                    }
                }
            }
            return false;
        }
        if (trigger == Gadget.TriggerAction.INTERACT) {
            final PlayerInteractEvent interactEvent = (PlayerInteractEvent)event;
            final Player player = interactEvent.getPlayer();
            final Location playerLocation = player.getLocation();
            if (!Gadget.checkEmptyArea(playerLocation.clone().add(-5.0, 0.0, -5.0), playerLocation.clone().add(5.0, 1.0, 5.0))) {
                player.sendMessage(Translation.getTranslation("target_clean_area"));
                return false;
            }
            final DirectionUtils.Direction direction = DirectionUtils.getCardinalDirection(player);
            final Location corner = playerLocation.clone().add(-4.0, 1.0, -4.0);
            final Location line = corner.clone();
            Location location = corner.clone();
            final List<Block> blocks2 = new ArrayList<Block>();
            for (int i = 0; i < TrampolineAction.SCHEMA.length; ++i) {
                if (i % 7 == 0) {
                    location = line.add(1.0, 0.0, 0.0).clone();
                }
                location.add(0.0, 0.0, 1.0);
                if (i == 24) {
                    this.playerTrampoline.put(player, location.clone().add(0.0, 1.0, 0.0));
                }
                final char type = TrampolineAction.SCHEMA[i];
                final Block block2 = location.getBlock();
                block2.setType(Material.WOOL);
                block2.setData((type == 'A') ? DyeColor.BLUE.getWoolData() : DyeColor.BLACK.getWoolData());
                blocks2.add(block2);
            }
            final Location blockLocation = playerLocation.clone().add(0.0, 1.0, 0.0);
            final Block firstStair = blockLocation.add((direction == DirectionUtils.Direction.NORTH) ? 5 : ((direction == DirectionUtils.Direction.SOUTH) ? -5 : 0), -1.0, (direction == DirectionUtils.Direction.EAST) ? 5 : ((direction == DirectionUtils.Direction.WEST) ? -5 : 0)).getBlock();
            final Block secondStair = blockLocation.add((direction == DirectionUtils.Direction.NORTH) ? -1 : ((direction == DirectionUtils.Direction.SOUTH) ? 1 : 0), 1.0, (direction == DirectionUtils.Direction.EAST) ? -1 : ((direction == DirectionUtils.Direction.WEST) ? 1 : 0)).getBlock();
            firstStair.setType(Material.WOOD_STAIRS);
            firstStair.setData((byte)((direction == DirectionUtils.Direction.NORTH) ? 1 : ((direction == DirectionUtils.Direction.EAST) ? 3 : ((direction == DirectionUtils.Direction.WEST) ? 2 : 0))));
            secondStair.setType(Material.WOOD_STAIRS);
            secondStair.setData((byte)((direction == DirectionUtils.Direction.NORTH) ? 1 : ((direction == DirectionUtils.Direction.EAST) ? 3 : ((direction == DirectionUtils.Direction.WEST) ? 2 : 0))));
            blocks2.add(firstStair);
            blocks2.add(secondStair);
            player.teleport(playerLocation.add(0.0, 2.0, 0.0));
            this.playerBlocks.put(player, blocks2);
        }
        return true;
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        if (ticks % 10L == 0L) {
            final Location location = this.playerTrampoline.get(player);
            if (location == null) {
                return true;
            }
            Entity[] nearbyEntities;
            for (int length = (nearbyEntities = LocationUtils.getNearbyEntities(location, 5)).length, i = 0; i < length; ++i) {
                final Entity entity = nearbyEntities[i];
                entity.setVelocity(entity.getVelocity().add(new Vector(0.0f, MathUtils.random(1.8f, 3.7f), 0.0f)));
            }
        }
        return false;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        final List<Block> blocks = this.playerBlocks.remove(player);
        if (blocks != null) {
            for (final Block block : blocks) {
                block.setType(Material.AIR);
            }
        }
    }
}