package io.github.ReadyMadeProgrammer.CentralPark.binder;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class CustomBinderFinder {
    public static Set<? extends CustomBinder> getBinders(ClassLoader classLoader){
        Reflections reflections = new Reflections(new ConfigurationBuilder().addClassLoader(classLoader));
        return Optional.ofNullable((Set<? extends CustomBinder>) reflections.getSubTypesOf(CustomBinder.class).parallelStream().filter(c->c.isAnnotationPresent(Binder.class)).filter(c->!c.getName().startsWith("org.glassfish.hk2")).collect((Supplier<HashSet>) HashSet::new,HashSet::add,HashSet::addAll)).orElse(Collections.emptySet());
    }
}
