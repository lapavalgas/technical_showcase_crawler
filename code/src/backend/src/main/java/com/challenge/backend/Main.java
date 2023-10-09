package com.challenge.backend;

import com.challenge.backend.controller.CrawlController;
import com.challenge.backend.domain.Crawl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Crawl.class);

    public static void main(String[] args) {
        Spark.port(4567);

        new CrawlController();

        //Spark.stop();

    }

}
