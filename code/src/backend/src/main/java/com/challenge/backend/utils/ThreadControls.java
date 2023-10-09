package com.challenge.backend.utils;

import com.challenge.backend.exception.CrawlHandleExceptions;

public class ThreadControls {
    public static final int THREAD_NUMBERS = (int) Math.ceil(Runtime.getRuntime().availableProcessors() * 1.6);
    public static final long PROCESSING_DELAY = 500;
    public static final long THREAD_SLEEP = 2000;

    public static void sleep() {
        try {
            Thread.sleep(THREAD_SLEEP);
        } catch (InterruptedException e) {
            CrawlHandleExceptions.interruptedException(e, LogConstants.Error.ERROR_FAIL_SLEEP);
        }
    }
}