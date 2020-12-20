package com.mark.tiktok20.All;

public class ModelRv_activity {

    private int photo,video;
    private String text;

    public ModelRv_activity(int photo,int video,String text) {
        this.photo = photo;
        this.video = video;
        this.text = text;
    }

    public int getVideo() {
        return video;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getText() {
        return text;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    public void setText(String text) {
        this.text = text;
    }
}