package com.challenge.backend.service;

import com.challenge.backend.domain.Crawl;

import java.util.concurrent.atomic.AtomicReference;

public interface CrawlApiService {
    void startCrawler(AtomicReference<Crawl> crawlRef);
}
