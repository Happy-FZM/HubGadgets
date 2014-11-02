package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.ItemBuilder;
import me.Jaaakee224.HubGadgets.util.ParticleEffect;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class SuicidalSheepAction implements Gadget.GadgetAction
{
    public static Map<Player, Sheep> playerSheep;
    public static Map<Player, Bat> playerBat;
    public static Map<Player, List<Entity>> playerEntities;
    
    static {
        SuicidalSheepAction.playerSheep = new HashMap<Player, Sheep>();
        SuicidalSheepAction.playerBat = new HashMap<Player, Bat>();
        SuicidalSheepAction.playerEntities = new HashMap<Player, List<Entity>>();
    }
    
    @SuppressWarnings("serial")
	@Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger == Gadget.TriggerAction.INTERACT) {
            final Player player = ((PlayerEvent)event).getPlayer();
            Location location = player.getLocation();
            location = location.toVector().add(location.getDirection().multiply(2.0)).toLocation(player.getWorld());
            @SuppressWarnings({ "unchecked", "rawtypes" })
			final Bat bat = (Bat)player.getWorld().spawn(location, (Class)Bat.class);
            bat.setNoDamageTicks(Integer.MAX_VALUE);
            bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
            @SuppressWarnings({ "unchecked", "rawtypes" })
			final Sheep sheep = (Sheep)player.getWorld().spawn(location, (Class)Sheep.class);
            sheep.setNoDamageTicks(Integer.MAX_VALUE);
            sheep.setColor(DyeColor.RED);
            bat.setPassenger(sheep);
            bat.setVelocity(location.getDirection().add(new Vector(0.0, 1.3, 0.0)).multiply(1.7));
            SuicidalSheepAction.playerSheep.put(player, sheep);
            SuicidalSheepAction.playerBat.put(player, bat);
            SuicidalSheepAction.playerEntities.put(player, new ArrayList<Entity>() {
                {
                    this.add(sheep);
                    this.add(bat);
                }
            });
            return true;
        }
        return false;
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        if (ticks == 60L || (ticks <= 120L && ticks > 60L && ticks % 3L == 0L)) {
            final Sheep sheep = SuicidalSheepAction.playerSheep.get(player);
            if (sheep == null) {
                return true;
            }
            final World world = sheep.getWorld();
            final Location location = sheep.getLocation();
            if (ticks == 60L) {
                sheep.remove();
                world.createExplosion(location, 0.0f);
                final List<Entity> entities = SuicidalSheepAction.playerEntities.get(player);
                final List<ItemStack> contents = new ArrayList<ItemStack>();
                for (int i = 0; i < 15; ++i) {
                    contents.add(ItemBuilder.getRandomResource(Material.REDSTONE));
                    contents.add(ItemBuilder.getRandomResource(Material.PORK));
                }
                for (final ItemStack stack : contents) {
                    final Item item = world.dropItem(location, stack);
                    final float x = -0.2f + (float)(Math.random() * 1.4);
                    final float y = -0.2f + (float)(Math.random() * 1.4);
                    final float z = -0.2f + (float)(Math.random() * 1.4);
                    item.setVelocity(new Vector(x, y, z));
                    item.setPickupDelay(Integer.MAX_VALUE);
                    entities.add(item);
                }
            }
            else {
                if (ticks == 120L) {
                    world.playSound(location, Sound.FUSE, 5.0f, 5.0f);
                }
                final DyeColor color = (sheep.getColor() == DyeColor.WHITE) ? DyeColor.RED : DyeColor.WHITE;
                sheep.setColor(color);
                ParticleEffect.displayBlockCrack(sheep.getLocation(), Material.WOOL.getId(), color.getWoolData(), 0.0f, 0.3f, 0.0f, 5);
            }
        }
        return false;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        SuicidalSheepAction.playerSheep.get(player);
        SuicidalSheepAction.playerBat.remove(player);
        final List<Entity> entities = SuicidalSheepAction.playerEntities.get(player);
        if (entities != null) {
            for (final Entity entity : entities) {
                if (!entity.isDead()) {
                    entity.remove();
                }
            }
        }
    }
}