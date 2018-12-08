package zzz404.safesql.util;

public interface NoisyRunnable {
    public void run() throws Exception;

    public static void runQuietly(NoisyRunnable runnable) {
        try {
            runnable.run();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public static void runIgnoreException(NoisyRunnable runnable) {
        try {
            runnable.run();
        }
        catch (Exception ignored) {
        }
    }
}
