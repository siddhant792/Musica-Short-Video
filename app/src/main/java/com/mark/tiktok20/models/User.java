package com.mark.tiktok20.models;

public class User {
    private String name,username,gender,bio,profileurl,uid;
    private int followers,following,likes;

    public User(String name, String username, String gender, String bio,String profileurl,String uid,int followers,int following,int likes) {
        this.name = name;
        this.username = username;
        this.gender = gender;
        this.bio = bio;
        this.uid = uid;
        this.profileurl = profileurl;
        this.followers = followers;
        this.following = following;
        this.likes = likes;
    }
    public User(){

    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public String getBio() {
        return bio;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public String getUid() {
        return uid;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
    }

    public int getLikes() {
        return likes;
    }
}
