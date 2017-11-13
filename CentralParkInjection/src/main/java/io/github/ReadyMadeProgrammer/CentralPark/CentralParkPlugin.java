package io.github.ReadyMadeProgrammer.CentralPark;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public abstract class CentralParkPlugin extends JavaPlugin {
    private Injection injection;
    public final void onEnable(){
        injection = Injection.getInstance();
        injection.load(this, this.getClassLoader());
        // TODO: 17. 11. 6 Write Install Checker
        installDDL();
    }
    public final void onDisable(){

    }

    private List<Class<?>> databaseClasses() {
        return Collections.emptyList();
    }
    public final List<Class<?>> getDatabaseClasses(){
        return databaseClasses();
    }
}
