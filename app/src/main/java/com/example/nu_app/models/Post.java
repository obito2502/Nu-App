package com.example.nu_app.models;

public class Post {

    private String author, title, date, location, description;
    private int postId;

    public Post() {

    }

    public Post(String date, String title, String description, String location, int postId, String author) {
        this.postId = postId;
        this.author = author;
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}