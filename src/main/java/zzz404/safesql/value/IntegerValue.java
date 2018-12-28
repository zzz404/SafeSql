package zzz404.safesql.value;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class IntegerValue extends TypedValue<Integer> {

    @Override
    public void readFromRs(QuietResultSet rs, int index) {
        value = primitiveToObject(rs.getInt(index), rs);
    }

    @Override
    public void readFromRs(QuietResultSet rs, String columnName) {
        value = primitiveToObject(rs.getInt(columnName), rs);
    }

    @Override
    public void setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setInt(index, value);
    }

}
