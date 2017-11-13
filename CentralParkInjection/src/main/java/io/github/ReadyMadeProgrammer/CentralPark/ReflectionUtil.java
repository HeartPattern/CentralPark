package io.github.ReadyMadeProgrammer.CentralPark;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ReflectionUtil {
    public static <T> Set<Class<? extends T>> getSubtypeOf(Class<T> superType, ClassLoader classLoader) {
        return getReflections(classLoader).getSubTypesOf(superType);
    }

    public static Set<Class<?>> getAnnotatedWith(Class<? extends Annotation> annotation, ClassLoader classLoader) {
        return getReflections(classLoader).getTypesAnnotatedWith(annotation);
    }

    private static Reflections getReflections(ClassLoader... classLoaders) {
        return new Reflections(new ConfigurationBuilder().addClassLoaders(classLoaders));
    }
}
