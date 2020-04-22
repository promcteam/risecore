package me.travja.darkrise.core;

import me.travja.darkrise.core.legacy.util.message.MessageData;
import me.travja.darkrise.core.legacy.util.message.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class RisePlugin extends JavaPlugin {

    public static Logger log;
    public static RisePlugin inst;

    {
        inst = this;
        log = this.getLogger();
    }

    public void error(String msg) {
        log.severe(msg);
    }

    public BukkitTask runSync(Runnable run) {
        return Bukkit.getScheduler().runTask(inst, run);
    }

    public BukkitTask runTaskLater(int delay, Runnable run) {
        if (delay == 0)
            return runSync(run);
        return Bukkit.getScheduler().runTaskLater(inst, run, delay * 20L);
    }

    public BukkitTask runTaskAsynchronously(Runnable run) {
        return Bukkit.getScheduler().runTaskAsynchronously(this, run);
    }


    public boolean checkPermission(CommandSender sender, String s) {
        if (!(sender instanceof ConsoleCommandSender) && !sender.hasPermission(s)) {
            MessageUtil.sendMessage("noPermissions", sender, new MessageData("permission", s));
            return false;
        }

        return true;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        loadLang();
    }

    public FileConfiguration getLang() {
        File lang = new File(getDataFolder(), "lang_en.yml");
        if (!lang.exists()) {
            InputStream in = getResource("lang_en.yml");
            if (in == null)
                return null;

            lang.getParentFile().mkdirs();
            try {
                lang.createNewFile();
                FileWriter writer = new FileWriter(lang);
                int read;
                while ((read = in.read()) != -1) {
                    writer.write(read);
                }
                writer.close();
                in.close();
            } catch (IOException e) {
                getLogger().severe("Could not save lang_en.yml");
                e.printStackTrace();
            }
        }
        FileConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        return conf;
    }

    public void loadLang() {
        FileConfiguration conf = getLang();
        if (conf == null)
            return;
        MessageUtil.load(conf, this);
    }

    public void reloadLang() {
        FileConfiguration conf = getLang();
        if (conf == null)
            return;
        MessageUtil.reload(conf, this);
    }

    public void reloadMessages() {
        FileConfiguration lang = ConfigManager.loadConfigFile(new File(getDataFolder() + File.separator + "lang", "lang_en.yml"), getResource("lang/lang_en.yml"));
        MessageUtil.reload(lang, this);
    }

}