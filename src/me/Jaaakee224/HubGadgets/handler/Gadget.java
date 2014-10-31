package me.Jaaakee224.HubGadgets.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.Jaaakee224.HubGadgets.HubGadgetsPlugin;
import me.Jaaakee224.HubGadgets.config.Options;
import me.Jaaakee224.HubGadgets.config.Translation;
import me.Jaaakee224.HubGadgets.gadget.BatBlasterAction;
import me.Jaaakee224.HubGadgets.gadget.CatapultAction;
import me.Jaaakee224.HubGadgets.gadget.CowboyAction;
import me.Jaaakee224.HubGadgets.gadget.CryotubeAction;
import me.Jaaakee224.HubGadgets.gadget.DiscoBallAction;
import me.Jaaakee224.HubGadgets.gadget.DiscoPantsAction;
import me.Jaaakee224.HubGadgets.gadget.FlyingCarpetAction;
import me.Jaaakee224.HubGadgets.gadget.FunCannonAction;
import me.Jaaakee224.HubGadgets.gadget.GhostsAction;
import me.Jaaakee224.HubGadgets.gadget.KawarimiAction;
import me.Jaaakee224.HubGadgets.gadget.PaintGunAction;
import me.Jaaakee224.HubGadgets.gadget.PaintTrailAction;
import me.Jaaakee224.HubGadgets.gadget.ParachuteAction;
import me.Jaaakee224.HubGadgets.gadget.PartyPopperAction;
import me.Jaaakee224.HubGadgets.gadget.PigsFlyAction;
import me.Jaaakee224.HubGadgets.gadget.PoopBombAction;
import me.Jaaakee224.HubGadgets.gadget.PyromaniacAction;
import me.Jaaakee224.HubGadgets.gadget.RailgunAction;
import me.Jaaakee224.HubGadgets.gadget.RainbowAction;
import me.Jaaakee224.HubGadgets.gadget.RocketAction;
import me.Jaaakee224.HubGadgets.gadget.SelfDestructAction;
import me.Jaaakee224.HubGadgets.gadget.SlimevasionAction;
import me.Jaaakee224.HubGadgets.gadget.SuicidalSheepAction;
import me.Jaaakee224.HubGadgets.gadget.TNTFountainAction;
import me.Jaaakee224.HubGadgets.gadget.TeleportStickAction;
import me.Jaaakee224.HubGadgets.gadget.TetherballAction;
import me.Jaaakee224.HubGadgets.gadget.TrampolineAction;
import me.Jaaakee224.HubGadgets.util.LocationUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public enum Gadget implements Listener
{
    ROCKET(ChatColor.GREEN + "Rocket", ChatColor.GRAY + "Fly off to the moon with\n" + ChatColor.GRAY + "this huge rocket.", new ItemStack(Material.FIREWORK), 90, 10, new RocketAction(), Arrays.asList(TriggerAction.INTERACT, TriggerAction.ENTITY_CHANGE_BLOCK)), 
    PARACHUTE(ChatColor.GREEN + "Parachute", ChatColor.GRAY + "Jump into the sky before\n" + ChatColor.GRAY + "open your parachute for a\n" + ChatColor.GRAY + "soft landing.", new ItemStack(Material.LEASH), 60, -1, new ParachuteAction(), Arrays.asList(TriggerAction.INTERACT)), 
    TNTFOUNTAIN(ChatColor.GREEN + "TNT Fountain", ChatColor.GRAY + "Invoke a TNT Fountain.", new ItemStack(Material.TNT), 90, 15, new TNTFountainAction(), Arrays.asList(TriggerAction.INTERACT)), 
    COWBOY(ChatColor.GREEN + "Cowboy", ChatColor.GRAY + "Ride the nearest player\n" + ChatColor.GRAY + "or animal.", new ItemStack(Material.CACTUS), 0, -1, new CowboyAction(), Arrays.asList(TriggerAction.INTERACT_ENTITY)), 
    TETHERBALL(ChatColor.GREEN + "Tetherball", ChatColor.GRAY + "Create a stake with\n" + ChatColor.GRAY + "a ball attached on it,\n" + ChatColor.GRAY + "perfect for quick game.", new ItemStack(Material.FENCE), 90, 45, new TetherballAction(), Arrays.asList(TriggerAction.INTERACT_BLOCK, TriggerAction.BLOCK_BREAK, TriggerAction.HANGING_BREAK)), 
    PYROMANIAC(ChatColor.GREEN + "Pyromaniac", ChatColor.GRAY + "Ignites you for 10 seconds,\n" + ChatColor.GRAY + "before refresh you.", new ItemStack(Material.FLINT_AND_STEEL), 30, 10, new PyromaniacAction(), Arrays.asList(TriggerAction.INTERACT)), 
    TELEPORTSTICK(ChatColor.GREEN + "Teleport Stick", ChatColor.GRAY + "Use the magic stick\n" + ChatColor.GRAY + "to teleport yourself\n" + ChatColor.GRAY + "everywhere in the lobby.", new ItemStack(Material.STICK), 3, 0, new TeleportStickAction(), Arrays.asList(TriggerAction.INTERACT)), 
    RAINBOW(ChatColor.GREEN + "Rainbow", ChatColor.GRAY + "You can now show a\n" + ChatColor.GRAY + "rainbow with the most\n" + ChatColor.GRAY + "beautiful color!", new ItemStack(Material.COOKED_FISH, 1, (short)2), 120, 10, new RainbowAction(), Arrays.asList(TriggerAction.INTERACT)), 
    WHENPIGSFLY(ChatColor.GREEN + "When Pigs Fly", ChatColor.GRAY + "Ride a pig to achieve your\n" + ChatColor.GRAY + "most epic battles!", new ItemStack(Material.SADDLE), 30, 30, new PigsFlyAction(), Arrays.asList(TriggerAction.INTERACT, TriggerAction.ENTITY_DAMAGE, TriggerAction.VEHICLE_EXIT)), 
    CATAPULT(ChatColor.GREEN + "CATapult", ChatColor.GRAY + "Attack your ennemies with\n" + ChatColor.GRAY + "their worst fears, the CATS!\n" + ChatColor.GRAY + "Launches 4-6 exploding cats\n" + ChatColor.GRAY + "in the direction you are\n" + ChatColor.GRAY + "aiming for.", new ItemStack(Material.MONSTER_EGG, 1, (short)98), 60, 1, new CatapultAction(), Arrays.asList(TriggerAction.INTERACT)), 
    RAILGUN(ChatColor.GREEN + "Railgun", ChatColor.GRAY + "Directly coming from QuakeCraft,\n" + ChatColor.GRAY + "this RailGun is ready for\n" + ChatColor.GRAY + "unlimited fire!", new ItemStack(Material.WOOD_HOE), 10, 0, new RailgunAction(), Arrays.asList(TriggerAction.INTERACT)), 
    CRYOTUBE(ChatColor.GREEN + "Cryotube", ChatColor.GRAY + "Protect yourself in a\n" + ChatColor.GRAY + "iceberg, useful to survive\n" + ChatColor.GRAY + "from apocalyspe.", new ItemStack(Material.SNOW_BALL), 45, 10, new CryotubeAction(), Arrays.asList(TriggerAction.INTERACT, TriggerAction.BLOCK_BREAK)), 
    KAWARIMI(ChatColor.GREEN + "Kawarimi no Jutsu", ChatColor.GRAY + "With this ancestral technique,\n" + ChatColor.GRAY + "you can replace your body\n" + ChatColor.GRAY + ChatColor.GRAY + "by a wood block.", new ItemStack(Material.LOG), 45, 10, new KawarimiAction(), Arrays.asList(TriggerAction.INTERACT, TriggerAction.BLOCK_BREAK)), 
    DISCOPANTS(ChatColor.GREEN + "Disco Pants", ChatColor.GRAY + "What is better than random\n" + ChatColor.GRAY + "colored pants?", new ItemStack(Material.LEATHER_LEGGINGS), 60, 30, new DiscoPantsAction(), Arrays.asList(TriggerAction.INTERACT, TriggerAction.INVENTORY_CLICK, TriggerAction.INVENTORY_DROP)), 
    PAINTTRAIL(ChatColor.GREEN + "Paint Trail", ChatColor.GRAY + "Leaves a trail of randomly\n" + ChatColor.GRAY + "colored clay above your\n" + ChatColor.GRAY + "head that disappears\n" + ChatColor.GRAY + "a few seconds later.", new ItemStack(Material.INK_SACK, 1, (short)11), 60, 10, new PaintTrailAction(), Arrays.asList(TriggerAction.INTERACT)), 
    POOPBOMB(ChatColor.GREEN + "Poop Bomb", ChatColor.GRAY + "If the name doesn't say\n" + ChatColor.GRAY + "enough, this is pretty much\n" + ChatColor.GRAY + "just a bomb that explodes\n" + ChatColor.GRAY + "releasing poop everywhere.", new ItemStack(Material.INK_SACK, 1, (short)3), 60, 10, new PoopBombAction(), Arrays.asList(TriggerAction.INTERACT)), 
    PAINTGUN(ChatColor.GREEN + "Paint Gun", ChatColor.GRAY + "Paint the lobby to your\n" + ChatColor.GRAY + "taste with that gun of paint.", new ItemStack(Material.GOLD_BARDING), 10, -1, new PaintGunAction(), Arrays.asList(TriggerAction.INTERACT, TriggerAction.TELEPORT_ENDERPEARL, TriggerAction.PROJECTILE_HIT, TriggerAction.ENTITY_DAMAGE_BY_ENTITY, TriggerAction.HANGING_BREAK_BY_ENTITY)), 
    BATBLASTER(ChatColor.GREEN + "Bat Blaster", ChatColor.GRAY + "Launch a wave of bat\n" + ChatColor.GRAY + "on people you don't like!", new ItemStack(Material.IRON_BARDING), 30, 2, new BatBlasterAction(), Arrays.asList(TriggerAction.INTERACT)), 
    SELFDESTRUCT(ChatColor.GREEN + "Self Destruct", ChatColor.GRAY + "Have you ever wanted to\n" + ChatColor.GRAY + "make you explode and stay\n" + ChatColor.GRAY + "alive to see what happens?", new ItemStack(Material.FIREWORK_CHARGE), 60, 10, new SelfDestructAction(), Arrays.asList(TriggerAction.INTERACT)), 
    SLIMEVASION(ChatColor.GREEN + "Slimevasion", ChatColor.GRAY + "Summon slimes that will\n" + ChatColor.GRAY + "shatter when falling.", new ItemStack(Material.SLIME_BALL), 60, 15, new SlimevasionAction(), Arrays.asList(TriggerAction.INTERACT, TriggerAction.ENTITY_DAMAGE)), 
    TRAMPOLINE(ChatColor.GREEN + "Trampoline", ChatColor.GRAY + "Constructs a trampoline\n" + ChatColor.GRAY + "that sends you into the air.", new ItemStack(Material.HOPPER), 90, 30, new TrampolineAction(), Arrays.asList(TriggerAction.INTERACT, TriggerAction.BLOCK_BREAK)), 
    FUNCANNON(ChatColor.GREEN + "Fun Cannon", ChatColor.GRAY + "Shoot fun things!", new ItemStack(Material.BLAZE_ROD), 10, -1, new FunCannonAction(), Arrays.asList(TriggerAction.INTERACT, TriggerAction.TELEPORT_ENDERPEARL, TriggerAction.PROJECTILE_HIT, TriggerAction.ENTITY_DAMAGE_BY_ENTITY, TriggerAction.HANGING_BREAK_BY_ENTITY)), 
    SUICIDALSHEEP(ChatColor.GREEN + "Suicidal Sheep", ChatColor.GRAY + "A flying sheep that explodes\n" + ChatColor.GRAY + "after a few seconds. What\n" + ChatColor.GRAY + "could be more normal?", new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData()), 60, 13, new SuicidalSheepAction(), Arrays.asList(TriggerAction.INTERACT)), 
    GHOSTS(ChatColor.GREEN + "Ghosts", ChatColor.GRAY + "Scare your friends with\n" + ChatColor.GRAY + "these ghosts!", new ItemStack(Material.SKULL_ITEM), 60, 20, new GhostsAction(), Arrays.asList(TriggerAction.INTERACT)), 
    FLYINGCARPET(ChatColor.GREEN + "Flying Carpet", ChatColor.GRAY + "Become a wizard with\n" + ChatColor.GRAY + "this flying carpet!", new ItemStack(Material.CARPET, 1, DyeColor.CYAN.getWoolData()), 90, 45, new FlyingCarpetAction(), Arrays.asList(TriggerAction.INTERACT, TriggerAction.ENTITY_CHANGE_BLOCK)),
    PARTYPOPPER(ChatColor.GREEN + "Party Popper", ChatColor.GRAY + "The party has just\n" + ChatColor.GRAY + "begun!", new ItemStack(Material.ENDER_CHEST), 60, 20, new PartyPopperAction(), Arrays.asList(TriggerAction.INTERACT)),
    DISCOBALL(ChatColor.GREEN + "Disco Ball", ChatColor.GRAY + "Dance to the\n" + ChatColor.GRAY + "beat!", new ItemStack(Material.STAINED_GLASS, 1, (short) 11), 60, 20, new DiscoBallAction(), Arrays.asList(TriggerAction.INTERACT, TriggerAction.BLOCK_BREAK));
    
    
    private boolean enable;
    private String name;
    private String description;
    private ItemStack item;
    private int cooldown;
    private int duration;
    private GadgetAction action;
    private List<TriggerAction> triggers;
    public static Map<Player, Gadget> players;
    public static Map<Player, Long> cooldowns;
    
    static {
        Gadget.players = new HashMap<Player, Gadget>();
        Gadget.cooldowns = new HashMap<Player, Long>();
    }
    
    public static boolean checkEmptyArea(final Location corner1, final Location corner2) {
        if (corner1.getWorld() != corner2.getWorld()) {
            return false;
        }
        final World world = corner1.getWorld();
        for (int x = corner1.getBlockX(); x <= corner2.getBlockX(); ++x) {
            for (int y = corner1.getBlockY(); y <= corner2.getBlockY(); ++y) {
                for (int z = corner1.getBlockZ(); z <= corner2.getBlockZ(); ++z) {
                    final Location location = new Location(world, x, y, z);
                    final Block block = location.getBlock();
                    if (block.getType() != Material.AIR) {
                        return false;
                    }
                    Entity[] nearbyEntities;
                    for (int length = (nearbyEntities = LocationUtils.getNearbyEntities(location, 2)).length, i = 0; i < length; ++i) {
                        final Entity entity = nearbyEntities[i];
                        if (entity instanceof ItemFrame || entity instanceof Painting) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    public static boolean checkTopBlocks(Location location, final int height, final Material material) {
        location = location.clone();
        for (int i = 0; i < height; ++i) {
            final Block block = location.getBlock();
            if (block.getType() != material) {
                return false;
            }
            Entity[] nearbyEntities;
            for (int length = (nearbyEntities = LocationUtils.getNearbyEntities(location, 2)).length, j = 0; j < length; ++j) {
                final Entity entity = nearbyEntities[j];
                if (entity instanceof ItemFrame || entity instanceof Painting) {
                    return false;
                }
            }
            location.add(0.0, 1.0, 0.0);
        }
        return true;
    }
    
    public static boolean checkEmptyBlocks(final Location location, final int height) {
        return checkTopBlocks(location, height, Material.AIR);
    }
    
    public static long getCooldown(final Player player, final Gadget gadget) {
        if (gadget.cooldown <= 0) {
            return 0L;
        }
        Long result = Gadget.cooldowns.get(player);
        if (result != null) {
            result = gadget.cooldown - (System.currentTimeMillis() - result) / 1000L;
        }
        else {
            result = 0L;
        }
        return result;
    }
    
    public static Gadget getPlayerGadget(final Player player) {
        return Gadget.players.get(player);
    }
    
    private Gadget(final String name, final String description, final ItemStack icon, final int cooldown, final int duration, final GadgetAction action, final List<TriggerAction> triggers) {
        this(true, name, description, icon, cooldown, duration, action, triggers);
    }
    
    private Gadget(final boolean enable, final String name, final String description, final ItemStack item, final int cooldown, final int duration, final GadgetAction action, final List<TriggerAction> triggers) {
        this.enable = enable;
        this.name = name;
        this.description = description;
        this.item = item;
        this.cooldown = cooldown;
        this.duration = duration;
        this.action = action;
        this.triggers = triggers;
        Bukkit.getPluginManager().registerEvents(this, HubGadgetsPlugin.i);
    }
    
    public boolean isEnable() {
        return this.enable;
    }
    
    public void setEnable(final boolean enable) {
        this.enable = enable;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public ItemStack getItem() {
        return this.item;
    }
    
    public void setItem(final ItemStack item) {
        this.item = item;
    }
    
    public int getCooldown() {
        return this.cooldown;
    }
    
    public void setCooldown(final int cooldown) {
        this.cooldown = cooldown;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    public void setDuration(final int duration) {
        this.duration = duration;
    }
    
    public GadgetAction getAction() {
        return this.action;
    }
    
    public void setAction(final GadgetAction action) {
        this.action = action;
    }
    
    public List<TriggerAction> getTriggers() {
        return this.triggers;
    }
    
    public void setTriggers(final List<TriggerAction> triggers) {
        this.triggers = triggers;
    }
    
    public static Map<Player, Gadget> getPlayers() {
        return Gadget.players;
    }
    
    public static void setPlayers(final Map<Player, Gadget> players) {
        Gadget.players = players;
    }
    
    public static Map<Player, Long> getCooldowns() {
        return Gadget.cooldowns;
    }
    
    public static void setCooldowns(final Map<Player, Long> cooldowns) {
        Gadget.cooldowns = cooldowns;
    }
    
    public void afterExec(final Player player) {
        Gadget.players.put(player, this);
        Gadget.cooldowns.put(player, System.currentTimeMillis());
        if (this.duration == 0) {
            this.action.onRun(player, 0L, this);
            this.finish(player);
        }
        else {
            new BukkitRunnable() {
                private long maxTicks = (Gadget.this.duration == -1) ? Long.MAX_VALUE : (Gadget.this.duration * 20L);
                
                @Override
				public void run() {
                    try {
                        if (!Gadget.players.containsKey(player)) {
                            this.cancel();
                            return;
                        }
                        if (this.maxTicks == 0L || Gadget.this.action.onRun(player, this.maxTicks, Gadget.this)) {
                            this.cancel();
                            Gadget.this.finish(player);
                            return;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    finally {
                        --this.maxTicks;
                    }
                    --this.maxTicks;
                }
            }.runTaskTimer(HubGadgetsPlugin.i, 0L, 1L);
        }
    }
    
    public void finish(final Player player) {
        Gadget.players.remove(player);
        this.action.onFinish(player, this);
    }
    
    public boolean check(final Player player, final boolean cooldown) {
        if (!player.hasPermission("gadgets.gadget." + ChatColor.stripColor(this.name().toLowerCase()))) {
            player.sendMessage(Translation.getTranslation("no_permission"));
            return false;
        }
        if (cooldown) {
            if (player.hasPermission("gadgets.cooldown.bypass") && Gadget.players.containsKey(player)) {
                player.sendMessage(Translation.getTranslation("multiple"));
                return false;
            }
            final long seconds = getCooldown(player, this);
            if (!player.hasPermission("gadgets.cooldown.bypass") && seconds > 0L) {
                String[] translation;
                for (int length = (translation = Translation.getTranslation("cooldown")).length, i = 0; i < length; ++i) {
                    final String msg = translation[i];
                    player.sendMessage(msg.replace("[second]", new StringBuilder(String.valueOf(seconds)).toString()).replace("[plural]", (seconds > 1L) ? "s" : ""));
                }
                return false;
            }
        }
        return true;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (this.triggers.contains(TriggerAction.BLOCK_BREAK) && this.action.onEvent(event, TriggerAction.BLOCK_BREAK, this)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (this.triggers.contains(TriggerAction.ENTITY_DAMAGE) && this.action.onEvent(event, TriggerAction.ENTITY_DAMAGE, this)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (this.triggers.contains(TriggerAction.ENTITY_DAMAGE_BY_ENTITY) && this.action.onEvent(event, TriggerAction.ENTITY_DAMAGE_BY_ENTITY, this)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (this.triggers.contains(TriggerAction.ENTITY_CHANGE_BLOCK) && this.action.onEvent(event, TriggerAction.ENTITY_CHANGE_BLOCK, this)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (this.triggers.contains(TriggerAction.ENTITY_EXPLODE) && this.action.onEvent(event, TriggerAction.ENTITY_EXPLODE, this)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHangingBreak(final HangingBreakEvent event) {
        if (this.triggers.contains(TriggerAction.HANGING_BREAK) && this.action.onEvent(event, TriggerAction.HANGING_BREAK, this)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHangingBreakByEntity(final HangingBreakByEntityEvent event) {
        if (this.triggers.contains(TriggerAction.HANGING_BREAK_BY_ENTITY) && this.action.onEvent(event, TriggerAction.HANGING_BREAK_BY_ENTITY, this)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(final InventoryClickEvent event) {
        if (this.triggers.contains(TriggerAction.INVENTORY_CLICK) && this.action.onEvent(event, TriggerAction.INVENTORY_CLICK, this)) {
            event.setCancelled(true);
        }
        else if (this.item.isSimilar(event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        if (this.triggers.contains(TriggerAction.INVENTORY_DROP) && this.action.onEvent(event, TriggerAction.INVENTORY_DROP, this)) {
            event.setCancelled(true);
        }
        else if (this.item.isSimilar(event.getItemDrop().getItemStack())) {
            event.getItemDrop().remove();
            event.getPlayer().getInventory().setItem(Options.getGadgets().getSlot(), this.item);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (this.triggers.contains(TriggerAction.TELEPORT_ENDERPEARL) && event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && this.action.onEvent(event, TriggerAction.TELEPORT_ENDERPEARL, this)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onVehicleExit(final VehicleExitEvent event) {
        if (this.triggers.contains(TriggerAction.VEHICLE_EXIT) && event.getExited() instanceof Player && this.action.onEvent(event, TriggerAction.VEHICLE_EXIT, this)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileHit(final ProjectileHitEvent event) {
        if (this.triggers.contains(TriggerAction.PROJECTILE_HIT) && event.getEntity() instanceof Projectile) {
            final Projectile projectile = event.getEntity();
            if (projectile.getShooter() != null && projectile.getShooter() instanceof Player) {
                this.action.onEvent(event, TriggerAction.PROJECTILE_HIT, this);
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if ((this.triggers.contains(TriggerAction.INTERACT) || this.triggers.contains(TriggerAction.INTERACT_BLOCK)) && event.getAction().name().startsWith("RIGHT_CLICK") && event.hasItem() && event.getItem().hasItemMeta() && event.getItem().getItemMeta().getDisplayName().equals(this.name)) {
            event.setCancelled(true);
            event.setUseItemInHand(Event.Result.DENY);
            event.setUseInteractedBlock(Event.Result.DENY);
            final Player player = event.getPlayer();
            player.updateInventory();
            if (this.check(player, true) && this.action.onEvent(event, (event.getAction().name().contains("BLOCK") && this.triggers.contains(TriggerAction.INTERACT_BLOCK)) ? TriggerAction.INTERACT_BLOCK : TriggerAction.INTERACT, this)) {
                this.afterExec(player);
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = player.getItemInHand();
        if (this.triggers.contains(TriggerAction.INTERACT_ENTITY) && item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(this.name)) {
            event.setCancelled(true);
            if (this.check(player, true) && this.action.onEvent(event, TriggerAction.INTERACT_ENTITY, this)) {
                this.afterExec(player);
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (this.triggers.contains(TriggerAction.PLAYER_QUIT)) {
            this.action.onEvent(event, TriggerAction.PLAYER_QUIT, this);
        }
        if (Gadget.players.get(player) == this) {
            this.finish(player);
        }
    }
    
    public enum TriggerAction
    {
        INTERACT(), 
        INTERACT_BLOCK(), 
        INTERACT_ENTITY(), 
        PLAYER_QUIT(), 
        BLOCK_BREAK(), 
        ENTITY_DAMAGE(), 
        ENTITY_DAMAGE_BY_ENTITY(), 
        HANGING_BREAK_BY_ENTITY(), 
        HANGING_BREAK(), 
        INVENTORY_DROP(), 
        INVENTORY_CLICK(), 
        TELEPORT_ENDERPEARL(), 
        PROJECTILE_HIT(), 
        ENTITY_CHANGE_BLOCK(), 
        ENTITY_EXPLODE(), 
        VEHICLE_EXIT();
    }
    
    public interface GadgetAction
    {
        boolean onEvent(Event p0, TriggerAction p1, Gadget p2);
        
        boolean onRun(Player p0, long p1, Gadget p2);
        
        void onFinish(Player p0, Gadget p1);
    }
}