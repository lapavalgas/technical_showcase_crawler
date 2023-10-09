package com.challenge.backend.service;

import com.challenge.backend.Main;
import com.challenge.backend.domain.Crawl;
import com.challenge.backend.exception.CrawlHandleExceptions;
import com.challenge.backend.helper.HttpRequestHelper;
import com.challenge.backend.helper.URLFormatterHelper;
import com.challenge.backend.utils.CrawlConstants;
import com.challenge.backend.utils.ThreadControls;
import com.challenge.backend.utils.LogConstants;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlScript {
    private static final Pattern patternLinkRecognition = Pattern.compile(CrawlConstants.RegexPatterns.LINK_RECOGNITION_PATTERN);
    private final Pattern patternKeyword;
    private final ExecutorService executorService;
    private final AtomicReference<Crawl> crawlRef;
    private final Set<String> allUrlFoundInTheResponse;
    private final Object lock;
    private final Object lockAtomicCrawlUrl;
    private final Object lockAtomicCrawlUrlFound;
    private final Object lockAllUrlFoundInTheResponse;
    private final Set<String> urlList;

    CrawlScript(AtomicReference<Crawl> crawlRef) {
        executorService = Executors.newFixedThreadPool(ThreadControls.THREAD_NUMBERS);
        Main.logger.info(LogConstants.SearchEngine.CRAWLER_EXECUTOR_SERVICE_INITIALIZED);
        this.crawlRef = crawlRef;
        allUrlFoundInTheResponse = ConcurrentHashMap.newKeySet();
        urlList = ConcurrentHashMap.newKeySet();

        lock = new Object();
        lockAtomicCrawlUrl = new Object();
        lockAtomicCrawlUrlFound = new Object();
        lockAllUrlFoundInTheResponse = new Object();

        patternKeyword = Pattern.compile(crawlRef.get().getKeyword(), Pattern.CASE_INSENSITIVE);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isVisitedURL(String url) {
        synchronized (lock) {
            return crawlRef.get().getVisitedUrls().contains(url);
        }
    }

    private boolean isKeywordInResponseContent(String htmlContent, String keyword) {
        final Matcher matcher = patternKeyword.matcher(htmlContent);
        return matcher.find();
    }

    private Set<String> extractAllLinksInAHTMLResponse(String response) {
        final Matcher matcher = patternLinkRecognition.matcher(response);
        while (matcher.find()) {
            final String preUrl = URLFormatterHelper.formatRelativePathAndAbsolutUrl(matcher.group(2));
            final String url = URLFormatterHelper.crawlCustomFormatURLForFixALastProblem(preUrl);
            if (URLFormatterHelper.isSameDomain(CrawlConstants.URL.BASE, url)) {
                this.urlList.add(url);
            }
        }
        return urlList;
    }

    public void activateSearchEngine() throws UnsupportedEncodingException, URISyntaxException, ExecutionException, InterruptedException {
        final String response = HttpRequestHelper.requestUrl(CrawlConstants.URL.BASE, this.crawlRef);
        Main.logger.info(LogConstants.SearchEngine.CRAWLER_REQUESTED_URL, CrawlConstants.URL.BASE);
        if (response != null) {
            if (isKeywordInResponseContent(response, this.crawlRef.get().getKeyword())) {
                Main.logger.info(LogConstants.SearchEngine.CRAWLER_FIND_MATCH_KEYWORD, this.crawlRef.get().getKeyword(), CrawlConstants.URL.BASE);
                this.crawlRef.get().addUrl(CrawlConstants.URL.BASE);
                Main.logger.info(LogConstants.SearchEngine.CRAWLER_PUT_URL, CrawlConstants.URL.BASE);
            }
            this.crawlRef.get().addAllFoundUrl(extractAllLinksInAHTMLResponse(response));
            Main.logger.info(LogConstants.SearchEngine.CRAWLER_UPDATED_FOUND_URL_LIST);
        }
    }

    public void scriptLogicCrawler() throws UnsupportedEncodingException, URISyntaxException {
        for (String url : this.crawlRef.get().getFoundUrls()) {
            executorService.submit(() -> {
                try {
                    processUnvisitedUrl(url);
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        ThreadControls.sleep();
        Main.logger.info(LogConstants.SearchEngine.CRAWLER_THREAD_SLEEP, ThreadControls.THREAD_SLEEP);
        synchronizedAtomicCrawlAddAllFoundUrl();
        executeScriptUntilCompletion();
        executorServiceShutdown();
        Main.logger.info(LogConstants.SearchEngine.CRAWLER_FINISH_SEARCH_JOB, this.crawlRef.get().getId(), this.crawlRef.get().getKeyword());
    }

    private void processUnvisitedUrl(String url) throws ExecutionException, InterruptedException {
        if (!isVisitedURL(url)) {
            Main.logger.info(LogConstants.SearchEngine.CRAWLER_EXECUTOR_SERVICE_STARTED, this.crawlRef.get().getId(), this.crawlRef.get().getKeyword());
            final String response = HttpRequestHelper.requestUrl(url, this.crawlRef);
            Main.logger.info(LogConstants.SearchEngine.CRAWLER_REQUESTED_URL, url);
            if (response != null) {
                try {
                    validateKeywordAndUpdateUrlList(response, url);
                    synchronizedAtomicCrawlAddAllFoundUrl(response);
                } catch (UnsupportedEncodingException | URISyntaxException e) {
                    CrawlHandleExceptions.genericException(e);
                }
            }
        }
    }

    private void validateKeywordAndUpdateUrlList(String response, String url) {
        if (isKeywordInResponseContent(response, this.crawlRef.get().getKeyword())) {
            Main.logger.info(LogConstants.SearchEngine.CRAWLER_FIND_MATCH_KEYWORD, this.crawlRef.get().getKeyword(), url);
            synchronizedAtomicCrawlAddUrl(url);
        }
    }

    private void synchronizedAtomicCrawlAddUrl(String url) {
        synchronized (lock) {
            synchronized (lockAtomicCrawlUrl) {
                this.crawlRef.get().addUrl(url);
                Main.logger.info(LogConstants.SearchEngine.CRAWLER_PUT_URL, url);
            }
        }
    }

    private void synchronizedAtomicCrawlAddAllFoundUrl(String response) throws UnsupportedEncodingException, URISyntaxException {
        synchronized (lock) {
            synchronized (lockAllUrlFoundInTheResponse) {
                allUrlFoundInTheResponse.addAll(extractAllLinksInAHTMLResponse(response));
                Main.logger.info(LogConstants.SearchEngine.CRAWLER_UPDATED_PROVISORY_FOUND_URL_LIST);
            }
        }
    }

    private void synchronizedAtomicCrawlAddAllFoundUrl() {
        synchronized (lock) {
            synchronized (lockAtomicCrawlUrlFound) {
                this.crawlRef.get().addAllFoundUrl(allUrlFoundInTheResponse);
                Main.logger.info(LogConstants.SearchEngine.CRAWLER_FOUND_URL_LIST_UPDATED);
            }
        }
    }

    private void executeScriptUntilCompletion() throws UnsupportedEncodingException, URISyntaxException {
        if (!isFinished()) {
            Main.logger.info(LogConstants.SearchEngine.CRAWLER_CONTINUES);
            scriptLogicCrawler();
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isFinished() {
        synchronized (lock) {
            return crawlRef.get().getProcessedUrls().containsAll(crawlRef.get().getFoundUrls());
        }
    }

    private void executorServiceShutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ie) {
            CrawlHandleExceptions.interruptedException(ie, LogConstants.SearchEngine.THREAD_STOP_FAILURE);
        }
    }

    public void recheckFailedUrlRequests() throws UnsupportedEncodingException, URISyntaxException, ExecutionException, InterruptedException {
        boolean isThereMoreUrls = false;
        Main.logger.info(LogConstants.SearchEngine.CRAWLER_DOBLECHECK_STARTS);
        for (String url : this.crawlRef.get().getMalformedUrl()) {
            if (!isVisitedURL(url)) {
                final String response = HttpRequestHelper.requestUrl(url, this.crawlRef);
                if (response != null) {
                    if (isKeywordInResponseContent(response, this.crawlRef.get().getKeyword())) {
                        this.crawlRef.get().addUrl(url);
                    }
                    final Set<String> urls = extractAllLinksInAHTMLResponse(response);
                    allUrlFoundInTheResponse.addAll(urls);
                    if (!urls.isEmpty()) {
                        isThereMoreUrls = true;
                    }
                }
            }
        }
        this.crawlRef.get().addAllFoundUrl(allUrlFoundInTheResponse);
        Main.logger.info(LogConstants.SearchEngine.CRAWLER_DOBLECHECK_FINISH);
        if (isThereMoreUrls) {
            scriptLogicCrawler();
        }
    }

    public void closeCrawler() {
        Main.logger.info(LogConstants.SearchEngine.CRAWLER_CLOSING, this.crawlRef.get().getId(), this.crawlRef.get().getKeyword());
        this.crawlRef.get().setStatus(Crawl.Status.DONE);
        Main.logger.info(LogConstants.SearchEngine.CRAWLER_CLOSE, this.crawlRef.get().getId(), this.crawlRef.get().getKeyword());
    }
}
