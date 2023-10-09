package com.challenge.backend.helper;

import com.challenge.backend.Main;
import com.challenge.backend.domain.Crawl;
import com.challenge.backend.exception.CrawlHandleExceptions;
import com.challenge.backend.utils.CrawlConstants;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import static com.challenge.backend.utils.LogConstants.HttpRequest.*;
import static com.challenge.backend.utils.LogConstants.SearchEngine.CRAWLER_PROCESSES_URL;
import static com.challenge.backend.utils.LogConstants.SearchEngine.CRAWLER_VISITED_URL;

public class HttpAsyncRequestHelper {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static String requestUrl(String url, AtomicReference<Crawl> atomicCrawl) throws ExecutionException, InterruptedException {

        atomicCrawl.get().synchronizedProcessUrl(url);
        Main.logger.info(CRAWLER_PROCESSES_URL, url);

        if (URLFormatterHelper.isValidURL(url) && URLFormatterHelper.isSameDomain(CrawlConstants.URL.BASE, url)) {
            Main.logger.info(VALIDATED_PATTERN_DOMAIN, url);

            try {
                Main.logger.info(SETUP_HTTP_CLIENT, httpClient);

                final HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
                Main.logger.info(SETUP_HTTP_REQUEST, request);

                return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(httpResponse -> {
                            Main.logger.info(REQUESTED_URL, url);

                            final int statusCode = httpResponse.statusCode();
                            Main.logger.info(RESPONSE_STATUS_CODE, statusCode);

                            atomicCrawl.get().addVisitedUrls(url);
                            Main.logger.info(CRAWLER_VISITED_URL, url);

                            if (statusCode == 200) {
                                return httpResponse.body();
                            } else {
                                return null;
                            }
                        })
                        .exceptionally(e -> CrawlHandleExceptions.interruptedWhileRequesting((Exception) e, HTTP_CLIENT_FAIL, url, atomicCrawl)).get();
            } catch (Exception e) {
                return CompletableFuture.completedFuture(CrawlHandleExceptions.interruptedWhileRequesting(e, HTTP_CLIENT_FAIL, url, atomicCrawl)).get();
            }
        }
        return CompletableFuture.completedFuture(CrawlHandleExceptions.whileRequesting(url, atomicCrawl)).get();
    }
}