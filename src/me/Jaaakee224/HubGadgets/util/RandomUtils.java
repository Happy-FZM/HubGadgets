package me.Jaaakee224.HubGadgets.util;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.util.Vector;

public final class RandomUtils {
	
    public static final Random random;
    
    static {
        random = new Random(System.nanoTime());
    }
    
    public static Vector getRandomVector() {
        final double x = RandomUtils.random.nextDouble() * 2.0 - 1.0;
        final double y = RandomUtils.random.nextDouble() * 2.0 - 1.0;
        final double z = RandomUtils.random.nextDouble() * 2.0 - 1.0;
        return new Vector(x, y, z).normalize();
    }
    
    public static Vector getRandomCircleVector() {
        final double rnd = RandomUtils.random.nextDouble() * 2.0 * 3.141592653589793;
        final double x = Math.cos(rnd);
        final double z = Math.sin(rnd);
        return new Vector(x, 0.0, z);
    }
    
    public static Material getRandomMaterial(final Material[] materials) {
        return materials[RandomUtils.random.nextInt(materials.length)];
    }
    
    public static double getRandomAngle() {
        return RandomUtils.random.nextDouble() * 2.0 * 3.141592653589793;
    }
}