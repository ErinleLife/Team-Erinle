package com.arnav.pocdoc.symptomchecker.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataSymptoms {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("primary_symptom")
    @Expose
    private String primarySymptom;
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
    private String recomendation;
    @SerializedName("natural")
    @Expose
    private String natural;
    @SerializedName("prevention")
    @Expose
    private String prevention;
    @SerializedName("specific")
    @Expose
    private String specific;
    @SerializedName("primary")
    @Expose
    private String primary;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    private boolean select;

    public DataSymptoms(Integer id, String primarySymptom, String symptomGroup) {
        this.id = id;
        this.primarySymptom = primarySymptom;
        this.symptomGroup = symptomGroup;
    }

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

    public String getRecomendation() {
        return recomendation;
    }

    public void setRecomendation(String recomendation) {
        this.recomendation = recomendation;
    }

    public String getNatural() {
        return natural;
    }

    public void setNatural(String natural) {
        this.natural = natural;
    }

    public String getPrevention() {
        return prevention;
    }

    public void setPrevention(String prevention) {
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
