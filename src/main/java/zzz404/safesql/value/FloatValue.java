package zzz404.safesql.value;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class FloatValue extends TypedValue<Float> {

    @Override
    public void readFromRs(QuietResultSet rs, int index) {
        value = primitiveToObject(rs.getFloat(index), rs);
    }

    @Override
    public void readFromRs(QuietResultSet rs, String columnName) {
        value = primitiveToObject(rs.getFloat(columnName), rs);
    }

    @Override
    public void setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setFloat(index, value);
    }

}
