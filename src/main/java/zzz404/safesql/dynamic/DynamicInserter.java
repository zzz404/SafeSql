package zzz404.safesql.dynamic;

import java.util.Collections;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;

import zzz404.safesql.reflection.OneObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;

public class DynamicInserter<T> extends DynamicExecuter<T> {

    public DynamicInserter(DbSourceImpl dbSource, Class<T> clazz) {
        super(dbSource, clazz);
    }

    public DynamicInserter<T> values(OneObjectPlayer<T> columnsCollector) {
        super.collectFields(columnsCollector);
        return this;
    }

    protected String sql() {
        Validate.isTrue(CollectionUtils.isNotEmpty(fields));
        return "INSERT INTO " + tableName + " ("
                + fields.stream().map(field -> field.getColumnName()).collect(Collectors.joining(", ")) + ") VALUES ("
                + String.join(", ", Collections.nCopies(fields.size(), "?")) + ")";
    }

}
