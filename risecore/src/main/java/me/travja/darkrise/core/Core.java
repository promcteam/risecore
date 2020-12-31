package me.travja.darkrise.core;

import me.travja.darkrise.core.bungee.BungeeListener;
import me.travja.darkrise.core.bungee.BungeeUtil;
import me.travja.darkrise.core.command.ReloadCommand;
import me.travja.darkrise.core.command.UnstuckCommand;
import me.travja.darkrise.core.config.CommandBlock;
import me.travja.darkrise.core.item.DarkRiseItemImpl;
import me.travja.darkrise.core.legacy.cmds.DelayedCommand;
import me.travja.darkrise.core.legacy.util.Init;
import me.travja.darkrise.core.legacy.util.Vault;
import me.travja.darkrise.core.legacy.util.item.ItemBuilder;
import me.travja.darkrise.core.legacy.util.message.MessageUtil;
import me.travja.darkrise.core.listeners.BoatListener;
import me.travja.darkrise.core.listeners.InteractListener;
import me.travja.darkrise.core.listeners.JoinListener;
import me.travja.darkrise.core.util.BlockLocation;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Core extends JavaPlugin {

    private static Core instance;
    public static Logger log;
    public static FileConfiguration config;

    public static boolean IS_BUNGEE = false;
    public static String BUNGEE_ID = "server";

    private static List<DelayedCommand> firstJoin = new ArrayList<>(), onJoin = new ArrayList<>();
    private static List<CommandBlock> onInteract = new ArrayList<>();

    @Override
    public void onLoad() {
        super.onLoad();
        ConfigurationSerialization.registerClass(BlockLocation.class, "RC_BlockLocation");
        instance = this;
    }

    @Override
    public void onEnable() {
        log = this.getLogger();

        ConfigurationSerialization.registerClass(ItemBuilder.class);
        config = ConfigManager.loadConfigFile(new File(getDataFolder(), "config.yml"), getResource("config.yml"));
        Debugger.setDebug(config.getBoolean("debug", false));
        register();
        Vault.init();
        Init.load();
    }

    public static Core getInstance() {
        return instance;
    }

    public static List<DelayedCommand> getFirstJoin() {
        return firstJoin;
    }

    public static List<DelayedCommand> getOnJoin() {
        return onJoin;
    }

    public static List<CommandBlock> getOnInteract() {
        return onInteract;
    }

    public static List<CommandBlock> getOnInteract(Material type) {
        ArrayList<CommandBlock> ret = new ArrayList<>();

        for (CommandBlock cmd : onInteract) {
            if (cmd.getMaterial() == type) {
                ret.add(cmd);
            }
        }

        return ret;
    }

    private void register() {
        UnstuckCommand unstuck = new UnstuckCommand();
        getCommand("corereload").setExecutor(new ReloadCommand());
        getCommand("stuck").setExecutor(unstuck);
        ConfigurationSerialization.registerClass(DarkRiseItemImpl.class, "DarkRiseItemImpl");
        ConfigurationSerialization.registerClass(DarkRiseItemImpl.DivineItemsMeta.class, "DarkRiseItemImpl_Divine");

        reloadConfiguration();
        BUNGEE_ID = config.getString("bungee_id", "server");
        IS_BUNGEE = config.getBoolean("bungee", false);
        getServer().getMessenger().registerOutgoingPluginChannel(this, BungeeUtil.CHANNEL);
        getServer().getMessenger().registerIncomingPluginChannel(this, BungeeUtil.CHANNEL, new BungeeListener());

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new InteractListener(), this);
        pm.registerEvents(new BoatListener(), this);
        pm.registerEvents(unstuck, this);

    }

    public void reloadConfiguration() {
        config = ConfigManager.loadConfigFile(new File(getDataFolder(), "config.yml"), getResource("config.yml"));
        MessageUtil.load(ConfigManager.loadConfigFile(new File(getDataFolder() + File.separator + "lang", "lang_en.yml"), getResource("lang/lang_en.yml")), this);
        firstJoin = DelayedCommand.deserializeMapList((ArrayList<Map<String, Object>>) config.get("onFirstJoin"));
        onJoin = DelayedCommand.deserializeMapList((ArrayList<Map<String, Object>>) config.get("onJoin"));
        onInteract = new ArrayList<>();
        for (Map<String, Object> cmd : ((List<Map<String, Object>>) config.get("onInteract"))) {
            onInteract.add(new CommandBlock(cmd));
        }
    }

}