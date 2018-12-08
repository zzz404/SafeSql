package zzz404.safesql;

public class FieldBackDoor {
    public static String getRealColumnName(Field field) {
        return field.realColumnName;
    }
}
