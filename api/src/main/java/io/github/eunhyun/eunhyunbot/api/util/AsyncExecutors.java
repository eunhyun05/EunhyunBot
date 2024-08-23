package io.github.eunhyun.eunhyunbot.api.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AsyncExecutors {

    private static ExecutorService service = null;

    public static ExecutorService getService() {
        if (service == null) {
            service = Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors(),
                    (
                            new BasicThreadFactory.Builder())
                            .namingPattern("EunhyunBotAsyncThread-%d")
                            .daemon(true)
                            .priority(5).uncaughtExceptionHandler((t, e) -> log.warn("EunhyunBot {} Thread 예외 발생.", t.getName())).build());
        }
        return service;
    }

    public static void run(Runnable runnable) {
        getService().execute(new ExceptionHandler(runnable));
    }

    public static void shutdown() {
        if (service != null && service.isTerminated()) {
            service.shutdown();
        }
    }

    @AllArgsConstructor
    public static class ExceptionHandler implements Runnable {

        private final Runnable runnable;

        @Override
        public void run() {
            try {
                runnable.run();
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, runnable.getClass().getName(), e);
            }
        }
    }
}