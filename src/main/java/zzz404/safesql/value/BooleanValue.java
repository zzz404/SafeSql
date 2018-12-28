package zzz404.safesql.value;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class BooleanValue extends TypedValue<Boolean> {

    @Override
    public void readFromRs(QuietResultSet rs, int index) {
        value = primitiveToObject(rs.getBoolean(index), rs);
    }

    @Override
    public void readFromRs(QuietResultSet rs, String columnName) {
        value = primitiveToObject(rs.getBoolean(columnName), rs);
    }

    @Override
    public void setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setBoolean(index, value);
    }

}
