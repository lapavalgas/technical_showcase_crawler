package com.challenge.backend.helper;

import com.challenge.backend.Main;
import com.challenge.backend.domain.Crawl;
import com.challenge.backend.exception.CrawlHandleExceptions;
import com.challenge.backend.utils.CrawlConstants;
import com.challenge.backend.utils.LogConstants;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicReference;

public class HttpRequestHelper {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * ProcessedList is any URL that has been requested by CrawlScriptHelper
     * ProcessedList will be a controller for the end the aplication execution
     * VisitedList is any URL requested by the HttpClient
     * VisitedList will ignore malformed URL that this CrawlerScriptHelper couldn't fix
     */
    public static String requestUrl(String url, AtomicReference<Crawl> atomicCrawl) {
        atomicCrawl.get().synchronizedProcessUrl(url);
        Main.logger.info(LogConstants.SearchEngine.CRAWLER_PROCESSES_URL, url);

        if (URLFormatterHelper.isValidURL(url) && URLFormatterHelper.isSameDomain(CrawlConstants.URL.BASE, url)) {
            Main.logger.info(LogConstants.HttpRequest.VALIDATED_PATTERN_DOMAIN, url);

            try {
                Main.logger.info(LogConstants.HttpRequest.SETUP_HTTP_CLIENT, httpClient);

                final HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
                Main.logger.info(LogConstants.HttpRequest.SETUP_HTTP_REQUEST, request);

                final HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                Main.logger.info(LogConstants.HttpRequest.REQUESTED_URL, url);

                final int statusCode = httpResponse.statusCode();
                Main.logger.info(LogConstants.HttpRequest.RESPONSE_STATUS_CODE, statusCode);

                atomicCrawl.get().addVisitedUrls(url);
                Main.logger.info(LogConstants.SearchEngine.CRAWLER_VISITED_URL, url);

                if (statusCode == 200) {
                    return httpResponse.body();
                } else {
                    return null;
                }
            } catch (Exception e) {
                return CrawlHandleExceptions.interruptedWhileRequesting(e, LogConstants.HttpRequest.HTTP_CLIENT_FAIL, url, atomicCrawl);
            }
        }
        return CrawlHandleExceptions.whileRequesting(url, atomicCrawl);
    }
}