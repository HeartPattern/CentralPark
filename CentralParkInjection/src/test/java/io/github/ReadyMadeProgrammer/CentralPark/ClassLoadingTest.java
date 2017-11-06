package io.github.ReadyMadeProgrammer.CentralPark;

import io.github.ReadyMadeProgrammer.CentralPark.binder.CustomBinder;
import org.junit.Test;
import org.reflections.Reflections;

public class ClassLoadingTest {
    @Test
    public void test(){
        System.out.println("e");
        new Reflections(InjectionManager.class.getClassLoader()).getSubTypesOf(CustomBinder.class).parallelStream().forEach(c-> System.out.println(c.getName()));

    }
}