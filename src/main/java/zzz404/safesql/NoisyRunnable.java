package zzz404.safesql;

public interface NoisyRunnable {
    public void run() throws Exception;

    public static void runQuiet(NoisyRunnable runnable) {
        try {
            runnable.run();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }
}
