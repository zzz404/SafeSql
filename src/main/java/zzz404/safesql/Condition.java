package zzz404.safesql;

public interface Condition {

    public <T> Condition or(T field, String operator, @SuppressWarnings("unchecked") T... values);

}
