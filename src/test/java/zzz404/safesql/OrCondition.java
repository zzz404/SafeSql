package zzz404.safesql;

import java.util.List;

public class OrCondition extends Condition {

    public List<Condition> subConditions;

    protected OrCondition(Condition cond1) {
        super(null);
        
    }

}
