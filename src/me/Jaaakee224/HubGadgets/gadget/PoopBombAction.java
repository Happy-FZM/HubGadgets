package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.HubGadgetsPlugin;
import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.ItemBuilder;
import me.Jaaakee224.HubGadgets.util.MathUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PoopBombAction implements Gadget.GadgetAction {
    private final Map<Player, Item> bombs;
    private final Map<Player, List<Item>> entities;
    
    public PoopBombAction() {
        super();
        this.bombs = new HashMap<Player, Item>();
        this.entities = new HashMap<Player, List<Item>>();
    }
    
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT) {
            return false;
        }
        final Player player = ((PlayerEvent)event).getPlayer();
        final World world = player.getWorld();
        final Location location = player.getLocation();
        final Item item = world.dropItem(location, ItemBuilder.getRandomResource(Material.INK_SACK, (short)3));
        item.setVelocity(location.getDirection().multiply(1));
        item.setPickupDelay(Integer.MAX_VALUE);
        world.playSound(location, Sound.FUSE, 10.0f, 5.0f);
        new BukkitRunnable() {
            @Override
			public void run() {
                if (!item.isDead()) {
                    final Location location = item.getLocation();
                    final World world = location.getWorld();
                    item.remove();
                    world.createExplosion(location, 0.0f);
                    final List<Item> entities = new ArrayList<Item>();
                    for (int i = 0; i < MathUtils.random.nextInt(41) + 40; ++i) {
                        final Item poop = world.dropItem(location, ItemBuilder.getRandomResource(Material.INK_SACK, (short)3));
                        final float x = -0.8f + (float)(Math.random() * 2.6);
                        final float y = -0.4f + (float)(Math.random() * 1.8);
                        final float z = -0.8f + (float)(Math.random() * 2.6);
                        poop.setVelocity(new Vector(x, y, z));
                        poop.setPickupDelay(Integer.MAX_VALUE);
                        entities.add(poop);
                    }
                    for (int i = 0; i < MathUtils.random.nextInt(21) + 20; ++i) {
                        final Item poop = world.dropItem(location, ItemBuilder.getRandomResource(Material.WOOL, 1, (short)12));
                        final float x = -0.8f + (float)(Math.random() * 2.6);
                        final float y = -0.4f + (float)(Math.random() * 1.8);
                        final float z = -0.8f + (float)(Math.random() * 2.6);
                        poop.setVelocity(new Vector(x, y, z));
                        poop.setPickupDelay(Integer.MAX_VALUE);
                        entities.add(poop);
                    }
                    PoopBombAction.this.entities.put(player, entities);
                }
            }
        }.runTaskLater(HubGadgetsPlugin.i, 80L);
        this.bombs.put(player, item);
        return true;
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        if (ticks % 20L == 0L) {
            final int seconds = (int)(ticks / 20L);
            final int duration = gadget.getDuration();
            if (seconds != duration && seconds >= duration - 3) {
                final Item bomb = this.bombs.get(player);
                if (bomb == null || bomb.isDead()) {
                    return true;
                }
                bomb.getWorld().playSound(bomb.getLocation(), Sound.LAVA_POP, 1.5f, 0.5f);
            }
        }
        return false;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        final Item bomb = this.bombs.remove(player);
        if (bomb != null && !bomb.isDead()) {
            bomb.remove();
        }
        final List<Item> entities = this.entities.remove(player);
        if (entities != null) {
            for (final Item entity : entities) {
                if (!entity.isDead()) {
                    entity.remove();
                }
            }
        }
    }
}