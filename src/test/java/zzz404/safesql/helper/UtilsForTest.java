package zzz404.safesql.helper;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import zzz404.safesql.ConnectionFactory;
import zzz404.safesql.util.NoisyRunnable;

public class UtilsForTest {

    public static void coverAll(Object o) {
        Method[] methods = o.getClass().getMethods();
        for (Method method : methods) {
            if (method.getDeclaringClass() == o.getClass()) {
                Class<?>[] classes = method.getParameterTypes();
                Object[] args = new Object[classes.length];
                for (int i = 0; i < classes.length; i++) {
                    Class<?> clazz = classes[i];
                    if (classes[i].isPrimitive()) {
                        args[i] = Array.get(Array.newInstance(clazz, 1), 0);
                    }
                }
                try {
                    method.invoke(o, args);
                }
                catch (Exception ignored) {
                }
            }
        }
    }

    public static <T> void pass(NoisyRunnable runnable) {
        try {
            runnable.run();
        }
        catch (Exception ignored) {
        }
    }

}
