package zzz404.safesql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConditionBuilder {

    static ThreadLocal<ConditionBuilder> instance = new ThreadLocal<>();

    public List<String> operators = new ArrayList<>();
    public List<Object[]> values = new ArrayList<>();

    public List<Condition> buildConditions(List<String> fields) {
        List<Condition> result = new ArrayList<>();

        Iterator<String> field_iter = fields.iterator();
        Iterator<Object[]> values_iter = values.iterator();

        for (String op : operators) {
            String field = field_iter.next();
            Object[] values = values_iter.next();
            result.add(Condition.of(field, op, values));
        }
        return result;
    }
}
