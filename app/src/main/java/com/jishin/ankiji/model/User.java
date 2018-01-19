package com.jishin.ankiji.model;

/**
 * Created by lana on 18/01/2018.
 */

public class User {
    private String id;
    private String username;
    private String email;
    private String linkPhoto;

    public User(String id, String username, String email, String linkPhoto) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.linkPhoto = linkPhoto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkPhoto() {
        return linkPhoto;
    }

    public void setLinkPhoto(String linkPhoto) {
        this.linkPhoto = linkPhoto;
    }
}
