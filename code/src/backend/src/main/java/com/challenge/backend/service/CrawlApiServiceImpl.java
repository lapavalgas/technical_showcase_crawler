package com.challenge.backend.service;

import com.challenge.backend.Main;
import com.challenge.backend.domain.Crawl;
import com.challenge.backend.exception.CrawlHandleExceptions;
import com.challenge.backend.utils.TimeTravelUtils;
import com.challenge.backend.utils.LogConstants;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class CrawlApiServiceImpl implements CrawlApiService {
    @Override
    public void startCrawler(AtomicReference<Crawl> crawlRef) {
        final CrawlScript crawlScript = new CrawlScript(crawlRef);
        try {
            crawlRef.get().setStatus(Crawl.Status.ACTIVE);
            Main.logger.info(LogConstants.SearchEngine.CRAWLER_STATUS_ACTIVATED);
            crawlScript.activateSearchEngine();
            Main.logger.info(LogConstants.ApiService.SEARCH_ENGINE_STEP_1_INITIALIZED);
            CompletableFuture.runAsync(() -> {
                TimeTravelUtils elapsedTime = new TimeTravelUtils(crawlRef.get().getId());
                try {
                    int scriptExecutionCount = 1;
                    Main.logger.info(LogConstants.ApiService.SEARCH_ENGINE_STEP_2_INITIALIZED);
                    do {
                        crawlScript.scriptLogicCrawler();
                        crawlScript.recheckFailedUrlRequests();
                        scriptExecutionCount += 1;
                    }
                    while (!crawlScript.isFinished());
                    crawlRef.get().setScriptExecutionCount(scriptExecutionCount);
                    crawlRef.get().setMillisElapsedTime(elapsedTime.getDuration());
                    System.gc();
                } catch (URISyntaxException | UnsupportedEncodingException | ExecutionException |
                         InterruptedException e) {
                    CrawlHandleExceptions.genericException(e);
                }
                crawlScript.closeCrawler();
                Main.logger.warn(crawlRef.get().toLog());
            });
        } catch (UnsupportedEncodingException | URISyntaxException | ExecutionException | InterruptedException e) {
            CrawlHandleExceptions.genericException(e);
        }
    }
}
