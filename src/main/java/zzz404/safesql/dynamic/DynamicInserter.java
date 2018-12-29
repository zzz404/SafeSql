package zzz404.safesql.dynamic;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;

import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.OrMapper;
import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class DynamicInserter<T> {

    private DbSourceImpl dbSource;
    private T o;

    public DynamicInserter(DbSourceImpl dbSource, T o) {
        this.dbSource = dbSource;
        this.o = o;
    }

    @SuppressWarnings({ "unchecked" })
    public T execute() {
        OrMapper<T> mapper = OrMapper.get((Class<T>) o.getClass(), dbSource);
        Collection<String> realColumnNames = mapper.get_realColumnName_of_all_getters();
        String sql = "INSERT INTO " + mapper.getRealTableName() + " (" + StringUtils.join(realColumnNames, ", ")
                + ") VALUES (" + String.join(", ", Collections.nCopies(realColumnNames.size(), "?")) + ")";
        
        for (String columnName : realColumnNames) {
            mapper.getValue(o, columnName);
        }

        
        dbSource.withConnection(conn -> {
            QuietPreparedStatement pstmt = conn.prepareStatement(sql);
            int i = 0;
            for (String columnName : realColumnNames) {
                mapper.setValueToPstmt(pstmt, i++, o, columnName);
            }
            pstmt.executeUpdate();

            String realColumnName_of_autoIncrement = mapper.getTableSchema().getRealColumnName_of_autoIncrement();
            if (realColumnName_of_autoIncrement != null) {
                QuietResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    mapper.setValueToObject(o, realColumnName_of_autoIncrement, rs, 1);
                }
            }
            return null;
        });
        return o;
    }

}
