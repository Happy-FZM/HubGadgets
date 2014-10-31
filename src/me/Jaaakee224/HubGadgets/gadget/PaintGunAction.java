package me.Jaaakee224.HubGadgets.gadget;

import java.util.HashMap;
import java.util.Map;

import me.Jaaakee224.HubGadgets.HubGadgetsPlugin;
import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.MathUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PaintGunAction implements Gadget.GadgetAction
{
    private final Map<EnderPearl, Player> pearls;
    
    public PaintGunAction() {
        super();
        this.pearls = new HashMap<EnderPearl, Player>();
    }
    
    @SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	@Override
    public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
        if (trigger != Gadget.TriggerAction.INTERACT && trigger != Gadget.TriggerAction.TELEPORT_ENDERPEARL && trigger != Gadget.TriggerAction.PROJECTILE_HIT && trigger != Gadget.TriggerAction.ENTITY_DAMAGE_BY_ENTITY && trigger != Gadget.TriggerAction.HANGING_BREAK_BY_ENTITY) {
            return false;
        }
        if (trigger == Gadget.TriggerAction.TELEPORT_ENDERPEARL) {
            return this.pearls.values().contains(((PlayerEvent)event).getPlayer());
        }
        if (trigger == Gadget.TriggerAction.INTERACT) {
            final Player player = ((PlayerEvent)event).getPlayer();
            final Location location = player.getLocation();
            player.playSound(location, Sound.CHICKEN_EGG_POP, 8.0f, 10.0f);
            this.pearls.put((EnderPearl)player.launchProjectile((Class)EnderPearl.class), player);
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
                return (damager instanceof EnderPearl && this.pearls.containsKey(damager)) || (damager instanceof Player && this.pearls.values().contains(damager));
            }
            if (trigger == Gadget.TriggerAction.PROJECTILE_HIT) {
                final ProjectileHitEvent hitEvent = (ProjectileHitEvent)event;
                if (hitEvent.getEntity() instanceof EnderPearl) {
                    final EnderPearl pearl = (EnderPearl)hitEvent.getEntity();
                    if (pearl.getShooter() != null && pearl.getShooter() instanceof Player && this.pearls.containsKey(pearl)) {
                        final Player player2 = (Player)pearl.getShooter();
                        final Location location2 = pearl.getLocation().add(pearl.getVelocity());
                        final Block block = location2.getBlock();
                        if (block.getType() != Material.AIR && !block.getType().name().contains("SIGN")) {
                            final BlockRestore restore = new BlockRestore(player2, location2, block.getType(), block.getData());
                            new BukkitRunnable() {
                                @Override
								public void run() {
                                    restore.restore();
                                }
                            }.runTaskLater(HubGadgetsPlugin.i, 80L);
                            player2.sendBlockChange(location2, Material.STAINED_CLAY, (byte)MathUtils.random.nextInt(15));
                        }
                        this.pearls.remove(pearl);
                    }
                }
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void onFinish(final Player player, final Gadget gadget) {
    }
    
    @Override
    public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
        return true;
    }
    
    public static class BlockRestore
    {
        private final Player player;
        private final Location location;
        private final Material material;
        private final byte data;
        
        @SuppressWarnings("deprecation")
		public void restore() {
            this.player.sendBlockChange(this.location, this.material, this.data);
        }
        
        public BlockRestore(final Player player, final Location location, final Material material, final byte data) {
            super();
            this.player = player;
            this.location = location;
            this.material = material;
            this.data = data;
        }
    }
}