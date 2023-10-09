package com.challenge.backend.utils;

public class CrawlConstants {

    public static class URL {
        public static final String BASE = (System.getenv("BASE_URL") != null) ? System.getenv("BASE_URL") : "https://manpages.courier-mta.org/";
        public static final String REQUEST_PARAM_ID = ":id";
    }

    public static class Path {
        public static final String PATH_CRAWL_POST = "/crawl";
        public static final String PATH_CRAWL_GET = "/crawl/:id";
        public static final String PATH_SESSION_LOG = "/session/log";

        public static final String PATH_SESSION_LOG_ID = "/session/log/:id";
    }

    public static class Session {
        public static final String STORE_ID = "IDSTORE";
    }

    public static class StatusCode {
        public static final String MSG_INVALID_KEYWORD = "Invalid Keyword";
        public static final String MSG_INVALID_ID = "Invalid ID";
    }

    public static class RegexPatterns {
        public static final String URL_VALID_PATTERN = "[a-zA-Z0-9-._~:/?#\\[\\]@!$&'()*+,;=]*";
        public static final String URL_INVALID_STRANGE_PATTERN = "/\\.\\./";
        public static final String URL_START_PATTERN = "^(?!https?://|www\\.).*";
        public static final String URL_START_PATTERN_HTTP = "http://";
        public static final String URL_MAILTO_SLASH = "/mailto:";
        public static final String URL_MAILTO = "mailto:";
        public static final String URL_FTP_SLASH = "/ftp:";
        public static final String URL_FTP = "ftp:";
        public static final String CHAR_NOTHING = "";
        public static final String CHAR_SPACE = " ";
        public static final String CHAR_SLASH = "/";
        public static final String CHAR_SLASH_SLASH = "//";
        public static final String HREF_RECOGNITION_PATTERN = "href=\"(.*?)\"";
        public static final String URL_RECOGNITION_PATTERN = "url=\"(.*?)\"";
        public static final String SRC_RECOGNITION_PATTERN = "src=\"(.*?)\"";
        public static final String LINK_RECOGNITION_PATTERN = "(href|url|src)=\"(.*?)\"";
        public static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    }

}
