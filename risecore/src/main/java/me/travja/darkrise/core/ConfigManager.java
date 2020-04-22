package me.travja.darkrise.core;

import org.jetbrains.annotations.Nullable;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConfigManager {

    /**
     * Loads a given file as a {@link FileConfiguration} object.
     * If the file doesn't exist, one will be created and the default values will be added.
     *
     * @param file        The File to load.
     * @param defaultFile An InputStream representing the default values for the file. Can be null
     * @return The FileConfiguration parsed from the file.
     */
    public static FileConfiguration loadConfigFile(File file, @Nullable InputStream defaultFile) {
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
                if (defaultFile != null) {
                    FileOutputStream out = new FileOutputStream(file);
                    int read;
                    while ((read = defaultFile.read()) != -1) {
                        out.write(read);
                    }

                    defaultFile.close();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration conf = new YamlConfiguration();
        try {
            conf.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return conf;
    }

}
