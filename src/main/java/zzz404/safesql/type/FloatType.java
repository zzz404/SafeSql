package zzz404.safesql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class FloatType extends ValueType<Float> {

    public Float readFromRs(QuietResultSet rs, int index) {
        return primitiveToObject(rs.getFloat(index), rs);
    }

    @Override
    public Float readFromRs(QuietResultSet rs, String columnName) {
        return primitiveToObject(rs.getFloat(columnName), rs);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Float value) {
        pstmt.setFloat(index, value);
    }

}
