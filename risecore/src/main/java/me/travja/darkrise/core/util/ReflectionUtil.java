package me.travja.darkrise.core.util;

import lombok.Getter;
import org.bukkit.Bukkit;

public class ReflectionUtil {

    private static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    @Getter
    private static boolean newVersion = Integer.parseInt(version.split("_")[1]) >= 17;

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + version + "." + name);
    }

    public static Class<?> getBukkitClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
    }

}
