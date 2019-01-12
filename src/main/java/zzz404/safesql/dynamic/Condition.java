package zzz404.safesql.dynamic;

public interface Condition {

    public Condition or(Object field, String operator, Object... values);

}
