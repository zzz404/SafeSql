package zzz404.safesql;

public class ConnFactoryBackDoor {
    public static void removeAllFactories() {
        ConnectionFactory.map.clear();
    }

    public static String getName(ConnectionFactory factory) {
        return factory.name;
    }
}
