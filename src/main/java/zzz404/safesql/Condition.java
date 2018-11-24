package zzz404.safesql;

public interface Condition {

    public <T> Condition or(T field, String operator, Object... values);
    
    public <T> void as(T field);
}
