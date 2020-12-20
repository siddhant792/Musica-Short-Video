package com.mark.tiktok20.models;

public class User_search {
    public String username,profileurl,name,uid;

    public User_search(String username, String profileurl, String name,String uid) {
        this.username = username;
        this.profileurl = profileurl;
        this.name = name;
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public User_search(){

    }
}
