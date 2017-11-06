package io.github.ReadyMadeProgrammer.CentralPark.binder;

public @interface Binder {
    enum Scope{
        GLOBAL, PLUGIN
    }
    Scope value() default Scope.PLUGIN;
}
