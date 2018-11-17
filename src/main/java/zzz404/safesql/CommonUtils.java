package zzz404.safesql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
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

    public static <T> boolean isEquals(T o1, Object o2,
            MainValueExtractor<T> valueExtractor) {
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

    public static <T> Stream<T> iter_to_stream(Iterator<T> iter) {
        Spliterator<T> spliterator;
        if (!iter.hasNext()) {
            spliterator = Spliterators.emptySpliterator();
        }
        else {
            spliterator = Spliterators.spliteratorUnknownSize(iter,
                    Spliterator.IMMUTABLE);
        }
        return StreamSupport.stream(spliterator, false);
    }

    public static <T, U, R> List<R> zip(Iterable<T> iter1, Iterable<U> iter2,
            BiFunction<T, U, R> func) {
        Iterator<T> itr1 = iter1.iterator();
        Iterator<U> itr2 = iter2.iterator();

        ArrayList<R> result = new ArrayList<>();
        while (itr1.hasNext()) {
            if (!itr2.hasNext()) {
                break;
            }
            R r = func.apply(itr1.next(), itr2.next());
            result.add(r);
        }
        return result;
    }

}
