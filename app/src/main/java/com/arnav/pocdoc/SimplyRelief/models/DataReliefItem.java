package com.arnav.pocdoc.SimplyRelief.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DataReliefItem implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("primary_symptom")
    @Expose
    private String primarySymptom;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("symptom_group")
    @Expose
    private String symptomGroup;
    @SerializedName("assessment")
    @Expose
    private String assessment;
    @SerializedName("exclusions")
    @Expose
    private String exclusions;
    @SerializedName("recomendation")
    @Expose
    private List<Recomendation> recomendation = null;
    @SerializedName("natural")
    @Expose
    private List<Recomendation> natural;
    @SerializedName("prevention")
    @Expose
    private List<Recomendation> prevention;
    @SerializedName("specific")
    @Expose
    private String specific;
    @SerializedName("primary")
    @Expose
    private String primary;
    @SerializedName("created_at")
    @Expose
    private Object createdAt = null;
    @SerializedName("updated_at")
    @Expose
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

    public String getPrimarySymptom() {
        return primarySymptom;
    }

    public void setPrimarySymptom(String primarySymptom) {
        this.primarySymptom = primarySymptom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSymptomGroup() {
        return symptomGroup;
    }

    public void setSymptomGroup(String symptomGroup) {
        this.symptomGroup = symptomGroup;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getExclusions() {
        return exclusions;
    }

    public void setExclusions(String exclusions) {
        this.exclusions = exclusions;
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

    public List<Recomendation> getPrevention() {
        return prevention;
    }

    public void setPrevention(List<Recomendation> prevention) {
        this.prevention = prevention;
    }

    public String getSpecific() {
        return specific;
    }

    public void setSpecific(String specific) {
        this.specific = specific;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }
}
