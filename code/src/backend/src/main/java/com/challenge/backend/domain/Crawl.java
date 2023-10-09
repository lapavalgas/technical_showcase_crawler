package com.challenge.backend.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

public class Crawl {
    private final Object lock = new Object();
    @Expose
    @SerializedName("id")
    private final String id;
    private final String keyword;
    @Expose
    @SerializedName("status")
    private Status status;
    @Expose
    @SerializedName("urls")
    private final Set<String> urls;
    private final Set<String> foundUrls;
    private final Set<String> processedUrls;
    private final Set<String> visitedUrls;
    private final Set<String> malformedUrls;
    private int scriptExecutionCount;
    private String millisElapsedTime;

    public void addUrl(String url) {
        synchronized (lock) {
            this.urls.add(url);
        }
    }

    public void addVisitedUrls(String url) {
        synchronized (lock) {
            this.visitedUrls.add(url);
        }
    }

    public enum Status {
        ACTIVE, DONE, INACTIVE, ERROR
    }

    public Crawl(String id, String keyword) {
        synchronized (lock) {
            this.id = id;
            this.keyword = keyword;
            this.status = Status.INACTIVE;
            this.urls = new HashSet<>();
            this.foundUrls = new HashSet<>();
            this.visitedUrls = new HashSet<>();
            this.processedUrls = new HashSet<>();
            this.malformedUrls = new HashSet<>();
        }
    }

    public Crawl(String id, String keyword, Status status) {
        synchronized (lock) {
            this.id = id;
            this.keyword = keyword;
            this.status = status;
            this.urls = new HashSet<>();
            this.foundUrls = new HashSet<>();
            this.visitedUrls = new HashSet<>();
            this.processedUrls = new HashSet<>();
            this.malformedUrls = new HashSet<>();
        }
    }

    public Crawl(String id, String keyword, Status status, Set<String> urls) {
        synchronized (lock) {
            this.id = id;
            this.keyword = keyword;
            this.status = status;
            this.urls = urls;
            this.foundUrls = new HashSet<>();
            this.visitedUrls = new HashSet<>();
            this.processedUrls = new HashSet<>();
            this.malformedUrls = new HashSet<>();
        }
    }

    public String getId() {
        return id;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setStatus(Status status) {
        synchronized (lock) {
            this.status = status;
        }
    }

    public Set<String> getFoundUrls() {
        return foundUrls;
    }

    public void addAllFoundUrl(Set<String> urls) {
        synchronized (lock) {
            this.foundUrls.addAll(urls);
        }
    }

    public Set<String> getVisitedUrls() {
        synchronized (lock) {
            return visitedUrls;
        }
    }

    public void synchronizedProcessUrl(String url) {
        synchronized (lock) {
            this.processedUrls.add(url);
        }
    }

    public Set<String> getProcessedUrls() {
        return this.processedUrls;
    }

    public void addMalformedUrl(String url) {
        synchronized (lock) {
            this.malformedUrls.add(url);
        }
    }

    public Set<String> getMalformedUrl() {
        return this.malformedUrls;
    }

    public void setScriptExecutionCount(int scriptExecutionCount) {
        this.scriptExecutionCount = scriptExecutionCount;
    }

    public void setMillisElapsedTime(String millisElapsedTime) {
        this.millisElapsedTime = millisElapsedTime;
    }

    @Override
    public String toString() {
        return "Crawl{" +
                "id='" + id + '\'' +
                ", keyword='" + keyword + '\'' +
                ", status=" + status +
                ", urls=" + urls +
                '}';
    }

    public String toLog() {
        return "\n|-------------------------" +
                "\n|id=" + id + "              |" +
                "\n|keyword=" + keyword + "         |" +
                "\n|urls=" + urls.size() + "                 |" +
                "\n|status=" + status + "              |" +
                "\n|-------------------------|" +
                "\n|urlsFound=" + foundUrls.size() + "           |" +
                "\n|processedUrls=" + processedUrls.size() + "       |" +
                "\n|visitedUrls=" + visitedUrls.size() + "         |" +
                "\n|malformedUrls=" + malformedUrls.size() + "          |" +
                "\n|scriptExecutionCount=" + scriptExecutionCount + "   |" +
                "\n|elapsedTime=" + millisElapsedTime + " |" +
                "\n|-------------------------|";
    }
}
