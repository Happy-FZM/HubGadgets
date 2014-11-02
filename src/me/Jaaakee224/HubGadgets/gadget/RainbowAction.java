package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.DirectionUtils;
import me.Jaaakee224.HubGadgets.util.ItemBuilder;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

public class RainbowAction implements Gadget.GadgetAction
{
    private final Map<Player, List<Entity>> entities;
    
    public RainbowAction() {
        super();
        this.entities = new HashMap<Player, List<Entity>>();
    }
    
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT) {
            return false;
        }
        final Player player = ((PlayerEvent)event).getPlayer();
        final List<Entity> entities = this.createRainbow(player.getLocation(), DirectionUtils.getCardinalDirection(player));
        this.entities.put(player, entities);
        return true;
    }
    
    private List<Entity> createRainbow(final Location location, final DirectionUtils.Direction direction) {
        final Location copy = location.clone();
        final List<Entity> entities = new ArrayList<Entity>();
        @SuppressWarnings("deprecation")
		final byte[] colors = { DyeColor.PINK.getWoolData(), DyeColor.PURPLE.getWoolData(), DyeColor.LIGHT_BLUE.getWoolData(), DyeColor.LIME.getWoolData(), DyeColor.YELLOW.getWoolData(), DyeColor.ORANGE.getWoolData(), DyeColor.RED.getWoolData() };
        byte[] array;
        for (int length = (array = colors).length, i = 0; i < length; ++i) {
            final byte color = array[i];
            this.createRainbowLine(copy.add(0.0, 0.37, 0.0), Material.WOOL, color, entities, direction);
        }
        return entities;
    }
    
    private void createRainbowLine(final Location location, final Material material, final short durability, final List<Entity> entities, final DirectionUtils.Direction direction) {
        final Location copy = location.clone();
        this.createRainbowPart(copy, material, durability, 2, 4, entities, direction);
        this.createRainbowPart(copy, material, durability, 3, 2, entities, direction);
        this.createRainbowPart(copy, material, durability, 4, 1, entities, direction);
        this.createRainbowPart(copy, material, durability, 10, 1, entities, direction);
        this.createRainbowPart(copy, material, durability, 1, 1, entities, direction);
        this.createRainbowPart(copy, material, durability, 10, 1, false, entities, direction);
        this.createRainbowPart(copy, material, durability, 4, 1, false, entities, direction);
        this.createRainbowPart(copy, material, durability, 3, 2, false, entities, direction);
        this.createRainbowPart(copy, material, durability, 2, 4, false, entities, direction);
    }
    
    private void createRainbowPart(final Location location, final Material material, final short durability, final int items, final int lines, final List<Entity> entities, final DirectionUtils.Direction direction) {
        this.createRainbowPart(location, material, durability, items, lines, true, entities, direction);
    }
    
    private void createRainbowPart(Location location, final Material material, final short durability, final int items, final int lines, final boolean add, final List<Entity> entities, final DirectionUtils.Direction direction) {
        final World world = location.getWorld();
        for (int i = 0; i < lines; ++i) {
            location.add(0.0, (add ? 1 : -1) * 0.9, 0.0);
            for (int j = 0; j < items; ++j) {
                location = location.add(((direction == DirectionUtils.Direction.EAST) ? 1 : -1) * -0.37, 0.0, ((direction == DirectionUtils.Direction.NORTH) ? 1 : -1) * -0.37);
                @SuppressWarnings({ "unchecked", "rawtypes" })
				final WitherSkull skull = (WitherSkull)world.spawn(location, (Class)WitherSkull.class);
                skull.setDirection(new Vector(0, 0, 0));
                skull.setVelocity(new Vector(0, 0, 0));
                final Item item = world.dropItem(location, ItemBuilder.getRandomResource(material, durability));
                item.setPickupDelay(Integer.MAX_VALUE);
                item.setVelocity(new Vector(0, 0, 0));
                skull.setPassenger(item);
                entities.add(skull);
                entities.add(item);
            }
        }
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        final List<Entity> entities = this.entities.remove(player);
        if (entities != null) {
            for (final Entity entity : entities) {
                entity.remove();
            }
        }
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        return false;
    }
}