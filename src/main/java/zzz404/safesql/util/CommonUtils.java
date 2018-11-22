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

    //    public static <T, U> List<Tuple2<T, U>> zip(List<T> list, U[] array) {
    //        ArrayList<Tuple2<T, U>> result = new ArrayList<>();
    //
    //        Iterator<T> iter = list.iterator();
    //        for (U u : array) {
    //            if (!iter.hasNext()) {
    //                break;
    //            }
    //            result.add(new Tuple2<T, U>(iter.next(), u));
    //        }
    //        return result;
    //    }
    //
    //    public static <T, U, R> List<R> zip(List<T> list, U[] array, BiFunction<T, U, R> func) {
    //        ArrayList<R> result = new ArrayList<>();
    //
    //        Iterator<T> iter = list.iterator();
    //        for (U u : array) {
    //            if (!iter.hasNext()) {
    //                break;
    //            }
    //            R r = func.apply(iter.next(), u);
    //            result.add(r);
    //        }
    //        return result;
    //    }

}
