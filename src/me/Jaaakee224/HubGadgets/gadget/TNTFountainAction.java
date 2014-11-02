package me.Jaaakee224.HubGadgets.gadget;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import me.Jaaakee224.HubGadgets.HubGadgetsPlugin;
import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.ParticleEffect;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TNTFountainAction implements Gadget.GadgetAction {
	private final Map<Player, Location> locations;

	public TNTFountainAction() {
		super();
		this.locations = new HashMap<Player, Location>();
	}

	@Override
	public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
		if (trigger != Gadget.TriggerAction.INTERACT) {
			return false;
		}
		
		final PlayerInteractEvent interactEvent = (PlayerInteractEvent)event;
		final Player player = interactEvent.getPlayer();
		@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
		final Block block = player.getTargetBlock((HashSet)null, 4);
		this.locations.put(player, block.getLocation().add(0.0, 1.5, 0.0));
		return true;
	}

	@Override
	public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
		if (ticks % 5L == 0L) {
			final float x = -0.6f + (float)(Math.random() * 2.2);
			final float z = -0.6f + (float)(Math.random() * 2.2);
			final Location location = this.locations.get(player);
			if (location == null) {
				return true;
			}
			final World world = location.getWorld();
			@SuppressWarnings({ "rawtypes", "unchecked" })
			final TNTPrimed tnt = (TNTPrimed)world.spawn(location, (Class)TNTPrimed.class);
			tnt.setVelocity(new Vector(x, 1.8f, z));
			tnt.setYield(0.0f);
			new BukkitRunnable() {
				@Override
				public void run() {
					final Location location = tnt.getLocation();
					if (!tnt.isDead()) {
						tnt.remove();
						tnt.getWorld().playSound(location, Sound.EXPLODE, 10, 1);
						ParticleEffect.LARGE_EXPLODE.display(location, 0.0f, 0.2f, 0.0f, 0.0f, 1);
					}
					ParticleEffect.SMOKE.display(location, 0.0f, 0.2f, 0.0f, 0.0f, 5);
					ParticleEffect.LAVA.display(location, 0.0f, 0.2f, 0.0f, 0.0f, 5);
				}
			}.runTaskLater(HubGadgetsPlugin.i, 35L);
		}
		return false;
	}

	@Override
	public void onFinish(final Player player, final Gadget gadget) {
		final Location location = this.locations.remove(player);
		if (location != null) {
			location.getWorld().playSound(location, Sound.EXPLODE, 1.0f, 0.5f);
		}
	}
}