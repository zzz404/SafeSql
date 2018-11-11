package zzz404.safesql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import net.sf.cglib.proxy.Enhancer;

public class SqlQuerier1<T> extends SqlQuerier {

    private Class<T> clazz;

    public SqlQuerier1(Class<T> clazz) {
        this.clazz = clazz;
        this.fields = Arrays.asList("*");
    }

    private T createMockedObject() {
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);
        GetterLogger<T> getterLogger = new GetterLogger<>(clazz);
        en.setCallback(getterLogger);
        
        @SuppressWarnings("unchecked")
        T mockedObject = (T) en.create();
        return mockedObject;
    }

    public SqlQuerier1<T> select(Consumer<T> consumer) {
        T mockedObject = createMockedObject();
        consumer.accept(mockedObject);
        this.fields = new ArrayList<>(new LinkedHashSet<>(
                QueryContext.INSTANCE.get().takeAllColumnNames()));
        return this;
    }

    public SqlQuerier1<T> where(Consumer<T> consumer) {
        T mockedObject = createMockedObject();
        consumer.accept(mockedObject);
        this.conditions = QueryContext.INSTANCE.get().conditions;
        return this;
    }

    public SqlQuerier1<T> orderBy(Consumer<T> consumer) {
        T mockedObject = createMockedObject();
        consumer.accept(mockedObject);
        this.orderBys = QueryContext.INSTANCE.get().orderBys;
        return this;
    }

    @Override
    public SqlQuerier1<T> offset(int offset) {
        super.offset(offset);
        return this;
    }

    @Override
    public SqlQuerier1<T> limit(int limit) {
        super.limit(limit);
        return this;
    }

    public String buildSql() {
        String sql = "SELECT " + StringUtils.join(fields, ", ") + " FROM ";
        return "";
    }
}
