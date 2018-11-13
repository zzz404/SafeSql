package zzz404.safesql;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrCondition extends Condition {

    public List<Condition> subConditions;

    protected OrCondition(Condition... subConditions) {
        super(null);
        this.subConditions = Arrays.asList(subConditions);
    }

    public <T> Condition or(T field, String operator, Object... values) {
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
            PreparedStatement pstmt) {
        for (Condition cond : subConditions) {
            i = cond.setValueToPstmt_and_returnNextIndex(i, pstmt);
        }
        return i;
    }

}
