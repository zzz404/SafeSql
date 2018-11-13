package zzz404.safesql;

import org.apache.commons.lang3.builder.EqualsBuilder;

public final class Utils {
    private Utils() {
    }

    public static RuntimeException throwRuntime(Throwable e) {
        Utils.<RuntimeException> _throwRuntime(e);
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Exception> void _throwRuntime(Throwable e)
            throws E {
        throw (E) e;
    }

    public static <T> boolean isEquals(T o1, Object o2, MainValueExtractor<T> valueExtractor) {
        if (o1.getClass() != o2.getClass()) {
            return false;
        }
        else {
            Object[] os1 = valueExtractor.extract(o1);
            if (os1 == null) {
                return true;
            }
            @SuppressWarnings("unchecked")
            Object[] os2 = valueExtractor.extract((T) o2);
            EqualsBuilder builder = new EqualsBuilder();
            for (int i = 0; i < os1.length; i++) {
                builder.append(os1[i], os2[i]);
            }
            return builder.isEquals();
        }
    }

    @FunctionalInterface
    public interface MainValueExtractor<T> {
        Object[] extract(T t);
    }
}
