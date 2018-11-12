package zzz404.safesql.sql;

import java.sql.ResultSet;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JpqlDao {

    public static Stream<ResultSet> toRsStream(ResultSet rs, Integer start, Integer limit) {
        ResultSetIterator iter = new ResultSetIterator(rs, start, limit);
        Spliterator<ResultSet> spliterator;
        if (!iter.hasNext()) {
            spliterator = Spliterators.emptySpliterator();
        }
        else {
            spliterator = Spliterators.spliteratorUnknownSize(iter, Spliterator.IMMUTABLE);
        }
        return StreamSupport.stream(spliterator, false);
    }

}
