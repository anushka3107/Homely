package com.example.app.model;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public final class home implements Serializable {
    //table names from phpmyadmin
    @SerializedName ( "image" )
    private String image;
    @SerializedName ( "name" )
    private String name;
    @SerializedName ( "place" )
    private String place;
    @SerializedName ( "price" )
    private String price;
    @SerializedName ( "description" )
    private String description;
    @SerializedName ( "bedroom" )
    private String bedroom;
    @SerializedName ( "restroom" )
    private String restroom;
    @SerializedName ( "rating" )
    private String rating;
    @SerializedName ( "owner_details" )
    private String owner_details;
    @SerializedName ( "people_allowed" )
    private String people_allowed;
    @SerializedName ( "livingpref" )
    private String livingpref;
    @SerializedName ( "pricerange" )
    private String pricerange;

    public home() { }

    public home(final String image , final String name , final String place , final String price , final String description , final String bedroom , final String restroom , final String rating , final String owner_details , final String people_allowed
            , final String livingpref , final String pricerange)
    {
        this.image = image;
        this.name = name;
        this.place = place;
        this.price = price;
        this.description = description;
        this.bedroom = bedroom;
        this.restroom = restroom;
        this.rating = rating;
        this.owner_details = owner_details;
        this.people_allowed = people_allowed;
        this.livingpref = livingpref;
        this.pricerange = pricerange;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getBedroom() {
        return bedroom;
    }

    public String getRestroom() {
        return restroom;
    }

    public String getRating() {
        return rating;
    }

    public String getOwner_details() {
        return owner_details;
    }

    public String getPeople_allowed() {
        return people_allowed;
    }

    public String getLivingpref() {
        return livingpref;
    }

    public String getPricerange() {
        return pricerange;
    }

     }

