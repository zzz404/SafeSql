package zzz404.safesql.sql;

import java.util.Collection;

import zzz404.safesql.reflection.MethodAnalyzer;
import zzz404.safesql.reflection.ObjectSchema;
import zzz404.safesql.sql.ResultSetAnalyzer.ColumnInfo;
import zzz404.safesql.sql.proxy.QuietResultSet;
import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.NoisyRunnable;
import zzz404.safesql.util.NoisySupplier;

public class OrMapper {
    private QuietResultSet rs;
    private DbSourceImpl dbSource;

    private ResultSetAnalyzer rsAnalyzer;

    public OrMapper(QuietResultSet rs, DbSourceImpl dbSource) {
        this.rs = rs;
        this.dbSource = dbSource;
        this.rsAnalyzer = new ResultSetAnalyzer(rs);
    }

    public <T> T mapToObject(Class<T> clazz, boolean matchTableName) {
        T o = NoisySupplier.getQuietly(() -> clazz.newInstance());

        Collection<ColumnInfo> columnInfos;
        if (matchTableName) {
            columnInfos = rsAnalyzer.getAllColumns(dbSource.getTableName(clazz.getSimpleName().toLowerCase()));
        }
        else {
            columnInfos = rsAnalyzer.getAllColumns();
        }
        ObjectSchema objSchema = ObjectSchema.get(clazz);
        NoisyRunnable.runQuietly(() -> {
            for (ColumnInfo columnInfo : columnInfos) {
                MethodAnalyzer setter = objSchema.findSetter_by_lcPropName(columnInfo.lcColumn);
                if (setter == null && dbSource.isSnakeFormCompatable()) {
                    setter = objSchema.findValidSetter_by_snakedPropName(columnInfo.lcColumn);
                }
                if (setter == null) {
                    throw new MappingException(clazz, columnInfo.column);
                }
                if (setter != null) {
                    TypedValue<?> value = TypedValue.valueOf(setter.getType()).readFromRs(rs, columnInfo.index);
                    setter.getMethod().invoke(o, value.getValue());
                }
            }
        });
        return o;
    }

}
