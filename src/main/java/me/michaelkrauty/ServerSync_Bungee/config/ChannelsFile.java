package me.michaelkrauty.ServerSync_Bungee.config;

import me.michaelkrauty.ServerSync_Bungee.Main;
import net.md_5.bungee.config.Configuration;

import java.util.Collection;

/**
 * Created on 3/21/2015.
 *
 * @author michaelkrauty
 */
public class ChannelsFile extends YamlFile {

    public ChannelsFile(Main main) {
        super(main, "channels");
    }

    public Collection<String> getChannels() {
        return yaml.getKeys();
    }

    public Configuration getChannel(String name) {
        return yaml.getSection(name);
    }
}
