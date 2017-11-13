package io.github.ReadyMadeProgrammer.CentralPark;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ClasspathDescriptorFileFinder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

class Injection {
    private static Injection injection;
    private HashMap<String, List<Class<?>>> databaseClasses;
    private Set<String> plugins;
    private boolean loadedPlugins;
    private CentralParkPlugin centralParkPlugin;
    private JavaPluginLoader bukkitJavaPluginLoader;
    private ServiceLocator locator;

    private Injection() {
        locator = ServiceLocatorUtilities.createAndPopulateServiceLocator("Global");
        databaseClasses = new HashMap<>();
        plugins = new HashSet<>();
        bukkitJavaPluginLoader = new JavaPluginLoader(Bukkit.getServer());
    }

    static Injection getInstance() {
        if (injection == null) {
            synchronized (Injection.class) {
                if (injection == null) {
                    injection = new Injection();
                }
            }
        }
        return injection;
    }

    void load(JavaPlugin javaPlugin, ClassLoader classLoader) {
        if (javaPlugin instanceof CentralParkPlugin) {
            centralParkPlugin = (CentralParkPlugin) javaPlugin;
        }
        DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
        try {
            dcs.getPopulator().populate(new ClasspathDescriptorFileFinder(classLoader));
        } catch (IOException e) {
            e.printStackTrace();
            javaPlugin.getLogger().severe("Failed to load injection inhabitants file.");
            javaPlugin.getLogger().severe("Disabling...");
            Bukkit.getPluginManager().disablePlugin(javaPlugin);
        }
        Set<Class<? extends AbstractBinder>> binders = ReflectionUtil.getSubtypeOf(AbstractBinder.class, classLoader);
        ServiceLocatorUtilities.bind(locator, (Binder[]) binders.toArray());
        if (checkAllLoaded(javaPlugin)) {
            initialize();
        }
    }

    private boolean checkAllLoaded(JavaPlugin plugin) {
        if (!loadedPlugins) {
            findPlugins();
        }
        plugins.remove(plugin.getDescription().getFullName());
        return plugins.isEmpty();
    }

    private void findPlugins() {
        plugins = (Lists.newArrayList(Objects.requireNonNull(centralParkPlugin.getDataFolder().getParentFile().listFiles())))
                .parallelStream()
                .filter(File::isFile)
                .filter(f -> f.getName().endsWith(".jar"))
                .map(f -> {
                    try {
                        return bukkitJavaPluginLoader.getPluginDescription(f);
                    } catch (InvalidDescriptionException e) {
                        e.printStackTrace();
                    }
                    return new PluginDescriptionFile("", "", "");
                })
                .filter(pd -> pd.getDepend().contains("CentralPark"))
                .map(PluginDescriptionFile::getFullName)
                .collect(Collectors.toSet());
    }

    private void initialize() {
        locator.getAllServices(Module.class);
    }

    void close() {
        locator.shutdown();
    }
}
