package zzz404.safesql.querier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import zzz404.safesql.AbstractCondition;
import zzz404.safesql.Field;
import zzz404.safesql.QueryContext;
import zzz404.safesql.reflection.ClassAnalyzer;
import zzz404.safesql.reflection.MethodAnalyzer;
import zzz404.safesql.reflection.OneObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.type.ValueType;
import zzz404.safesql.util.NoisyRunnable;

public class SqlUpdater<T> extends SqlDeleter<T> {

    private T o;
    private List<Field> fields = Collections.emptyList();

    @SuppressWarnings("unchecked")
    public SqlUpdater(DbSourceImpl dbSource, T o) {
        super(dbSource, (Class<T>) o.getClass());
        this.o = o;
    }

    public SqlUpdater<T> set(OneObjectPlayer<T> columnsCollector) {
        QueryContext.underQueryContext(ctx -> {
            NoisyRunnable.runQuietly(() -> columnsCollector.play(entity.getMockedObject()));
            this.fields = ctx.takeAllTableFieldsUniquely();
        });
        return this;
    }

    @Override
    protected String sql() {
        dbSource.revise(entity);
        if (fields.isEmpty()) {
            eval_all_fitableFields();
        }
        else {
            ClassAnalyzer classAnalyzer = ClassAnalyzer.get(o.getClass());
            fields.stream().filter(field -> {
                MethodAnalyzer analyzer = classAnalyzer.find_getter_by_propertyName(field.getPropertyName());
                ValueType<? extends Object> valueType = ValueType.get(analyzer.getMethod().getReturnType());
                return valueType != null;
            }).collect(Collectors.toList());
        }
        String tableName = dbSource.getRealTableName(entity.getVirtualTableName());
        String sql = "UPDATE " + tableName + " SET "
                + fields.stream().map(field -> field.realColumnName + "=?").collect(Collectors.joining(", "));
        if (!this.conditions.isEmpty()) {
            sql += " WHERE "
                    + this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
        }
        return sql;
    }

    protected void eval_all_fitableFields() {
        ClassAnalyzer classAnalyzer = ClassAnalyzer.get(o.getClass());
        List<MethodAnalyzer> analyzers = classAnalyzer.get_all_getterAnalyzers();
        fields = new ArrayList<>();
        for (MethodAnalyzer analyzer : analyzers) {
            ValueType<?> valueType = ValueType.get(analyzer.getType());
            if (valueType != null) {
                Field field = new Field(entity, analyzer.getPropertyName());
                fields.add(field);
            }
        }
    }

    public int execute() {
        return dbSource.withConnection(conn -> {
            QuietPreparedStatement pstmt = conn.prepareStatement(sql());
            int i = 1;
            ClassAnalyzer classAnalyzer = ClassAnalyzer.get(o.getClass());

            for (Field field : fields) {
                MethodAnalyzer analyzer = classAnalyzer.find_getter_by_propertyName(field.getPropertyName());
                Object value;
                try {
                    value = analyzer.getMethod().invoke(o);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
                ValueType<Object> valueType = ValueType.get(analyzer.getMethod().getReturnType());
                if (valueType != null) {
                    valueType.setToPstmt(pstmt, i++, value);
                }
            }

            for (Object paramValue : paramValues()) {
                ValueType.setValueToPstmt(pstmt, i++, paramValue);
            }
            return pstmt.executeUpdate();
        });
    }

}
