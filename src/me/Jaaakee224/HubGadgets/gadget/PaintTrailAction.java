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

public class PaintTrailAction implements Gadget.GadgetAction {
	private final Map<Player, List<Item>> entities;

	public PaintTrailAction() {
		super();
		this.entities = new HashMap<Player, List<Item>>();
	}

	@Override
	public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
		if (trigger != Gadget.TriggerAction.INTERACT) {
			return false;
		}
		final Player player = ((PlayerEvent)event).getPlayer();
		this.entities.put(player, new ArrayList<Item>());
		return true;
	}

	@Override
	public void onFinish(final Player player, final Gadget gadget) {
		final List<Item> entities = this.entities.remove(player);
		if (entities != null) {
			for (final Item item : entities) {
				if (!item.isDead()) {
					item.remove();
				}
			}
		}
	}

	@Override
	public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
		if (ticks % 4L == 0L) {
			final World world = player.getWorld();
			final List<Item> allEntities = this.entities.get(player);
			if (allEntities == null) {
				return true;
			}
			final List<Item> entities = new ArrayList<Item>();
			for (int i = 0; i < 3; ++i) {
				final Location location = player.getLocation().add(0.0, 2.0, 0.0);
				final Item item = world.dropItem(location, ItemBuilder.getRandomResource(Material.STAINED_CLAY, (short)MathUtils.random.nextInt(15)));

				item.setPickupDelay(Integer.MAX_VALUE);
				item.setVelocity(new Vector(MathUtils.random(-0.1f, 0.1f), 0.4f, MathUtils.random(-0.1f, 0.1f)));
				world.playSound(location, Sound.CHICKEN_EGG_POP, 10, 1);
				entities.add(item);
			}
			allEntities.addAll(entities);
			new BukkitRunnable() {
				@Override
				public void run() {
					for (final Item item : entities) {
						if (!item.isDead()) {
							item.remove();
						}
					}
				}
			}.runTaskLater(HubGadgetsPlugin.i, 60L);
		}
		return false;
	}
}