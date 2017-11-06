package io.github.ReadyMadeProgrammer.CentralPark;

import org.bukkit.plugin.java.JavaPlugin;
import org.glassfish.hk2.utilities.Binder;

import java.util.Collections;
import java.util.List;

public abstract class CentralParkPlugin extends JavaPlugin {
    private InjectionManager injectionManager;
    public final void onEnable(){
        injectionManager = new InjectionManager(this);
        injectionManager.onEnablePlugin();
        // TODO: 17. 11. 6 Write Install Checker
        installDDL();
    }
    public final void onDisable(){
        injectionManager.onDisablePlugin();
    }
    public List<Binder> binders(){
        return Collections.emptyList();
    }
    public List<Class<?>> databaseClasses(){
        return Collections.emptyList();
    }
    public final List<Class<?>> getDatabaseClasses(){
        return databaseClasses();
    }
}
