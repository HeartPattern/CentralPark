package io.github.ReadyMadeProgrammer.CentralPark.binder;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Binder(Binder.Scope.GLOBAL)
public class BukkitBinder extends CustomBinder{
    @Override
    protected void configure() {
        bind(Bukkit.getServer()).to(Server.class);
        bind(Bukkit.getPluginManager()).to(PluginManager.class);
        bind(Bukkit.getScoreboardManager()).to(ScoreboardManager.class);
        bind(Bukkit.getItemFactory()).to(ItemFactory.class);
        bind(Bukkit.getMessenger()).to(Messenger.class);
        bind(Bukkit.getScheduler()).to(BukkitScheduler.class);
        for(Plugin plugin : Bukkit.getPluginManager().getPlugins()){
            bindPlugin(plugin.getClass(),plugin);
        }
    }
    private <T extends Plugin> void bindPlugin(Class<T> tPlugin, Plugin plugin){
        bind(tPlugin.cast(plugin)).to(tPlugin).to(Plugin.class).named(plugin.getName());
    }
}
