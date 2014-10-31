package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.ParticleEffect;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerEvent;

public class FunCannonAction implements Gadget.GadgetAction {
    private final Map<Player, List<Projectile>> entities;
    
    public FunCannonAction() {
        super();
        this.entities = new HashMap<Player, List<Projectile>>();
    }
    
    @Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT && trigger != Gadget.TriggerAction.TELEPORT_ENDERPEARL && trigger != Gadget.TriggerAction.PROJECTILE_HIT && trigger != Gadget.TriggerAction.ENTITY_DAMAGE_BY_ENTITY && trigger != Gadget.TriggerAction.HANGING_BREAK_BY_ENTITY) {
            return false;
        }
        if (trigger == Gadget.TriggerAction.TELEPORT_ENDERPEARL) {
            return this.entities.containsKey(((PlayerEvent)event).getPlayer());
        }
        if (trigger == Gadget.TriggerAction.INTERACT) {
            final Player player = ((PlayerEvent)event).getPlayer();
            final List<Projectile> entities = new ArrayList<Projectile>();
            entities.add(player.launchProjectile((Class)Snowball.class));
            entities.add(player.launchProjectile((Class)Snowball.class));
            entities.add(player.launchProjectile((Class)EnderPearl.class));
            this.entities.put(player, entities);
        }
        else {
            if (trigger == Gadget.TriggerAction.ENTITY_DAMAGE_BY_ENTITY || trigger == Gadget.TriggerAction.HANGING_BREAK_BY_ENTITY) {
                Entity damager = null;
                if (trigger == Gadget.TriggerAction.ENTITY_DAMAGE_BY_ENTITY) {
                    damager = ((EntityDamageByEntityEvent)event).getDamager();
                }
                else {
                    damager = ((HangingBreakByEntityEvent)event).getRemover();
                }
                Player player2 = null;
                if (damager instanceof Player) {
                    player2 = (Player)damager;
                }
                else if (damager instanceof Snowball || damager instanceof EnderPearl) {
                    final Projectile projectile = (Projectile)damager;
                    if (projectile.getShooter() instanceof Player) {
                        player2 = (Player)projectile.getShooter();
                    }
                }
                if (player2 != null) {
                    if (player2 == damager) {
                        return this.entities.containsKey(player2);
                    }
                    final List<Projectile> entities2 = this.entities.get(player2);
                    if (entities2 != null && entities2.contains(damager)) {
                        return true;
                    }
                }
                return false;
            }
            if (trigger == Gadget.TriggerAction.PROJECTILE_HIT) {
                final ProjectileHitEvent hitEvent = (ProjectileHitEvent)event;
                if (hitEvent.getEntity() instanceof EnderPearl) {
                    final EnderPearl enderpearl = (EnderPearl)hitEvent.getEntity();
                    if (enderpearl.getShooter() != null && enderpearl.getShooter() instanceof Player) {
                        final Player player3 = (Player)enderpearl.getShooter();
                        if (this.entities.containsKey(player3)) {
                            Location center = enderpearl.getLocation();
                            final World world = center.getWorld();
                            ParticleEffect.HEART.display(center, 16.0);
                            center = center.add(0.0, 1.0, 0.0);
                            ParticleEffect.SMOKE.display(center, 0.3f, 0.5f, 0.0f, 0.3f, 20);
                            ParticleEffect.LAVA.display(center, 0.3f, 0.5f, 0.0f, 0.3f, 20);
                            world.playSound(center, Sound.CAT_MEOW, 1.0f, 1.0f);
                            world.playSound(center, Sound.CAT_MEOW, 1.0f, 1.0f);
                            world.playSound(center, Sound.WOLF_BARK, 1.0f, 1.0f);
                            gadget.finish(player3);
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
        final List<Projectile> entities = this.entities.remove(player);
        if (entities != null) {
            for (final Projectile entity : entities) {
                if (!entity.isDead()) {
                    entity.remove();
                }
            }
        }
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        return false;
    }
}