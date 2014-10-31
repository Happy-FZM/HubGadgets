package me.Jaaakee224.HubGadgets.gadget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.MathUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class ParachuteAction implements Gadget.GadgetAction
{
	private static final int CHICKENS = 10;
	private final Map<Player, List<Chicken>> entities;
	private final Map<Player, Chicken> parachutes;

	public ParachuteAction() {
		super();
		this.entities = new HashMap<Player, List<Chicken>>();
		this.parachutes = new HashMap<Player, Chicken>();
	}

	@Override
	public boolean onEvent(final Event event, final Gadget.TriggerAction trigger, final Gadget gadget) {
		final Player player = ((PlayerEvent)event).getPlayer();
		player.setVelocity(player.getVelocity().add(new Vector(0.0, 5.0, 0.0)));
		return true;
	}

	@Override
	public boolean onRun(final Player player, final long ticks, final Gadget gadget) {
		final long start = (Gadget.PARACHUTE.getDuration() == -1) ? 9223372036854775777L : (20 * (Gadget.PARACHUTE.getDuration() - 2));
		if (ticks == start) {
			final World world = player.getWorld();
			Location location = player.getLocation();
			final Chicken bottom = (Chicken)world.spawn(location, (Class)Chicken.class);
			bottom.setVelocity(new Vector(0, 0, 0));
			bottom.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
			bottom.setNoDamageTicks(Integer.MAX_VALUE);
			bottom.setPassenger(player);
			this.parachutes.put(player, bottom);
			location = location.add(0.0, 8.0, 0.0);
			final List<Chicken> entities = new ArrayList<Chicken>();
			for (int i = 0; i < 10; ++i) {
				final Chicken chicken = (Chicken)world.spawn(location.clone().add(MathUtils.random.nextDouble() * 0.2, 0.0, MathUtils.random.nextDouble() * 0.2), (Class)Chicken.class);
				chicken.setVelocity(new Vector(0, 0, 0));
				chicken.setLeashHolder(player);
				chicken.setNoDamageTicks(Integer.MAX_VALUE);
				entities.add(chicken);
			}
			this.entities.put(player, entities);
		}
		else if (ticks < start && ticks % 5L == 0L) {
			final Chicken parachute = this.parachutes.get(player);
			final List<Chicken> entities2 = this.entities.get(player);
			if (entities2 == null || parachute == null || parachute.getPassenger() == null || player.getLocation().subtract(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR) {
				return true;
			}
			for (final Chicken chicken2 : entities2) {
				chicken2.setVelocity(new Vector(0, 0, 0));
			}
		}
		return false;
	}

	@Override
	public void onFinish(final Player player, final Gadget gadget) {
		final List<Chicken> entities = this.entities.remove(player);
		if (entities != null) {
			for (final Chicken entity : entities) {
				if (!entity.isDead()) {
					entity.remove();
				}
			}
		}
		final Chicken parachute = this.parachutes.remove(player);
		if (parachute != null && !parachute.isDead()) {
			parachute.remove();
		}
	}
}