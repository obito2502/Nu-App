package com.example.nu_app.models;

public class Application {

    private String name, surname, email, club, occupation, motivation, reason, phone;

    public Application() {

    }
    public Application(String name, String surname, String email, String club, String occupation, String motivation, String reason, String phone) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.club = club;
        this.occupation = occupation;
        this.motivation = motivation;
        this.reason = reason;
        this.phone = phone;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) { this.phone = phone; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
