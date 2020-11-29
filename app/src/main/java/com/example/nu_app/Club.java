package com.example.nu_app;

public class Club {
    String email, name, description, president;

    public Club() {

    }

    public Club(String email, String name, String description, String president) {
        this.email = email;
        this.name = name;
        this.description = description;
        this.president = president;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPresident() {
        return president;
    }

    public void setPresident(String president) {
        this.president = president;
    }
}
