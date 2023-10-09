package com.challenge.backend.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrawlKeyword {
    @Expose
    @SerializedName("keyword")
    private final String keyword;

    public CrawlKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
