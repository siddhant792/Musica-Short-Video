package com.mark.tiktok20.models;

public class User_uploads {
    private String posturl,thumburl;

    public User_uploads(String posturl, String thumburl) {
        this.posturl = posturl;
        this.thumburl = thumburl;
    }

    public String getPosturl() {
        return posturl;
    }

    public String getThumburl() {
        return thumburl;
    }
}
