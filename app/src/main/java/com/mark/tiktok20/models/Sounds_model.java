package com.mark.tiktok20.models;

public class Sounds_model {
    String sound_name,sound_url;

    public String getSound_name() {
        return sound_name;
    }

    public String getSound_url() {
        return sound_url;
    }

    public Sounds_model(String sound_name, String sound_url) {
        this.sound_name = sound_name;
        this.sound_url = sound_url;
    }
}
