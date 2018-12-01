package zzz404.safesql.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.builder.EqualsBuilder;

public final class CommonUtils {
    private CommonUtils() {
    }

    public static RuntimeException wrapToRuntime(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        else {
            return new RuntimeException(e);
        }
    }

    public static <T> boolean isEquals(T o1, Object o2, MainValueExtractor<T> valueExtractor) {
        if (o1.getClass() != o2.getClass()) {
            return false;
        }
        else if (o1 == o2) {
            return true;
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

    public static <T> int hashCode(Object... args) {
        final int prime = 31;
        int result = 1;
        for (Object arg : args) {
            result = prime * result + ((arg == null) ? 0 : arg.hashCode());
        }
        return result;
    }

    public static <T> Stream<T> iter_to_stream(Iterator<T> iter) {
        Spliterator<T> spliterator;
        if (!iter.hasNext()) {
            spliterator = Spliterators.emptySpliterator();
        }
        else {
            spliterator = Spliterators.spliteratorUnknownSize(iter, Spliterator.IMMUTABLE);
        }
        return StreamSupport.stream(spliterator, false);
    }

    public static <T> String join(Collection<T> c, String separator, Function<T, String> converter) {
        return c.stream().map(converter).collect(Collectors.joining(separator));
    }

    public static String camelForm_to_snakeForm(String camelForm) {
        char[] cs = camelForm.toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(cs[0]));
        for (int i = 1; i < cs.length; i++) {
            if (Character.isUpperCase(cs[i])) {
                sb.append('_');
                sb.append(Character.toLowerCase(cs[i]));
            }
            else {
                sb.append(cs[i]);
            }
        }
        return sb.toString();
    }

    public static String snakeForm_to_camelForm(String snakeForm) {
        boolean upperCase = false;
        StringBuilder sb = new StringBuilder();
        char[] cs = snakeForm.toCharArray();
        for (char c : cs) {
            if (c == '_') {
                upperCase = true;
            }
            else {
                if (upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                }
                else {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        return sb.toString();
    }

}
