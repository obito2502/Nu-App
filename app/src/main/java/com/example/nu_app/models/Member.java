package com.example.nu_app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Member implements Parcelable {

    private String firstName;
    private String lastName;
    private String occupation;

    public Member() {
        firstName = lastName = occupation = "";
    }

    public Member(String firstName, String lastName, String occupation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
    }

    protected Member (Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOccupation() {
        return lastName;
    }

    public void setOccupation(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
    }
}
