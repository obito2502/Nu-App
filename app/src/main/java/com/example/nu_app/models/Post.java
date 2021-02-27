package com.example.nu_app.models;

import android.net.Uri;

import java.util.Date;

public class Post {

    private String author, title, date, location, description, imageLink;
    private long time;

    public Post() {

    }

    public Post(String title, String date,  String location, String description, String author, String imageLink) {

        this.author = author;
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
        this.time = new Date().getTime();
        this.imageLink = imageLink;
    }


    public long getTime() {return time;}

    public void setTime(long time) {this.time = time;}

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


    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}