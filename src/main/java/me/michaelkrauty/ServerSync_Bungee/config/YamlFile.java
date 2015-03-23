package me.michaelkrauty.ServerSync_Bungee.config;


import me.michaelkrauty.ServerSync_Bungee.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created on 3/21/2015.
 *
 * @author michaelkrauty
 */
public class YamlFile {

    public final File yamlFile;
    private final Main main;
    Configuration yaml;

    public YamlFile(Main main, String name) {
        this.main = main;
        yamlFile = new File(main.getDataFolder(), name + ".yml");
        if (!yamlFile.exists()) {
            try {
                InputStream in = main.getResourceAsStream(name + ".yml");
                FileOutputStream out = new FileOutputStream(yamlFile);
                byte[] buffer = new byte[1024];
                int len = in.read(buffer);
                while (len != -1) {
                    out.write(buffer, 0, len);
                    len = in.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        load();
    }

    public void load() {
        try {
            yaml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(yamlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(yaml, yamlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
