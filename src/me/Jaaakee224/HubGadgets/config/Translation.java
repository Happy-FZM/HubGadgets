package me.Jaaakee224.HubGadgets.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.Jaaakee224.HubGadgets.HubGadgetsPlugin;
import me.Jaaakee224.HubGadgets.handler.Gadget;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Translation {
	
    private static Map<String, String> messages;
    
    static {
        Translation.messages = new HashMap<String, String>();
    }
    
    public static FileConfiguration load(final File file) {
        final FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.options().copyDefaults(true);
        config.addDefault("multiple", "&cYou cannot have more than one active effect!");
        config.addDefault("cooldown", "&cYou must wait &4[second] &csecond[plural] before using this!");
        config.addDefault("cooldown_string", "&7COOLDOWN: &b[second] SECOND[plural|upper]");
        config.addDefault("no_permission", "&cYou do not have permission to using this!");
        config.addDefault("target_clean_area", "&cInvalid Location.");
        config.addDefault("unknown_gadget", "&cUnknown gadget! All gadgets: &4[gadgets]&c.");
        config.addDefault("inventory.title", "Gadgets");
        config.addDefault("inventory.prev", "&a< Page [page]");
        config.addDefault("inventory.next", "&aPage [page] >");
        config.addDefault("inventory.clear", "&cClear Gadget");
        config.addDefault("item.title", "&aGadgets &7(Right Click)");
        config.addDefault("item.description", "&7Right click to select a gadget!");
        Gadget[] values;
        for (int length = (values = Gadget.values()).length, i = 0; i < length; ++i) {
            final Gadget gadget = values[i];
            final String path = "gadgets." + gadget.name().toLowerCase() + ".";
            config.addDefault(String.valueOf(path) + "name", gadget.getName().replace("§", "&"));
            config.addDefault(String.valueOf(path) + "description", gadget.getDescription().replace("§", "&"));
        }
        
        try {
            config.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for (final String key : config.getKeys(false)) {
            if (!key.equals("gadgets")) {
                final ConfigurationSection section = config.getConfigurationSection(key);
                final boolean isSection = section != null;
                if (!isSection) {
                    Translation.messages.put(key, ChatColor.translateAlternateColorCodes('&', config.getString(key)));
                }
                else {
                    for (final String value : section.getKeys(false)) {
                        Translation.messages.put(String.valueOf(key) + "." + value, ChatColor.translateAlternateColorCodes('&', section.getString(value)));
                    }
                }
            }
            else {
                for (final String name : config.getConfigurationSection(key).getKeys(false)) {
                    final Gadget gadget2 = HubGadgetsPlugin.getGadget(name);
                    if (gadget2 == null) {
                        HubGadgetsPlugin.i.getLogger().severe(String.valueOf(file.getName()) + " | The gadget \"" + key + "\" does not exists!");
                    }
                    else {
                        if (!gadget2.isEnable()) {
                            continue;
                        }
                        gadget2.setName(ChatColor.translateAlternateColorCodes('&', config.getString(String.valueOf(key) + "." + name + ".name")));
                        gadget2.setDescription(ChatColor.translateAlternateColorCodes('&', config.getString(String.valueOf(key) + "." + name + ".description")));
                    }
                }
            }
        }
        return config;
    }
    
    public static String[] getTranslation(final String key) {
        String value = Translation.messages.get(key);
        if (value == null) {
            value = key;
        }
        return value.split("\n");
    }
}