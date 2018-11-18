package zzz404.safesql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class FloatType extends ValueType<Float> {

    public Float readFromRs(QuietResultSet rs, int index) {
        return rs.getFloat(index);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Float value) {
        pstmt.setFloat(index, value);
    }

}
