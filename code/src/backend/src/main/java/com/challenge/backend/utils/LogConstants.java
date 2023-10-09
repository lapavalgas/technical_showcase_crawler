package com.challenge.backend.utils;

public class LogConstants {
    public static class Controller {
        public static final String ID_GENERATED = "id '{}' has been generated.";
        public static final String REF_GENERATED = "reference '{}' has been generated.";
        public static final String SEARCH_STARTED = "search has been started.";

        public static final String ID_RETRIEVED = "id '{}' has been retrieved.";
        public static final String DATA_RETRIEVED = "data crawler has been retrieved.";
    }

    public static class ApiService {
        public static final String SEARCH_ENGINE_STEP_1_INITIALIZED = "search engine STEP 1 has been initialized.";
        public static final String SEARCH_ENGINE_STEP_2_INITIALIZED = "search engine STEP 2 has been initialized.";

    }

    public static class SearchEngine {
        public static final String CRAWLER_STATUS_ACTIVATED = "status has been activated.";
        public static final String CRAWLER_REQUESTED_URL = "requested the URL {}.";
        public static final String CRAWLER_FIND_MATCH_KEYWORD = "find a match for the keyword '{}' in the URL {}.";
        public static final String CRAWLER_PUT_URL = "put URL {} in the urlList.";
        public static final String CRAWLER_PROCESSES_URL = "processes the {}.";
        public static final String CRAWLER_ADD_MALFORMED_URL = "addMalformedUrlURL {}.";
        public static final String CRAWLER_VISITED_URL = "visited the URL {}.";
        public static final String CRAWLER_UPDATED_FOUND_URL_LIST = "updated foundUrlList.";
        public static final String CRAWLER_FOUND_URL_LIST_UPDATED = "foundUrlList has been updated.";
        public static final String CRAWLER_EXECUTOR_SERVICE_INITIALIZED = "ExecutorService for multithreading mode has been initialized.";
        public static final String CRAWLER_EXECUTOR_SERVICE_STARTED = "ExecutorService has been started for the id '{}' and keyword '{}'.";
        public static final String CRAWLER_UPDATED_PROVISORY_FOUND_URL_LIST = "updated provisory foundUrlList.";
        public static final String CRAWLER_CONTINUES = "The crawler continues.........";
        public static final String CRAWLER_FINISH_SEARCH_JOB = "The crawler finish the search job for the id '{}' and keyword '{}'.";
        public static final String CRAWLER_CLOSING = "The crawler is closing search for the id '{}' and keywork '{}'.";
        public static final String CRAWLER_CLOSE = "The crawler closed search for the id '{}' and keywork '{}'.";
        public static final String CRAWLER_THREAD_SLEEP = "thread sleep for a while {}.";
        public static final String CRAWLER_DOBLECHECK_STARTS = "start a doble check for malformedUrl.";
        public static final String CRAWLER_DOBLECHECK_FINISH = "finish a doble check for malformedUrl.";
        public static final String THREAD_STOP_FAILURE = "Failed to stop threads, check the thread status and try again. ";
    }

    public static class HttpRequest {
        public static final String VALIDATED_PATTERN_DOMAIN = "validated pattern and domain in the {}.";
        public static final String SETUP_HTTP_CLIENT = "setup the HttpClient '{}'.";
        public static final String SETUP_HTTP_REQUEST = "setup the HttpRequest '{}'.";
        public static final String REQUESTED_URL = "requested the URL {}.";
        public static final String RESPONSE_STATUS_CODE = "response status code as '{}'.";
        public static final String HTTP_CLIENT_FAIL = "HttpClient FAIL to request the URL {}.";
    }

    public static class Error {
        public static final String ERROR_FAIL_VALIDATED_URL = "ERROR to validate the URL {}.";
        public static final String ERROR_FAIL_VALIDATED_URL_DOMAIN = "ERROR to validate the URL domain.";
        public static final String ERROR_FAIL_VALIDATED_URL_PATTERN = "ERROR to validate the URL pattern.";
        public static final String ERROR_FAIL_SLEEP = "ERROR to sleep.";
        public static final String ERROR_FAIL_TO_STOP = "ERROR to stop jobs.";

        public static final String ERROR_CLASS = "ERROR CLASS {}";
        public static final String ERROR_MSG = "ERROR MSG   {}";


    }
}
