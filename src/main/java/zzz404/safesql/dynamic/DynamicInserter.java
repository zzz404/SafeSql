package zzz404.safesql.dynamic;

import java.util.Collections;
import java.util.stream.Collectors;

import zzz404.safesql.reflection.OneObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;

public class DynamicInserter<T> extends DynamicObjectExecuter<T> {

    public DynamicInserter(DbSourceImpl dbSource, T o) {
        super(dbSource, o);
    }

    public DynamicInserter<T> values(OneObjectPlayer<T> columnsCollector) {
        super.set(columnsCollector);
        return this;
    }

    protected String sql() {
        return "INSERT INTO " + tableName + " ("
                + fields.stream().map(field -> field.getColumnName()).collect(Collectors.joining(", ")) + ") VALUES ("
                + String.join(", ", Collections.nCopies(fields.size(), "?")) + ")";
    }

}
