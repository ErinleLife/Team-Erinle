package com.arnav.pocdoc.SimplyRelief.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DataOTCItem implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("recomendation")
    @Expose
    private List<Recomendation> recomendation = null;
    @SerializedName("natural")
    @Expose
    private List<Recomendation> natural;

    private Object updatedAt = null;
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

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Recomendation> getRecomendation() {
        return recomendation;
    }

    public void setRecomendation(List<Recomendation> recomendation) {
        this.recomendation = recomendation;
    }

    public List<Recomendation> getNatural() {
        return natural;
    }

    public void setNatural(List<Recomendation> natural) {
        this.natural = natural;
    }

}
