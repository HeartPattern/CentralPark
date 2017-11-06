package io.github.ReadyMadeProgrammer.CentralPark.binder;

import com.avaje.ebean.EbeanServer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import java.util.logging.Logger;

public class PluginBinder extends AbstractBinder {
    private Plugin plugin;

    public PluginBinder(Plugin plugin){
        this.plugin = plugin;
    }
    @Override
    protected void configure() {
        bind(plugin.getLogger()).to(Logger.class);
        bind(plugin.getDatabase()).to(EbeanServer.class);
        bind(plugin.getDescription()).to(PluginDescriptionFile.class);
        bind(plugin.getPluginLoader()).to(PluginLoader.class);
    }
}
