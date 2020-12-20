package com.mark.tiktok20.models;

public class Posts {
    String url;
    long counter;

    public Posts(String url, long counter) {
        this.url = url;
        this.counter = counter;
    }

    public String getUrl() {
        return url;
    }

    public long getCounter() {
        return counter;
    }
}
