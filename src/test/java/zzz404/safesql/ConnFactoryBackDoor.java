package zzz404.safesql;

public class ConnFactoryBackDoor {
    public static void removeAllFactories() {
        DbSource.map.clear();
    }

    public static String getName(DbSource factory) {
        return factory.name;
    }
}
