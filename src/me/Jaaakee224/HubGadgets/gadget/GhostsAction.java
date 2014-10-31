package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.MathUtils;
import me.Jaaakee224.HubGadgets.util.ParticleEffect;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GhostsAction implements Gadget.GadgetAction {
    public Map<Player, List<Entity>> playerEntities;
    
    public GhostsAction() {
        super();
        this.playerEntities = new HashMap<Player, List<Entity>>();
    }
    
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger == Gadget.TriggerAction.INTERACT) {
            final Player player = ((PlayerEvent)event).getPlayer();
            final Location location = player.getTargetBlock((HashSet)null, 4).getLocation().add(0.0, 4.0, 0.0);
            final List<Entity> entities = new ArrayList<Entity>();
            for (int i = 0; i < MathUtils.random(4, 6); ++i) {
                final Bat bat = (Bat)player.getWorld().spawn(location, (Class)Bat.class);
                bat.setNoDamageTicks(Integer.MAX_VALUE);
                bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
                final Zombie zombie = (Zombie)player.getWorld().spawn(location.clone().add(MathUtils.random(0, 1), 0.0, MathUtils.random(0, 2)), (Class)Zombie.class);
                zombie.setBaby(true);
                zombie.getEquipment().setArmorContents((ItemStack[])null);
                zombie.setNoDamageTicks(Integer.MAX_VALUE);
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
                final ItemStack head = new ItemStack(MathUtils.random.nextBoolean() ? Material.JACK_O_LANTERN : Material.SKULL_ITEM);
                head.setDurability((short)(MathUtils.randomBoolean() ? 0 : 1));
                zombie.getEquipment().setHelmet(head);
                bat.setPassenger(zombie);
                entities.add(zombie);
                entities.add(bat);
            }
            this.playerEntities.put(player, entities);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        if (ticks % 20L == 0L) {
            final List<Entity> entities = this.playerEntities.get(player);
            if (entities == null || entities.isEmpty()) {
                return true;
            }
            for (final Entity entity : entities) {
                ParticleEffect.WITCH_MAGIC.display(entity.getLocation(), 0.2f, 0.2f, 0.2f, 0.2f, 5);
            }
            if (MathUtils.randomBoolean(0.25f)) {
                final Entity entity = entities.get(0);
                entity.getWorld().playSound(entity.getLocation(), MathUtils.randomBoolean() ? Sound.ENDERDRAGON_GROWL : Sound.WOLF_HOWL, 5.0f, 5.0f);
            }
        }
        return false;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        final List<Entity> entities = this.playerEntities.remove(player);
        if (entities != null) {
            for (final Entity entity : entities) {
                if (!entity.isDead()) {
                    entity.remove();
                }
            }
        }
    }
}