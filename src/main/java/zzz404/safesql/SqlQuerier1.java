package zzz404.safesql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;

import net.sf.cglib.proxy.Enhancer;

public class SqlQuerier1<T> extends SqlQuerier {

    private Class<T> clazz;
    List<String> fields = Collections.emptyList();
    List<Condition> conditions = Collections.emptyList();

    public SqlQuerier1(Class<T> clazz) {
        this.clazz = clazz;
    }

    private GetterLogger<T> createGetterLogger() {
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);
        GetterLogger<T> getterLogger = new GetterLogger<>(clazz);
        en.setCallback(getterLogger);
        @SuppressWarnings("unchecked")
        T mockedObject = (T) en.create();
        getterLogger.mockedObject = mockedObject;
        return getterLogger;
    }

    public SqlQuerier1<T> select(Consumer<T> consumer) {
        GetterLogger<T> getterLogger = createGetterLogger();
        consumer.accept(getterLogger.mockedObject);
        this.fields = new ArrayList<>(
                new LinkedHashSet<>(getterLogger.calledGetterProperties));
        return this;
    }

    public SqlQuerier1<T> where(Consumer<T> consumer) {
        ConditionBuilder builder = new ConditionBuilder();
        ConditionBuilder.instance.set(builder);

        GetterLogger<T> getterLogger = createGetterLogger();
        consumer.accept(getterLogger.mockedObject);
        List<String> fields = getterLogger.calledGetterProperties;

        this.conditions = builder.buildConditions(fields);

        ConditionBuilder.instance.set(null);
        return this;
    }

    public SqlQuerier1<T> orderBy(Consumer<T> consumer) {
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

    @Override
    protected SqlQuerier1<T> build() {
        if (fields.isEmpty()) {
            fields = Arrays.asList("*");
        }
        return this;
    }

}
