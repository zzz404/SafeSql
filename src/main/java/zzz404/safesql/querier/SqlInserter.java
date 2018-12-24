package zzz404.safesql.querier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import zzz404.safesql.Entity;
import zzz404.safesql.Field;
import zzz404.safesql.reflection.ClassAnalyzer;
import zzz404.safesql.reflection.MethodAnalyzer;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.sql.TableSchema;
import zzz404.safesql.type.ValueType;
import zzz404.safesql.util.CommonUtils;

public class SqlInserter<T> {

    private DbSourceImpl dbSource;
    private T o;

    public SqlInserter(DbSourceImpl dbSource, T o) {
        this.dbSource = dbSource;
        this.o = o;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public T execute() {
        Class<T> clazz = (Class<T>) o.getClass();
        ClassAnalyzer classAnalyzer = ClassAnalyzer.get(clazz);
        List<MethodAnalyzer> getterAnalyzers = classAnalyzer.get_all_getterAnalyzers();
        Entity<T> entity = new Entity<>(0, clazz);

        List<FieldVo> fieldVos = new ArrayList<>();
        for (MethodAnalyzer analyzer : getterAnalyzers) {
            ValueType<?> valueType = ValueType.get(analyzer.getType());
            if (valueType != null) {
                Field field = new Field(entity, analyzer.getPropertyName());
                fieldVos.add(new FieldVo(valueType, field, analyzer));
            }
        }
        dbSource.revise(entity);

        String columns = fieldVos.stream().map(vo -> vo.field.realColumnName).collect(Collectors.joining(", "));
        String sql = "INSERT INTO " + dbSource.getRealTableName(entity.getVirtualTableName()) + " (" + columns
                + ") VALUES (" + String.join(", ", Collections.nCopies(fieldVos.size(), "?")) + ")";
        dbSource.withConnection(conn -> {
            QuietPreparedStatement pstmt = conn.prepareStatement(sql);
            int i = 0;
            for (FieldVo fieldVo : fieldVos) {
                ValueType valueType = fieldVo.valueType;
                try {
                    valueType.setToPstmt(pstmt, ++i, fieldVo.methodAnalyzer.getMethod().invoke(o));
                }
                catch (Exception e) {
                    throw CommonUtils.wrapToRuntime(e);
                }
            }
            pstmt.executeUpdate();

            TableSchema schema = dbSource.getSchema(entity.getVirtualTableName());
            if (schema.getAutoIncrementColumnName() != null) {
                QuietResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    for (FieldVo fieldVo : fieldVos) {
                        if (schema.getAutoIncrementColumnName().equals(fieldVo.field.realColumnName)) {
                            String propName = fieldVo.field.getPropertyName();
                            MethodAnalyzer analyzer = classAnalyzer.find_setter_by_propertyName(propName);
                            if (analyzer != null) {
                                ValueType valueType = ValueType.get(analyzer.getType());
                                if (valueType != null) {
                                    try {
                                        analyzer.getMethod().invoke(o, valueType.readFromRs(rs, 1));
                                    }
                                    catch (Exception e) {
                                        throw CommonUtils.wrapToRuntime(e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return null;
        });
        return o;
    }

    static class FieldVo {
        public ValueType<?> valueType;
        public MethodAnalyzer methodAnalyzer;
        public Field field;

        public FieldVo(ValueType<?> valueType, Field field, MethodAnalyzer methodAnalyzer) {
            this.valueType = valueType;
            this.field = field;
            this.methodAnalyzer = methodAnalyzer;
        }
    }
}
