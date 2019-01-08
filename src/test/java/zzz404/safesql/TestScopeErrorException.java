package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.dynamic.Scope;
import zzz404.safesql.dynamic.ScopeErrorException;

public class TestScopeErrorException {
    
    @Test
    void test_getMessage() {
        ScopeErrorException e = new ScopeErrorException("zzz", Scope.where);
        assertTrue(e.getMessage().contains("zzz"));
        assertTrue(e.getMessage().contains(Scope.where.name()));
    }
}
