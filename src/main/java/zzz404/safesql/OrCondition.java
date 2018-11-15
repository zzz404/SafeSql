package zzz404.safesql;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import zzz404.safesql.sql.QuietPreparedStatement;

public class OrCondition extends Condition {

    public List<Condition> subConditions;

    protected OrCondition(Condition... subConditions) {
        super(null);
        this.subConditions = Arrays.asList(subConditions);
    }

    public <T> OrCondition or(T field, String operator, Object... values) {
        QueryContext ctx = QueryContext.INSTANCE.get();
        Condition cond = Condition.of(ctx.takeColumnName(), operator, values);
        subConditions.add(cond);
        return this;
    }

    @Override
    public String toClause() {
        return "(" + subConditions.stream().map(c -> c.toClause())
                .collect(Collectors.joining(" OR ")) + ")";
    }

    @Override
    protected int do_setValueToPstmt_and_returnNextIndex(int i,
            QuietPreparedStatement pstmt) throws SQLException {
        for (Condition cond : subConditions) {
            i = cond.do_setValueToPstmt_and_returnNextIndex(i, pstmt);
        }
        return i;
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> subConditions.toArray());
    }

}
