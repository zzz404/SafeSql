package zzz404.safesql;

import java.util.Collections;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import zzz404.safesql.sql.QuietPreparedStatement;

public abstract class DynamicQuerier extends SqlQuerier {

    protected List<String> columnNames = Collections.emptyList();
    protected List<Condition> conditions = Collections.emptyList();
    protected List<OrderBy> orderBys = Collections.emptyList();

    protected <T> T createMockedObject(Class<T> clazz) {
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);
        GetterTracer<T> getterLogger = new GetterTracer<>(clazz);
        en.setCallback(getterLogger);

        @SuppressWarnings("unchecked")
        T mockedObject = (T) en.create();
        return mockedObject;
    }

    @Override
    protected abstract String buildSql();

    @Override
    protected abstract String buildSql_for_queryCount();

    @Override
    protected void setCondValueToPstmt(QuietPreparedStatement pstmt) {
        int i = 1;
        for (Condition cond : conditions) {
            i = cond.setValueToPstmt_and_returnNextIndex(i, pstmt);
        }
    }

    public abstract Object queryOne();

    public abstract List<?> queryList();

    public abstract Page<?> queryPage();

}
