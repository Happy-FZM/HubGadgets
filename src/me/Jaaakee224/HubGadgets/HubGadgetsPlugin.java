package me.Jaaakee224.HubGadgets;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.config.Gadgets;
import me.Jaaakee224.HubGadgets.config.Options;
import me.Jaaakee224.HubGadgets.config.Translation;
import me.Jaaakee224.HubGadgets.event.inventory.InventoryClick;
import me.Jaaakee224.HubGadgets.event.player.PlayerDropItem;
import me.Jaaakee224.HubGadgets.event.player.PlayerInteract;
import me.Jaaakee224.HubGadgets.event.player.PlayerJoin;
import me.Jaaakee224.HubGadgets.handler.Gadget;
import me.Jaaakee224.HubGadgets.handler.Menu;
import me.Jaaakee224.HubGadgets.menu.GadgetsMenuAction;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class HubGadgetsPlugin extends JavaPlugin {

	public static HubGadgetsPlugin i;
	public Menu menu;

	public static Gadget getGadget(final String name) {
		Gadget[] values;
		for (int length = (values = Gadget.values()).length, i = 0; i < length; ++i) {
			final Gadget gadget = values[i];
			if (gadget.name().contains(name.toUpperCase())) {
				return gadget;
			}
		}
		return null;
	}

	@Override
	public void onEnable() {
		i = this;
		Options.load(new File(this.getDataFolder(), "options.yml"));
		Gadgets.load(new File(this.getDataFolder(), "gadgets.yml"));
		this.menu = new Menu(StringUtils.join(Translation.getTranslation("inventory.title"), ", "), this.getMenuItems(), new GadgetsMenuAction());
		Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDropItem(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
	}

	@Override
	public void onDisable() {
		for (final Map.Entry<Player, Gadget> entry : Gadget.players.entrySet()) {
			entry.getValue().finish(entry.getKey());
		}
		i = null;
		Gadget.players = null;
		Gadget.cooldowns = null;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (!command.getName().equals("gadget")) {
			return false;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player!");
			return true;
		}
		final Player player = (Player)sender;
		if (args.length == 0) {
			this.menu.open(player, 1);
		}
		else if (args[0].equalsIgnoreCase("clear")) {
			final Gadget gadget = Gadget.getPlayerGadget(player);
			if (gadget != null) {
				gadget.finish(player);
			}
			player.getInventory().setItem(Options.getGadgets().getSlot(), (ItemStack)null);
		}
		else {
			final String name = args[0].toUpperCase();
			if (name.equalsIgnoreCase("use") && args.length == 2) {
				final Gadget gadget2 = getGadget(args[1]);
				if (gadget2 != null && gadget2.isEnable() && gadget2.check(player, true) && gadget2.getAction().onEvent(new PlayerInteractEvent(player, Action.LEFT_CLICK_AIR, gadget2.getItem(), gadget2.getTriggers().contains(Gadget.TriggerAction.INTERACT_BLOCK) ? player.getTargetBlock((HashSet)null, 4) : null, gadget2.getTriggers().contains(Gadget.TriggerAction.INTERACT_BLOCK) ? BlockFace.SELF : null), gadget2.getTriggers().contains(Gadget.TriggerAction.INTERACT_BLOCK) ? Gadget.TriggerAction.INTERACT_BLOCK : Gadget.TriggerAction.INTERACT, gadget2)) {
					gadget2.afterExec(player);
				}
				else if (gadget2 == null || !gadget2.isEnable()) {
					String[] translation;
					for (int length = (translation = Translation.getTranslation("unknown_gadget")).length, i = 0; i < length; ++i) {
						final String msg = translation[i];
						player.sendMessage(msg.replace("[Gadgets]", this.getGadgetsList()));
					}
				}
			}
			else {
				final Gadget gadget2 = getGadget(name);
				if (gadget2 != null && gadget2.isEnable()) {
					if (!player.hasPermission("gadgets.gadget." + ChatColor.stripColor(gadget2.name().replace("_", "").toLowerCase()))) {
						player.sendMessage(Translation.getTranslation("no_permission"));
						return true;
					}
					player.getInventory().setItem(Options.getGadgets().getSlot(), gadget2.getItem());
				}
				else {
					String[] translation2;
					for (int length2 = (translation2 = Translation.getTranslation("unknown_gadget")).length, j = 0; j < length2; ++j) {
						final String msg = translation2[j];
						player.sendMessage(msg.replace("[Gadgets]", this.getGadgetsList()));
					}
				}
			}
		}
		return true;
	}

	private String getGadgetsList() {
		String gadgets = "";
		Gadget[] values;
		for (int length = (values = Gadget.values()).length, i = 0; i < length; ++i) {
			final Gadget g = values[i];
			gadgets = gadgets + g.name().toLowerCase() + ", ";
		}
		gadgets = gadgets.substring(0, gadgets.length() - 2);
		return gadgets;
	}

	private ItemStack[] getMenuItems() {
		int ordinal = 0;
		final ItemStack[] items = new ItemStack[Gadget.values().length];
		Gadget[] values;
		for (int length = (values = Gadget.values()).length, i = 0; i < length; ++i) {
			final Gadget gadget = values[i];
			if (gadget.isEnable()) {
				items[ordinal] = gadget.getItem();
				++ordinal;
			}
		}
		return items;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (command.getName().equals("gadget")) {
			final List<String> names = new ArrayList<String>();
			Gadget[] values;
			for (int length = (values = Gadget.values()).length, i = 0; i < length; ++i) {
				final Gadget gadget = values[i];
				if (gadget.isEnable()) {
					names.add(ChatColor.stripColor(gadget.name()).toLowerCase().replace("_", ""));
				}
			}
			return names;
		}
		return null;
	}
}