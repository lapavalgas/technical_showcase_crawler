package com.challenge.backend.helper;

import com.challenge.backend.domain.CrawlKeyword;
import com.challenge.backend.utils.CrawlConstants;
import com.google.gson.Gson;
import spark.Request;

public class CrawlControllerHelper {
    public static String extractKeywordFromRequestBody(Request req) {
        return new Gson().fromJson(req.body(), CrawlKeyword.class).getKeyword();
    }

    public static String extractIdFromParams(Request req) {
        return req.params(CrawlConstants.URL.REQUEST_PARAM_ID);
    }
}