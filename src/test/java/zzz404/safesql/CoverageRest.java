package zzz404.safesql;

import org.junit.jupiter.api.Test;

public class CoverageRest {
    @Test
    void coverage_toString() {
        new OpCondition("", "", "").toString();
        new BetweenCondition("", "", "").toString();
        new InCondition("", "", "").toString();
        
        new OrderBy("", false).toString();
    }
}
