package me.Jaaakee224.HubGadgets.config;

import java.io.File;
import java.io.IOException;

import me.Jaaakee224.HubGadgets.HubGadgetsPlugin;
import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.util.ItemBuilder;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Gadgets {
	
    @SuppressWarnings("deprecation")
	public static void load(final File file) {
        final FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.options().copyDefaults(true);
        Gadget[] values;
        for (int length = (values = Gadget.values()).length, i = 0; i < length; ++i) {
            final Gadget gadget = values[i];
            final String path = String.valueOf(gadget.name().toLowerCase()) + ".";
            config.addDefault(String.valueOf(path) + "enable", true);
            config.addDefault(String.valueOf(path) + "icon.id", gadget.getItem().getTypeId());
            config.addDefault(String.valueOf(path) + "icon.data", gadget.getItem().getDurability());
            config.addDefault(String.valueOf(path) + "duration", gadget.getDuration());
            config.addDefault(String.valueOf(path) + "cooldown", gadget.getCooldown());
        }
        try {
            config.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for (final String key : config.getKeys(false)) {
            final Gadget gadget2 = HubGadgetsPlugin.getGadget(key);
            if (gadget2 == null) {
                HubGadgetsPlugin.i.getLogger().severe("gadgets.yml | The gadget \"" + key + "\" does not exists!");
            }
            else {
                if (!gadget2.isEnable()) {
                    continue;
                }
                final String path2 = String.valueOf(key) + ".";
                gadget2.setEnable(config.getBoolean(String.valueOf(path2) + "enable"));
                gadget2.setDuration(config.getInt(String.valueOf(path2) + "duration"));
                gadget2.setCooldown(config.getInt(String.valueOf(path2) + "cooldown"));
                final ItemBuilder builder = new ItemBuilder(Material.getMaterial(config.getInt(String.valueOf(path2) + "icon.id")), 1, (short)config.getInt(String.valueOf(path2) + "icon.data")).setTitle(gadget2.getName()).addLores(ChatColor.translateAlternateColorCodes('&', StringUtils.join(Translation.getTranslation("cooldown_string"), ", ").replace("[second]", new StringBuilder(String.valueOf(gadget2.getCooldown())).toString()).replace("[plural]", (gadget2.getCooldown() > 1) ? "s" : "").replace("[plural|upper]", (gadget2.getCooldown() > 1) ? "S" : "")), "§G§A§D§G§E§T");
                String[] split;
                for (int length2 = (split = gadget2.getDescription().split("\n")).length, j = 0; j < length2; ++j) {
                    final String lore = split[j];
                    builder.addLore(lore);
                }
                gadget2.setItem(builder.build());
            }
        }
    }
}