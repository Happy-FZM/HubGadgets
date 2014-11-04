package me.Jaaakee224.HubGadgets.util;

import net.minecraft.server.v1_7_R3.EntityFireworks;
import net.minecraft.server.v1_7_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_7_R3.World;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkHandler extends EntityFireworks {
	Player[] players = null;

	public FireworkHandler(World world, Player... p) {
		super(world);
		players = p;
		this.a(0.25F, 0.25F);
	}

	boolean gone = false;

	@Override
	public void h() {
		if (gone) {
			return;
		}

		if (!this.world.isStatic) {
			gone = true;

			if (players != null) {
				if (players.length > 0) {
					for (Player player : players) {
						(((CraftPlayer) player).getHandle()).playerConnection.sendPacket(new PacketPlayOutEntityStatus(this, (byte) 17));
					}

					this.die();
					return;
				}
			}

			world.broadcastEntityEffect(this, (byte) 17);
			this.die();
		}
	}

	public static void spawn(Location location, FireworkEffect effect, Player... players) {
		try {
			FireworkHandler firework = new FireworkHandler(((CraftWorld) location.getWorld()).getHandle(), players);
			FireworkMeta meta = ((Firework) firework.getBukkitEntity()).getFireworkMeta();
			meta.addEffect(effect);
			((Firework) firework.getBukkitEntity()).setFireworkMeta(meta);
			firework.setPosition(location.getX(), location.getY(), location.getZ());

			if ((((CraftWorld) location.getWorld()).getHandle()).addEntity(firework)) {
				firework.setInvisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}