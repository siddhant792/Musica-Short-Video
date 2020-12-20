package com.mark.tiktok20.models;

public class ModelThumbnail {
    private String thumb,post;

    public ModelThumbnail(String thumb, String post) {
        this.thumb = thumb;
        this.post = post;
    }

    public String getThumb() {
        return thumb;
    }

    public String getPost() {
        return post;
    }
}
