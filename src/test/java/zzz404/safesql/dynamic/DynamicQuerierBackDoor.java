package zzz404.safesql.dynamic;

import java.util.List;

import zzz404.safesql.AbstractCondition;

public class DynamicQuerierBackDoor {

    public static List<AbstractCondition> conditions(DynamicQuerier q) {
        return q.conditions;
    }
}
