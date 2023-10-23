package com.challenge.backend.controller;

import com.challenge.backend.Main;
import com.challenge.backend.domain.Crawl;
import com.challenge.backend.helper.*;
import com.challenge.backend.service.CrawlApiService;
import com.challenge.backend.service.CrawlApiServiceImpl;
import com.challenge.backend.utils.CrawlConstants;
import com.challenge.backend.utils.LogConstants;
import spark.Spark;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static spark.Spark.get;
import static spark.Spark.post;

public class CrawlController {
    private static final CrawlApiService crawlApiService = new CrawlApiServiceImpl();
    private static final Map<String, AtomicReference<Crawl>> crawls = new ConcurrentHashMap<>();

    public CrawlController() {
        setupRoutes();
    }

    private void setupRoutes() {

        post(CrawlConstants.Path.PATH_CRAWL_POST, (req, res) -> {
            final String keyword = CrawlControllerHelper.extractKeywordFromRequestBody(req);
            if (KeywordHelper.isValid(keyword)) {
                final String id = IdGeneratorHelper.generateId();
                SessionHelper.storeId(req, id);
                Main.logger.info(LogConstants.Controller.ID_GENERATED, id);
                final Crawl crawl = new Crawl(id, keyword, Crawl.Status.INACTIVE);
                AtomicReference<Crawl> crawlRef = new AtomicReference<>(crawl);
                crawls.put(id, crawlRef);
                Main.logger.info(LogConstants.Controller.REF_GENERATED, crawlRef);
                crawlApiService.startCrawler(crawlRef);
                Main.logger.info(LogConstants.Controller.SEARCH_STARTED);
                res.status(200);
                return GsonHelper.gsonWithIdStrategy.toJson(crawl);
            }
            res.status(400);
            return GsonHelper.gson.toJson(CrawlConstants.StatusCode.MSG_INVALID_KEYWORD);
        });

        get(CrawlConstants.Path.PATH_CRAWL_GET, (req, res) -> {
            String id = CrawlControllerHelper.extractIdFromParams(req);
            Main.logger.info(LogConstants.Controller.ID_RETRIEVED, id);

            if (SessionHelper.validSotredId(req, id)) {
                Main.logger.info(LogConstants.Controller.DATA_RETRIEVED);
                res.status(200);
//                for (String url : crawls.get(id).get().getProcessedUrls()) {
//                    Main.logger.info(url);
//                }
                return GsonHelper.gsonExcludeFields.toJson(crawls.get(id).get());
            }
            res.status(400);
            return GsonHelper.gson.toJson(CrawlConstants.StatusCode.MSG_INVALID_ID);
        });

        get(CrawlConstants.Path.PATH_SESSION_LOG, (req, res) -> {
            List<String> list = SessionHelper.getSessionStore(req);
            for (String id : list) {
                Main.logger.info(crawls.get(id).get().toLog());
            }
            return GsonHelper.gson.toJson(list);
        });

        final String allowedOrigin = "*";
        Spark.options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });
        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", allowedOrigin);
            response.header("Access-Control-Request-Method", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });

        Spark.afterAfter((request, response) -> {
            response.header("Access-Control-Allow-Origin", allowedOrigin);
        });
    }
}

