package me.Jaaakee224.HubGadgets.util;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class LocationUtils {
	
    public static Entity[] getNearbyEntities(final Location location, final int radius) {
        final int chunkRadius = (radius < 16) ? 1 : ((radius - radius % 16) / 16);
        final HashSet<Entity> radiusEntities = new HashSet<Entity>();
        for (int chX = 0 - chunkRadius; chX <= chunkRadius; ++chX) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; ++chZ) {
                final int x = (int)location.getX();
                final int y = (int)location.getY();
                final int z = (int)location.getZ();
                Entity[] entities;
                for (int length = (entities = new Location(location.getWorld(), x + chX * 16, y, z + chZ * 16).getChunk().getEntities()).length, i = 0; i < length; ++i) {
                    final Entity entity = entities[i];
                    if (entity.getLocation().distanceSquared(location) <= radius && entity.getLocation().getBlock() != location.getBlock()) {
                        radiusEntities.add(entity);
                    }
                }
            }
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }
}