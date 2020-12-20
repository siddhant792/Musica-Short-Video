package com.mark.tiktok20.All;

public class ModelRv_home {

    public int photo;
    public String text,video;

    public ModelRv_home(int profile_photo, String current_video, String username) {
        this.photo = profile_photo;
        this.video = current_video;
        this.text = username;
    }

    public String getVideo() {
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

    public void setVideo(String video) {
        this.video = video;
    }

    public void setText(String text) {
        this.text = text;
    }
}