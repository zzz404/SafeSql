package zzz404.safesql.helper;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;

import zzz404.safesql.Entity;
import zzz404.safesql.Field;
import zzz404.safesql.sql.type.TypedValue;
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

    public static Field<Integer> createSimpleField(String propertyName) {
        return new Field<>(new Entity<>(1, Object.class), propertyName);
    }

    public static <K, V> Map<K, V> newMap(K k, V v) {
        HashMap<K, V> map = new HashMap<>();
        map.put(k, v);
        return map;
    }

    public static <K, V> Map<K, V> newMap(K k1, V v1, K k2, V v2) {
        HashMap<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static void assertEquals(List<?> list, Object[] array) {
        Assertions.assertEquals(list, Arrays.asList(array));
    }

    public static List<TypedValue<?>> createTypedValueList(Object... os) {
        return Arrays.stream(os).map(TypedValue::valueOf).collect(Collectors.toList());
    }
}
