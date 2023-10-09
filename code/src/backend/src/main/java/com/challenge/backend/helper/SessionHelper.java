package com.challenge.backend.helper;

import com.challenge.backend.utils.CrawlConstants;
import spark.Request;

import java.util.ArrayList;
import java.util.List;

public class SessionHelper {
    public static void storeId(Request req, String id) {
        List<String> idList = retrievesOrMountSessionStoreIds(req);
        idList.add(id);
        req.session().attribute(CrawlConstants.Session.STORE_ID, idList);
    }

    public static boolean validSotredId(Request req, String id) {
        List<String> idList = retrievesOrMountSessionStoreIds(req);
        return idList.contains(id);
    }

    public static List<String> getSessionStore(Request req) {
        return retrievesOrMountSessionStoreIds(req);
    }

    private static List<String> retrievesOrMountSessionStoreIds(Request req) {
        List<String> idList = req.session().attribute(CrawlConstants.Session.STORE_ID);
        if (idList == null) {
            idList = new ArrayList<>();
            req.session().attribute(CrawlConstants.Session.STORE_ID, idList);
        }
        return idList;
    }
}