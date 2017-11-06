package io.github.ReadyMadeProgrammer.CentralPark;

import io.github.ReadyMadeProgrammer.CentralPark.binder.*;
import org.bukkit.Bukkit;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ClasspathDescriptorFileFinder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

import java.io.IOException;
import java.util.Set;

class InjectionManager {
    private CentralParkPlugin plugin;
    private static ServiceLocator systemServiceLocator;
    private ServiceLocator pluginServiceLocator;
    private BukkitBinder bukkitBinder;
    private CentralParkBinder centralParkBinder;
    private PluginBinder pluginBinder;
    static{
        systemServiceLocator = ServiceLocatorUtilities.createAndPopulateServiceLocator("CentralPark");
        Set<? extends CustomBinder> binders = CustomBinderFinder.getBinders(InjectionManager.class.getClassLoader());
        binders.forEach(b->ServiceLocatorUtilities.bind(systemServiceLocator,b));
    }
    InjectionManager(CentralParkPlugin plugin){
        this.plugin = plugin;
        pluginServiceLocator = ServiceLocatorUtilities.createAndPopulateServiceLocator(plugin.getName());
        bukkitBinder = new BukkitBinder();
        pluginBinder = new PluginBinder(plugin);
        ServiceLocatorUtilities.bind(systemServiceLocator,bukkitBinder,centralParkBinder);
    }
    void onEnablePlugin(){
        Binder[] binders = (Binder[])plugin.binders().toArray();
        ServiceLocatorUtilities.bind(systemServiceLocator,binders);
        DynamicConfigurationService dcs = systemServiceLocator.getService(DynamicConfigurationService.class);
        try {
            dcs.getPopulator().populate(new ClasspathDescriptorFileFinder(plugin.getClass().getClassLoader()));
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Failed to load injection inhabitants file.");
            plugin.getLogger().severe("Disabling...");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
        systemServiceLocator.getAllServices(Module.class);
        systemServiceLocator.inject(plugin);
        systemServiceLocator.postConstruct(plugin);
    }
    void onDisablePlugin(){
        systemServiceLocator.preDestroy(plugin);
        systemServiceLocator.shutdown();
    }
}
