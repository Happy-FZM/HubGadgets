package me.Jaaakee224.HubGadgets.util;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class RandomFireworkHandler {

	public static void spawnRandomFirework(final Location loc) {
		final Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		final FireworkMeta fireworkMeta = firework.getFireworkMeta();
		final Random random = new Random();
		final FireworkEffect effect = FireworkEffect.builder().flicker(random.nextBoolean()).withColor(getColor(random.nextInt(14) + 1)).withFade(getColor(random.nextInt(14) + 1)).with(Type.values()[random.nextInt(Type.values().length)]).trail(random.nextBoolean()).build();
		fireworkMeta.addEffect(effect);
		fireworkMeta.setPower(random.nextInt(2) + 1);
		firework.setFireworkMeta(fireworkMeta);
	}

	private static Color getColor(final int i) {
		switch (i) {
		case 1:
			return Color.AQUA;
		case 2:
			return Color.BLUE;
		case 3:
			return Color.FUCHSIA;
		case 4:
			return Color.GREEN;
		case 5:
			return Color.LIME;
		case 6:
			return Color.MAROON;
		case 7:
			return Color.NAVY;
		case 8:
			return Color.ORANGE;
		case 9:
			return Color.PURPLE;
		case 10:
			return Color.RED;
		case 11:
			return Color.SILVER;
		case 12:
			return Color.TEAL;
		case 13:
			return Color.WHITE;
		case 14:
			return Color.YELLOW;
		}
		return null;
	}
}