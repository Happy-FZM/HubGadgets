package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.ItemBuilder;
import me.Jaaakee224.HubGadgets.util.MathUtils;
import me.Jaaakee224.HubGadgets.util.ParticleEffect;
import me.Jaaakee224.HubGadgets.util.RandomUtils;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class SelfDestructAction implements Gadget.GadgetAction {
	
    private final Map<Player, Integer> particles;
    private final Map<Player, List<Item>> entities;
    
    public SelfDestructAction() {
        super();
        this.particles = new HashMap<Player, Integer>();
        this.entities = new HashMap<Player, List<Item>>();
    }
    
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT) {
            return false;
        }
        this.particles.put(((PlayerEvent)event).getPlayer(), 1);
        return true;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        this.particles.remove(player);
        final List<Item> entities = this.entities.remove(player);
        if (entities != null) {
            for (final Item entity : entities) {
                if (!entity.isDead()) {
                    entity.remove();
                }
            }
        }
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        if (ticks % 5L == 0L) {
            Integer particles = this.particles.get(player);
            if (particles == null) {
                return true;
            }
            if (particles > 0 && ticks % 20L != 0L && ticks % 5L == 0L) {
                for (int i = 0; i < 20; ++i) {
                    final Location location = player.getLocation();
                    location.add(RandomUtils.getRandomCircleVector().multiply(MathUtils.random.nextDouble() * 0.6));
                    location.add(0.0, MathUtils.random.nextFloat() * 2.0f, 0.0);
                    ParticleEffect.SMOKE.display(location, 0.0f, 0.0f, 0.0f, 0.0f, 2 * particles);
                    ParticleEffect.displayBlockCrack(location, Material.REDSTONE_BLOCK.getId(), (byte)0, 0.0f, 0.0f, 0.0f, particles / 3);
                }
            }
            else if (ticks % 20L == 0L) {
                final World world = player.getWorld();
                final int remainingSecs = (int)(gadget.getDuration() - (gadget.getDuration() - ticks / 20L));
                if (remainingSecs == gadget.getDuration() - 4) {
                    world.playSound(player.getLocation(), Sound.FUSE, 5.0f, 5.0f);
                    player.playEffect(EntityEffect.HURT);
                }
                else if (remainingSecs > gadget.getDuration() - 5) {
                    ++particles;
                    this.particles.put(player, particles);
                }
                else if (remainingSecs == gadget.getDuration() - 5) {
                    player.playEffect(EntityEffect.HURT);
                    final Location location2 = player.getLocation();
                    world.createExplosion(location2, 0.0f);
                    final List<Item> entities = new ArrayList<Item>();
                    final List<ItemStack> contents = new ArrayList<ItemStack>();
                    
                    for (int j = 0; j < 15; ++j) {
                        contents.add(ItemBuilder.getRandomResource(Material.REDSTONE));
                        contents.add(ItemBuilder.getRandomResource(Material.PORK));
                        contents.add(ItemBuilder.getRandomResource(Material.REDSTONE_BLOCK));
                        contents.add(ItemBuilder.getRandomResource(Material.INK_SACK, 1, (short) 1));
                    }
                    final Iterator<ItemStack> iterator = contents.iterator();
                    while (iterator.hasNext()) {
                        final ItemStack stack = iterator.next();
                        final Item item = world.dropItem(location2, stack);
                        final float x = -0.2f + (float)(Math.random() * 1.4);
                        final float y = -0.2f + (float)(Math.random() * 1.4);
                        final float z = -0.2f + (float)(Math.random() * 1.4);
                        item.setVelocity(new Vector(x, y, z));
                        item.setPickupDelay(Integer.MAX_VALUE);
                        entities.add(item);
                    }
                    this.particles.put(player, -1);
                    this.entities.put(player, entities);
                    return false;
                }
            }
        }
        return false;
    }
}