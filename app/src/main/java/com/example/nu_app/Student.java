package com.example.nu_app;

import java.util.ArrayList;
import java.util.List;

public class Student {
    String email, name, major;
    List<String> subscriptions = new ArrayList<String>();

    public Student() {
    }

    public Student(String email, String name, String major) {
        this.email = email;
        this.name = name;
        this.major = major;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }


    public List<String> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<String> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void addSubscription(String club) {
        subscriptions.add(club);
    }

    public void cancelSubsription(String club) {
        subscriptions.remove(club);
    }
}
