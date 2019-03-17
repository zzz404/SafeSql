package zzz404.safesql.dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import zzz404.safesql.reflection.MethodAnalyzer;
import zzz404.safesql.reflection.ObjectSchema;
import zzz404.safesql.reflection.OneObjectPlayer;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.TableSchema;
import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.NoisyRunnable;

abstract class DynamicObjectExecuter<T> extends DynamicExecuter<T> {

    protected T o;
    protected List<FieldImpl> fields;
    protected ObjectSchema objSchema;

    @SuppressWarnings("unchecked")
    public DynamicObjectExecuter(DbSourceImpl dbSource, T o) {
        super(dbSource, (Class<T>) o.getClass());
        this.o = o;
        this.objSchema = ObjectSchema.get(o.getClass());
    }

    protected DynamicObjectExecuter<T> collectFields(OneObjectPlayer<T> columnsCollector) {
        QueryContext.underQueryContext(ctx -> {
            NoisyRunnable.runQuietly(() -> columnsCollector.play(entity.getMockedObject_for_traceGetter()));
            fields = ctx.takeAllTableFieldsUniquely();
            fields.forEach(FieldImpl::checkType);
        });
        return this;
    }

    protected abstract String sql();

    protected List<TypedValue<?>> paramValues() {
        List<TypedValue<?>> paramValues = new ArrayList<>();
        for (FieldImpl field : fields) {
            String propName = field.getPropertyName();
            Object value = objSchema.findGetter_by_propName(propName).getValue(o);
            paramValues.add(TypedValue.valueOf(value));
        }
        paramValues.addAll(super.paramValues());
        return paramValues;
    }

    public int execute() {
        if (CollectionUtils.isEmpty(fields)) {
            TableSchema tableSchema = dbSource.getSchema(entity.getName());
            fields = objSchema.findAllValidGetters().stream().map(MethodAnalyzer::getPropertyName)
                    .filter(propName -> tableSchema.hasMatchedColumn(propName))
                    .map(propName -> new FieldImpl(entity, propName)).collect(Collectors.toList());
        }
        return super.execute();
    }

}
