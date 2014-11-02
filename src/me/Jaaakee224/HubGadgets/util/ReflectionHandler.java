package me.Jaaakee224.HubGadgets.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

public final class ReflectionHandler {
    public static Class<?> getClass(final String name, final PackageType type) throws Exception {
        return Class.forName(type + "." + name);
    }
    
    public static Class<?> getClass(final String name, final SubPackageType type) throws Exception {
        return Class.forName(type + "." + name);
    }
    
    public static Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... parameterTypes) {
        @SuppressWarnings("rawtypes")
		final Class[] p = DataType.convertToPrimitive(parameterTypes);
        Constructor<?>[] constructors;
        for (int length = (constructors = clazz.getConstructors()).length, i = 0; i < length; ++i) {
            final Constructor<?> c = constructors[i];
            if (DataType.equalsArray(DataType.convertToPrimitive(c.getParameterTypes()), p)) {
                return c;
            }
        }
        return null;
    }
    
    public static Constructor<?> getConstructor(final String className, final PackageType type, final Class<?>... parameterTypes) throws Exception {
        return getConstructor(getClass(className, type), parameterTypes);
    }
    
    public static Constructor<?> getConstructor(final String className, final SubPackageType type, final Class<?>... parameterTypes) throws Exception {
        return getConstructor(getClass(className, type), parameterTypes);
    }
    
    public static Object newInstance(final Class<?> clazz, final Object... args) throws Exception {
        return getConstructor(clazz, DataType.convertToPrimitive(args)).newInstance(args);
    }
    
    public static Object newInstance(final String className, final PackageType type, final Object... args) throws Exception {
        return newInstance(getClass(className, type), args);
    }
    
    public static Object newInstance(final String className, final SubPackageType type, final Object... args) throws Exception {
        return newInstance(getClass(className, type), args);
    }
    
    public static Method getMethod(final Class<?> clazz, final String name, final Class<?>... parameterTypes) {
        @SuppressWarnings("rawtypes")
		final Class[] p = DataType.convertToPrimitive(parameterTypes);
        Method[] methods;
        for (int length = (methods = clazz.getMethods()).length, i = 0; i < length; ++i) {
            final Method m = methods[i];
            if (m.getName().equals(name) && DataType.equalsArray(DataType.convertToPrimitive(m.getParameterTypes()), p)) {
                return m;
            }
        }
        return null;
    }
    
    public static Method getMethod(final String className, final PackageType type, final String name, final Class<?>... parameterTypes) throws Exception {
        return getMethod(getClass(className, type), name, parameterTypes);
    }
    
    public static Method getMethod(final String className, final SubPackageType type, final String name, final Class<?>... parameterTypes) throws Exception {
        return getMethod(getClass(className, type), name, parameterTypes);
    }
    
    public static Object invokeMethod(final String name, final Object instance, final Object... args) throws Exception {
        return getMethod(instance.getClass(), name, DataType.convertToPrimitive(args)).invoke(instance, args);
    }
    
    public static Object invokeMethod(final Class<?> clazz, final String name, final Object instance, final Object... args) throws Exception {
        return getMethod(clazz, name, DataType.convertToPrimitive(args)).invoke(instance, args);
    }
    
    public static Object invokeMethod(final String className, final PackageType type, final String name, final Object instance, final Object... args) throws Exception {
        return invokeMethod(getClass(className, type), name, instance, args);
    }
    
    public static Object invokeMethod(final String className, final SubPackageType type, final String name, final Object instance, final Object... args) throws Exception {
        return invokeMethod(getClass(className, type), name, instance, args);
    }
    
    public static Field getField(final Class<?> clazz, final String name) throws Exception {
        final Field f = clazz.getField(name);
        f.setAccessible(true);
        return f;
    }
    
    public static Field getField(final String className, final PackageType type, final String name) throws Exception {
        return getField(getClass(className, type), name);
    }
    
    public static Field getField(final String className, final SubPackageType type, final String name) throws Exception {
        return getField(getClass(className, type), name);
    }
    
    public static Field getDeclaredField(final Class<?> clazz, final String name) throws Exception {
        final Field f = clazz.getDeclaredField(name);
        f.setAccessible(true);
        return f;
    }
    
    public static Field getDeclaredField(final String className, final PackageType type, final String name) throws Exception {
        return getDeclaredField(getClass(className, type), name);
    }
    
    public static Field getDeclaredField(final String className, final SubPackageType type, final String name) throws Exception {
        return getDeclaredField(getClass(className, type), name);
    }
    
    public static Object getValue(final Object instance, final String fieldName) throws Exception {
        return getField(instance.getClass(), fieldName).get(instance);
    }
    
    public static Object getValue(final Class<?> clazz, final Object instance, final String fieldName) throws Exception {
        return getField(clazz, fieldName).get(instance);
    }
    
    public static Object getValue(final String className, final PackageType type, final Object instance, final String fieldName) throws Exception {
        return getValue(getClass(className, type), instance, fieldName);
    }
    
    public static Object getValue(final String className, final SubPackageType type, final Object instance, final String fieldName) throws Exception {
        return getValue(getClass(className, type), instance, fieldName);
    }
    
    public static Object getDeclaredValue(final Object instance, final String fieldName) throws Exception {
        return getDeclaredField(instance.getClass(), fieldName).get(instance);
    }
    
    public static Object getDeclaredValue(final Class<?> clazz, final Object instance, final String fieldName) throws Exception {
        return getDeclaredField(clazz, fieldName).get(instance);
    }
    
    public static Object getDeclaredValue(final String className, final PackageType type, final Object instance, final String fieldName) throws Exception {
        return getDeclaredValue(getClass(className, type), instance, fieldName);
    }
    
    public static Object getDeclaredValue(final String className, final SubPackageType type, final Object instance, final String fieldName) throws Exception {
        return getDeclaredValue(getClass(className, type), instance, fieldName);
    }
    
    public static void setValue(final Object instance, final String fieldName, final Object fieldValue) throws Exception {
        final Field f = getField(instance.getClass(), fieldName);
        f.set(instance, fieldValue);
    }
    
    public static void setValue(final Object instance, final FieldPair pair) throws Exception {
        setValue(instance, pair.getName(), pair.getValue());
    }
    
    public static void setValue(final Class<?> clazz, final Object instance, final String fieldName, final Object fieldValue) throws Exception {
        final Field f = getField(clazz, fieldName);
        f.set(instance, fieldValue);
    }
    
    public static void setValue(final Class<?> clazz, final Object instance, final FieldPair pair) throws Exception {
        setValue(clazz, instance, pair.getName(), pair.getValue());
    }
    
    public static void setValue(final String className, final PackageType type, final Object instance, final String fieldName, final Object fieldValue) throws Exception {
        setValue(getClass(className, type), instance, fieldName, fieldValue);
    }
    
    public static void setValue(final String className, final PackageType type, final Object instance, final FieldPair pair) throws Exception {
        setValue(className, type, instance, pair.getName(), pair.getValue());
    }
    
    public static void setValue(final String className, final SubPackageType type, final Object instance, final String fieldName, final Object fieldValue) throws Exception {
        setValue(getClass(className, type), instance, fieldName, fieldValue);
    }
    
    public static void setValue(final String className, final SubPackageType type, final Object instance, final FieldPair pair) throws Exception {
        setValue(className, type, instance, pair.getName(), pair.getValue());
    }
    
    public static void setValues(final Object instance, final FieldPair... pairs) throws Exception {
        for (final FieldPair pair : pairs) {
            setValue(instance, pair);
        }
    }
    
    public static void setValues(final Class<?> clazz, final Object instance, final FieldPair... pairs) throws Exception {
        for (final FieldPair pair : pairs) {
            setValue(clazz, instance, pair);
        }
    }
    
    public static void setValues(final String className, final PackageType type, final Object instance, final FieldPair... pairs) throws Exception {
        setValues(getClass(className, type), instance, pairs);
    }
    
    public static void setValues(final String className, final SubPackageType type, final Object instance, final FieldPair... pairs) throws Exception {
        setValues(getClass(className, type), instance, pairs);
    }
    
    public static void setDeclaredValue(final Object instance, final String fieldName, final Object fieldValue) throws Exception {
        final Field f = getDeclaredField(instance.getClass(), fieldName);
        f.set(instance, fieldValue);
    }
    
    public static void setDeclaredValue(final Object instance, final FieldPair pair) throws Exception {
        setDeclaredValue(instance, pair.getName(), pair.getValue());
    }
    
    public static void setDeclaredValue(final Class<?> clazz, final Object instance, final String fieldName, final Object fieldValue) throws Exception {
        final Field f = getDeclaredField(clazz, fieldName);
        f.set(instance, fieldValue);
    }
    
    public static void setDeclaredValue(final Class<?> clazz, final Object instance, final FieldPair pair) throws Exception {
        setDeclaredValue(clazz, instance, pair.getName(), pair.getValue());
    }
    
    public static void setDeclaredValue(final String className, final PackageType type, final Object instance, final String fieldName, final Object fieldValue) throws Exception {
        setDeclaredValue(getClass(className, type), instance, fieldName, fieldValue);
    }
    
    public static void setDeclaredValue(final String className, final PackageType type, final Object instance, final FieldPair pair) throws Exception {
        setDeclaredValue(className, type, instance, pair.getName(), pair.getValue());
    }
    
    public static void setDeclaredValue(final String className, final SubPackageType type, final Object instance, final String fieldName, final Object fieldValue) throws Exception {
        setDeclaredValue(getClass(className, type), instance, fieldName, fieldValue);
    }
    
    public static void setDeclaredValue(final String className, final SubPackageType type, final Object instance, final FieldPair pair) throws Exception {
        setDeclaredValue(className, type, instance, pair.getName(), pair.getValue());
    }
    
    public static void setDeclaredValues(final Object instance, final FieldPair... pairs) throws Exception {
        for (final FieldPair pair : pairs) {
            setDeclaredValue(instance, pair);
        }
    }
    
    public static void setDeclaredValues(final Class<?> clazz, final Object instance, final FieldPair... pairs) throws Exception {
        for (final FieldPair pair : pairs) {
            setDeclaredValue(clazz, instance, pair);
        }
    }
    
    public static void setDeclaredValues(final String className, final PackageType type, final Object instance, final FieldPair... pairs) throws Exception {
        setDeclaredValues(getClass(className, type), instance, pairs);
    }
    
    public static void setDeclaredValues(final String className, final SubPackageType type, final Object instance, final FieldPair... pairs) throws Exception {
        setDeclaredValues(getClass(className, type), instance, pairs);
    }
    
    public enum DataType
    {
        BYTE(Byte.TYPE, Byte.class), 
        SHORT(Short.TYPE, Short.class), 
        INTEGER(Integer.TYPE, Integer.class), 
        LONG(Long.TYPE, Long.class), 
        CHARACTER(Character.TYPE, Character.class), 
        FLOAT(Float.TYPE, Float.class), 
        DOUBLE(Double.TYPE, Double.class), 
        BOOLEAN(Boolean.TYPE, Boolean.class);
        
        private static final Map<Class<?>, DataType> CLASS_MAP;
        private final Class<?> primitive;
        private final Class<?> reference;
        
        static {
            CLASS_MAP = new HashMap<Class<?>, DataType>();
            DataType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final DataType t = values[i];
                DataType.CLASS_MAP.put(t.primitive, t);
                DataType.CLASS_MAP.put(t.reference, t);
            }
        }
        
        private DataType(final Class<?> primitive, final Class<?> reference) {
            this.primitive = primitive;
            this.reference = reference;
        }
        
        public Class<?> getPrimitive() {
            return this.primitive;
        }
        
        public Class<?> getReference() {
            return this.reference;
        }
        
        public static DataType fromClass(final Class<?> c) {
            return DataType.CLASS_MAP.get(c);
        }
        
        public static Class<?> getPrimitive(final Class<?> c) {
            final DataType t = fromClass(c);
            return (t == null) ? c : t.getPrimitive();
        }
        
        public static Class<?> getReference(final Class<?> c) {
            final DataType t = fromClass(c);
            return (t == null) ? c : t.getReference();
        }
        
        public static Class<?>[] convertToPrimitive(final Class<?>[] classes) {
            final int length = (classes == null) ? 0 : classes.length;
            @SuppressWarnings("rawtypes")
			final Class[] types = new Class[length];
            for (int i = 0; i < length; ++i) {
                types[i] = getPrimitive(classes[i]);
            }
            return types;
        }
        
        public static Class<?>[] convertToPrimitive(final Object[] objects) {
            final int length = (objects == null) ? 0 : objects.length;
            @SuppressWarnings("rawtypes")
			final Class[] types = new Class[length];
            for (int i = 0; i < length; ++i) {
                types[i] = getPrimitive(objects[i].getClass());
            }
            return types;
        }
        
        public static boolean equalsArray(final Class<?>[] a1, final Class<?>[] a2) {
            if (a1 == null || a2 == null || a1.length != a2.length) {
                return false;
            }
            for (int i = 0; i < a1.length; ++i) {
                if (!a1[i].equals(a2[i]) && !a1[i].isAssignableFrom(a2[i])) {
                    return false;
                }
            }
            return true;
        }
    }
    
    public enum PackageType
    {
        MINECRAFT_SERVER("MINECRAFT_SERVER", 0, "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().substring(23)), 
        CRAFTBUKKIT("CRAFTBUKKIT", 1, Bukkit.getServer().getClass().getPackage().getName());
        
        private final String name;
        
        private PackageType(final String s, final int n, final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
    
    public enum PacketType
    {
        HANDSHAKING_IN_SET_PROTOCOL("HANDSHAKING_IN_SET_PROTOCOL", 0, "PacketHandshakingInSetProtocol"), 
        LOGIN_IN_ENCRYPTION_BEGIN("LOGIN_IN_ENCRYPTION_BEGIN", 1, "PacketLoginInEncryptionBegin"), 
        LOGIN_IN_START("LOGIN_IN_START", 2, "PacketLoginInStart"), 
        LOGIN_OUT_DISCONNECT("LOGIN_OUT_DISCONNECT", 3, "PacketLoginOutDisconnect"), 
        LOGIN_OUT_ENCRYPTION_BEGIN("LOGIN_OUT_ENCRYPTION_BEGIN", 4, "PacketLoginOutEncryptionBegin"), 
        LOGIN_OUT_SUCCESS("LOGIN_OUT_SUCCESS", 5, "PacketLoginOutSuccess"), 
        PLAY_IN_ABILITIES("PLAY_IN_ABILITIES", 6, "PacketPlayInAbilities"), 
        PLAY_IN_ARM_ANIMATION("PLAY_IN_ARM_ANIMATION", 7, "PacketPlayInArmAnimation"), 
        PLAY_IN_BLOCK_DIG("PLAY_IN_BLOCK_DIG", 8, "PacketPlayInBlockDig"), 
        PLAY_IN_BLOCK_PLACE("PLAY_IN_BLOCK_PLACE", 9, "PacketPlayInBlockPlace"), 
        PLAY_IN_CHAT("PLAY_IN_CHAT", 10, "PacketPlayInChat"), 
        PLAY_IN_CLIENT_COMMAND("PLAY_IN_CLIENT_COMMAND", 11, "PacketPlayInClientCommand"), 
        PLAY_IN_CLOSE_WINDOW("PLAY_IN_CLOSE_WINDOW", 12, "PacketPlayInCloseWindow"), 
        PLAY_IN_CUSTOM_PAYLOAD("PLAY_IN_CUSTOM_PAYLOAD", 13, "PacketPlayInCustomPayload"), 
        PLAY_IN_ENCHANT_ITEM("PLAY_IN_ENCHANT_ITEM", 14, "PacketPlayInEnchantItem"), 
        PLAY_IN_ENTITY_ACTION("PLAY_IN_ENTITY_ACTION", 15, "PacketPlayInEntityAction"), 
        PLAY_IN_FLYING("PLAY_IN_FLYING", 16, "PacketPlayInFlying"), 
        PLAY_IN_HELD_ITEM_SLOT("PLAY_IN_HELD_ITEM_SLOT", 17, "PacketPlayInHeldItemSlot"), 
        PLAY_IN_KEEP_ALIVE("PLAY_IN_KEEP_ALIVE", 18, "PacketPlayInKeepAlive"), 
        PLAY_IN_LOOK("PLAY_IN_LOOK", 19, "PacketPlayInLook"), 
        PLAY_IN_POSITION("PLAY_IN_POSITION", 20, "PacketPlayInPosition"), 
        PLAY_IN_POSITION_LOOK("PLAY_IN_POSITION_LOOK", 21, "PacketPlayInPositionLook"), 
        PLAY_IN_SET_CREATIVE_SLOT("PLAY_IN_SET_CREATIVE_SLOT", 22, "PacketPlayInSetCreativeSlot "), 
        PLAY_IN_SETTINGS("PLAY_IN_SETTINGS", 23, "PacketPlayInSettings"), 
        PLAY_IN_STEER_VEHICLE("PLAY_IN_STEER_VEHICLE", 24, "PacketPlayInSteerVehicle"), 
        PLAY_IN_TAB_COMPLETE("PLAY_IN_TAB_COMPLETE", 25, "PacketPlayInTabComplete"), 
        PLAY_IN_TRANSACTION("PLAY_IN_TRANSACTION", 26, "PacketPlayInTransaction"), 
        PLAY_IN_UPDATE_SIGN("PLAY_IN_UPDATE_SIGN", 27, "PacketPlayInUpdateSign"), 
        PLAY_IN_USE_ENTITY("PLAY_IN_USE_ENTITY", 28, "PacketPlayInUseEntity"), 
        PLAY_IN_WINDOW_CLICK("PLAY_IN_WINDOW_CLICK", 29, "PacketPlayInWindowClick"), 
        PLAY_OUT_ABILITIES("PLAY_OUT_ABILITIES", 30, "PacketPlayOutAbilities"), 
        PLAY_OUT_ANIMATION("PLAY_OUT_ANIMATION", 31, "PacketPlayOutAnimation"), 
        PLAY_OUT_ATTACH_ENTITY("PLAY_OUT_ATTACH_ENTITY", 32, "PacketPlayOutAttachEntity"), 
        PLAY_OUT_BED("PLAY_OUT_BED", 33, "PacketPlayOutBed"), 
        PLAY_OUT_BLOCK_ACTION("PLAY_OUT_BLOCK_ACTION", 34, "PacketPlayOutBlockAction"), 
        PLAY_OUT_BLOCK_BREAK_ANIMATION("PLAY_OUT_BLOCK_BREAK_ANIMATION", 35, "PacketPlayOutBlockBreakAnimation"), 
        PLAY_OUT_BLOCK_CHANGE("PLAY_OUT_BLOCK_CHANGE", 36, "PacketPlayOutBlockChange"), 
        PLAY_OUT_CHAT("PLAY_OUT_CHAT", 37, "PacketPlayOutChat"), 
        PLAY_OUT_CLOSE_WINDOW("PLAY_OUT_CLOSE_WINDOW", 38, "PacketPlayOutCloseWindow"), 
        PLAY_OUT_COLLECT("PLAY_OUT_COLLECT", 39, "PacketPlayOutCollect"), 
        PLAY_OUT_CRAFT_PROGRESS_BAR("PLAY_OUT_CRAFT_PROGRESS_BAR", 40, "PacketPlayOutCraftProgressBar"), 
        PLAY_OUT_CUSTOM_PAYLOAD("PLAY_OUT_CUSTOM_PAYLOAD", 41, "PacketPlayOutCustomPayload"), 
        PLAY_OUT_ENTITY("PLAY_OUT_ENTITY", 42, "PacketPlayOutEntity"), 
        PLAY_OUT_ENTITY_DESTROY("PLAY_OUT_ENTITY_DESTROY", 43, "PacketPlayOutEntityDestroy"), 
        PLAY_OUT_ENTITY_EFFECT("PLAY_OUT_ENTITY_EFFECT", 44, "PacketPlayOutEntityEffect"), 
        PLAY_OUT_ENTITY_EQUIPMENT("PLAY_OUT_ENTITY_EQUIPMENT", 45, "PacketPlayOutEntityEquipment"), 
        PLAY_OUT_ENTITY_HEAD_ROTATION("PLAY_OUT_ENTITY_HEAD_ROTATION", 46, "PacketPlayOutEntityHeadRotation"), 
        PLAY_OUT_ENTITY_LOOK("PLAY_OUT_ENTITY_LOOK", 47, "PacketPlayOutEntityLook"), 
        PLAY_OUT_ENTITY_METADATA("PLAY_OUT_ENTITY_METADATA", 48, "PacketPlayOutEntityMetadata"), 
        PLAY_OUT_ENTITY_STATUS("PLAY_OUT_ENTITY_STATUS", 49, "PacketPlayOutEntityStatus"), 
        PLAY_OUT_ENTITY_TELEPORT("PLAY_OUT_ENTITY_TELEPORT", 50, "PacketPlayOutEntityTeleport"), 
        PLAY_OUT_ENTITY_VELOCITY("PLAY_OUT_ENTITY_VELOCITY", 51, "PacketPlayOutEntityVelocity"), 
        PLAY_OUT_EXPERIENCE("PLAY_OUT_EXPERIENCE", 52, "PacketPlayOutExperience"), 
        PLAY_OUT_EXPLOSION("PLAY_OUT_EXPLOSION", 53, "PacketPlayOutExplosion"), 
        PLAY_OUT_GAME_STATE_CHANGE("PLAY_OUT_GAME_STATE_CHANGE", 54, "PacketPlayOutGameStateChange"), 
        PLAY_OUT_HELD_ITEM_SLOT("PLAY_OUT_HELD_ITEM_SLOT", 55, "PacketPlayOutHeldItemSlot"), 
        PLAY_OUT_KEEP_ALIVE("PLAY_OUT_KEEP_ALIVE", 56, "PacketPlayOutKeepAlive"), 
        PLAY_OUT_KICK_DISCONNECT("PLAY_OUT_KICK_DISCONNECT", 57, "PacketPlayOutKickDisconnect"), 
        PLAY_OUT_LOGIN("PLAY_OUT_LOGIN", 58, "PacketPlayOutLogin"), 
        PLAY_OUT_MAP("PLAY_OUT_MAP", 59, "PacketPlayOutMap"), 
        PLAY_OUT_MAP_CHUNK("PLAY_OUT_MAP_CHUNK", 60, "PacketPlayOutMapChunk"), 
        PLAY_OUT_MAP_CHUNK_BULK("PLAY_OUT_MAP_CHUNK_BULK", 61, "PacketPlayOutMapChunkBulk"), 
        PLAY_OUT_MULTI_BLOCK_CHANGE("PLAY_OUT_MULTI_BLOCK_CHANGE", 62, "PacketPlayOutMultiBlockChange"), 
        PLAY_OUT_NAMED_ENTITY_SPAWN("PLAY_OUT_NAMED_ENTITY_SPAWN", 63, "PacketPlayOutNamedEntitySpawn"), 
        PLAY_OUT_NAMED_SOUND_EFFECT("PLAY_OUT_NAMED_SOUND_EFFECT", 64, "PacketPlayOutNamedSoundEffect"), 
        PLAY_OUT_OPEN_SIGN_EDITOR("PLAY_OUT_OPEN_SIGN_EDITOR", 65, "PacketPlayOutOpenSignEditor"), 
        PLAY_OUT_OPEN_WINDOW("PLAY_OUT_OPEN_WINDOW", 66, "PacketPlayOutOpenWindow"), 
        PLAY_OUT_PLAYER_INFO("PLAY_OUT_PLAYER_INFO", 67, "PacketPlayOutPlayerInfo"), 
        PLAY_OUT_POSITION("PLAY_OUT_POSITION", 68, "PacketPlayOutPosition"), 
        PLAY_OUT_REL_ENTITY_MOVE("PLAY_OUT_REL_ENTITY_MOVE", 69, "PacketPlayOutRelEntityMove"), 
        PLAY_OUT_REL_ENTITY_MOVE_LOOK("PLAY_OUT_REL_ENTITY_MOVE_LOOK", 70, "PacketPlayOutRelEntityMoveLook"), 
        PLAY_OUT_REMOVE_ENTITY_EFFECT("PLAY_OUT_REMOVE_ENTITY_EFFECT", 71, "PacketPlayOutRemoveEntityEffect"), 
        PLAY_OUT_RESPAWN("PLAY_OUT_RESPAWN", 72, "PacketPlayOutRespawn"), 
        PLAY_OUT_SCOREBOARD_DISPLAY_OBJECTIVE("PLAY_OUT_SCOREBOARD_DISPLAY_OBJECTIVE", 73, "PacketPlayOutScoreboardDisplayObjective"), 
        PLAY_OUT_SCOREBOARD_OBJECTIVE("PLAY_OUT_SCOREBOARD_OBJECTIVE", 74, "PacketPlayOutScoreboardObjective"), 
        PLAY_OUT_SCOREBOARD_SCORE("PLAY_OUT_SCOREBOARD_SCORE", 75, "PacketPlayOutScoreboardScore"), 
        PLAY_OUT_SCOREBOARD_TEAM("PLAY_OUT_SCOREBOARD_TEAM", 76, "PacketPlayOutScoreboardTeam"), 
        PLAY_OUT_SET_SLOT("PLAY_OUT_SET_SLOT", 77, "PacketPlayOutSetSlot"), 
        PLAY_OUT_SPAWN_ENTITY("PLAY_OUT_SPAWN_ENTITY", 78, "PacketPlayOutSpawnEntity"), 
        PLAY_OUT_SPAWN_ENTITY_EXPERIENCE_ORB("PLAY_OUT_SPAWN_ENTITY_EXPERIENCE_ORB", 79, "PacketPlayOutSpawnEntityExperienceOrb"), 
        PLAY_OUT_SPAWN_ENTITY_LIVING("PLAY_OUT_SPAWN_ENTITY_LIVING", 80, "PacketPlayOutSpawnEntityLiving"), 
        PLAY_OUT_SPAWN_ENTITY_PAINTING("PLAY_OUT_SPAWN_ENTITY_PAINTING", 81, "PacketPlayOutSpawnEntityPainting"), 
        PLAY_OUT_SPAWN_ENTITY_WEATHER("PLAY_OUT_SPAWN_ENTITY_WEATHER", 82, "PacketPlayOutSpawnEntityWeather"), 
        PLAY_OUT_SPAWN_POSITION("PLAY_OUT_SPAWN_POSITION", 83, "PacketPlayOutSpawnPosition"), 
        PLAY_OUT_STATISTIC("PLAY_OUT_STATISTIC", 84, "PacketPlayOutStatistic"), 
        PLAY_OUT_TAB_COMPLETE("PLAY_OUT_TAB_COMPLETE", 85, "PacketPlayOutTabComplete"), 
        PLAY_OUT_TILE_ENTITY_DATA("PLAY_OUT_TILE_ENTITY_DATA", 86, "PacketPlayOutTileEntityData"), 
        PLAY_OUT_TRANSACTION("PLAY_OUT_TRANSACTION", 87, "PacketPlayOutTransaction"), 
        PLAY_OUT_UPDATE_ATTRIBUTES("PLAY_OUT_UPDATE_ATTRIBUTES", 88, "PacketPlayOutUpdateAttributes"), 
        PLAY_OUT_UPDATE_HEALTH("PLAY_OUT_UPDATE_HEALTH", 89, "PacketPlayOutUpdateHealth"), 
        PLAY_OUT_UPDATE_SIGN("PLAY_OUT_UPDATE_SIGN", 90, "PacketPlayOutUpdateSign"), 
        PLAY_OUT_UPDATE_TIME("PLAY_OUT_UPDATE_TIME", 91, "PacketPlayOutUpdateTime"), 
        PLAY_OUT_WINDOW_ITEMS("PLAY_OUT_WINDOW_ITEMS", 92, "PacketPlayOutWindowItems"), 
        PLAY_OUT_WORLD_EVENT("PLAY_OUT_WORLD_EVENT", 93, "PacketPlayOutWorldEvent"), 
        PLAY_OUT_WORLD_PARTICLES("PLAY_OUT_WORLD_PARTICLES", 94, "PacketPlayOutWorldParticles", "Packet63WorldParticles"), 
        STATUS_IN_PING("STATUS_IN_PING", 95, "PacketStatusInPing"), 
        STATUS_IN_START("STATUS_IN_START", 96, "PacketStatusInStart"), 
        STATUS_OUT_PONG("STATUS_OUT_PONG", 97, "PacketStatusOutPong"), 
        STATUS_OUT_SERVER_INFO("STATUS_OUT_SERVER_INFO", 98, "PacketStatusOutServerInfo");
        
        private final String name;
        private final String legacy;
        private Class<?> packet;
        
        private PacketType(final String s, final int n, final String name) {
            this.name = name;
            this.legacy = null;
        }
        
        private PacketType(final String s, final int n, final String name, final String legacy) {
            this.name = name;
            this.legacy = legacy;
        }
        
        public String getName() {
            return this.getName();
        }
        
        public Class<?> getPacket() throws Exception {
            if (this.packet == null) {
                try {
                    this.packet = ReflectionHandler.getClass(this.name, PackageType.MINECRAFT_SERVER);
                }
                catch (Exception ex) {
                    if (this.legacy == null) {
                        throw ex;
                    }
                    this.packet = ReflectionHandler.getClass(this.legacy, PackageType.MINECRAFT_SERVER);
                }
            }
            return this.packet;
        }
    }
    
    public enum SubPackageType
    {
        BLOCK("BLOCK", 0), 
        CHUNKIO("CHUNKIO", 1), 
        COMMAND("COMMAND", 2), 
        CONVERSATIONS("CONVERSATIONS", 3), 
        ENCHANTMENS("ENCHANTMENS", 4), 
        ENTITY("ENTITY", 5), 
        EVENT("EVENT", 6), 
        GENERATOR("GENERATOR", 7), 
        HELP("HELP", 8), 
        INVENTORY("INVENTORY", 9), 
        MAP("MAP", 10), 
        METADATA("METADATA", 11), 
        POTION("POTION", 12), 
        PROJECTILES("PROJECTILES", 13), 
        SCHEDULER("SCHEDULER", 14), 
        SCOREBOARD("SCOREBOARD", 15), 
        UPDATER("UPDATER", 16), 
        UTIL("UTIL", 17);
        
        private final String name;
        
        private SubPackageType(final String s, final int n) {
            this.name = PackageType.CRAFTBUKKIT + "." + this.name().toLowerCase();
        }
        
        public String getName() {
            return this.name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
    
    public final class FieldPair
    {
        private final String name;
        private final Object value;
        
        public FieldPair(final String name, final Object value) {
            super();
            this.name = name;
            this.value = value;
        }
        
        public String getName() {
            return this.name;
        }
        
        public Object getValue() {
            return this.value;
        }
    }
}