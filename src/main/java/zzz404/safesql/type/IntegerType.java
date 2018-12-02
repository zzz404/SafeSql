package zzz404.safesql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class IntegerType extends ValueType<Integer> {

    public Integer readFromRs(QuietResultSet rs, int index) {
        return primitiveToObject(rs.getInt(index), rs);
    }

    @Override
    public Integer readFromRs(QuietResultSet rs, String columnName) {
        return primitiveToObject(rs.getInt(columnName), rs);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Integer value) {
        pstmt.setInt(index, value);
    }

}
