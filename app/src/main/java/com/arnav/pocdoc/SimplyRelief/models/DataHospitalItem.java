package com.arnav.pocdoc.SimplyRelief.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataHospitalItem implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String long1;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("zipcode")
    @Expose
    private String zipcode;

    private final Object updatedAt = null;
    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getLat() {
        return lat;
    }

    public String getLong1() {
        return long1;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLong1(String long1) {
        this.long1 = long1;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
