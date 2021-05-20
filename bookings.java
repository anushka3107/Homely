package com.example.app.model;

import android.widget.SearchView;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class bookings implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("contact")
    private String contact;
    @SerializedName("email")
    private String email;
    @SerializedName("date")
    private String date;
    @SerializedName ( "time" )
    private String time;

    public bookings(final String name , final String contact , final String email , final String date , final String time) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.date = date;
        this.time = time;
    }

    public bookings() {}

    public String getName() {
        return this.name;
    }

    public String getContact() {
        return this.contact;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDate()
    {
        return this.date;
    }

    public void setName( String name)
    {
        this.name = name;
    }

    public void setContact( String contact) {
        this.contact = contact;
    }

    public void setEmail( String email) {
        this.email = email;
    }

    public void setDate( String date) {
        this.date = date;
    }

    public void setTime( String time) {
        this.time = time;
    }

    public String getTime() {
        return this.time;
    }
}