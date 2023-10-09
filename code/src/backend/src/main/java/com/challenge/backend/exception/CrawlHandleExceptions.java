package com.challenge.backend.exception;

import com.challenge.backend.Main;
import com.challenge.backend.domain.Crawl;
import com.challenge.backend.utils.LogConstants;

import java.util.concurrent.atomic.AtomicReference;

public class CrawlHandleExceptions {
    private static final Object lock = new Object();

    public static void genericException(Exception e) {
        Main.logger.error(LogConstants.Error.ERROR_CLASS, e.getClass());
        Main.logger.error(LogConstants.Error.ERROR_MSG, e.getMessage());
    }

    public static void interruptedException(Exception e, String log) {
        Thread.currentThread().interrupt();
        Main.logger.error(LogConstants.Error.ERROR_CLASS, e.getClass());
        Main.logger.error(LogConstants.Error.ERROR_MSG, e.getMessage());
        if (log != null) {
            Main.logger.info(log);
        }
    }

    public static String interruptedWhileRequesting(Exception e, String log, String url, AtomicReference<Crawl> atomicCrawl) {
        Thread.currentThread().interrupt();
        Main.logger.info(log, url);
        synchronized (lock) {
            atomicCrawl.get().addMalformedUrl(url);
        }
        Main.logger.info(LogConstants.SearchEngine.CRAWLER_ADD_MALFORMED_URL, url);
        Main.logger.error(LogConstants.Error.ERROR_CLASS, e.getClass());
        Main.logger.error(LogConstants.Error.ERROR_MSG, e.getMessage());
        return null;
    }

    public static String whileRequesting(String url, AtomicReference<Crawl> atomicCrawl) {
        synchronized (lock) {
            atomicCrawl.get().addMalformedUrl(url);
        }
        Main.logger.info(LogConstants.SearchEngine.CRAWLER_ADD_MALFORMED_URL, url);
        return null;
    }
}
