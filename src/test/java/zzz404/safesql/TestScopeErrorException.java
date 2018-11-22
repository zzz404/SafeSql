package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

public class TestScopeErrorException {
    void test_getMessage() {
        ScopeErrorException e = new ScopeErrorException("zzz", Scope.where);
        assertTrue(e.getMessage().contains("zzz"));
        assertTrue(e.getMessage().contains(Scope.where.name()));
    }
}
