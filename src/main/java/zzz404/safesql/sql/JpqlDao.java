package zzz404.safesql.sql;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JpqlDao {

    public static Stream<QuietResultSet> toRsStream(QuietResultSet rs,
            Integer start, Integer limit) {
        QuietResultSetIterator iter = new QuietResultSetIterator(rs, start,
                limit);
        Spliterator<QuietResultSet> spliterator;
        if (!iter.hasNext()) {
            spliterator = Spliterators.emptySpliterator();
        }
        else {
            spliterator = Spliterators.spliteratorUnknownSize(iter,
                    Spliterator.IMMUTABLE);
        }
        return StreamSupport.stream(spliterator, false);
    }

}
