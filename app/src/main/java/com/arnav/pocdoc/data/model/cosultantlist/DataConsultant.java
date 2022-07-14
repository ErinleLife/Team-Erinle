package com.arnav.pocdoc.data.model.cosultantlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataConsultant {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("last_name")
    @Expose
    private Object lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("isPurchased")
    @Expose
    private Integer isPurchased;
    @SerializedName("patient_id")
    @Expose
    private Object patientId;
    @SerializedName("location")
    @Expose
    private Object location;
    @SerializedName("insurance")
    @Expose
    private Object insurance;
    @SerializedName("height")
    @Expose
    private Object height;
    @SerializedName("weight")
    @Expose
    private Object weight;
    @SerializedName("dob")
    @Expose
    private Object dob;
    @SerializedName("race")
    @Expose
    private Object race;
    @SerializedName("blood_type")
    @Expose
    private Object bloodType;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("habits")
    @Expose
    private Object habits;
    @SerializedName("otc_medication")
    @Expose
    private Object otcMedication;
    @SerializedName("bp_level")
    @Expose
    private Object bpLevel;
    @SerializedName("rx_medication_name")
    @Expose
    private Object rxMedicationName;
    @SerializedName("sugar")
    @Expose
    private Object sugar;
    @SerializedName("family_history")
    @Expose
    private Object familyHistory;
    @SerializedName("conditions")
    @Expose
    private Object conditions;
    @SerializedName("allergies")
    @Expose
    private Object allergies;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("active_status")
    @Expose
    private Integer activeStatus;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("dark_mode")
    @Expose
    private Integer darkMode;
    @SerializedName("messenger_color")
    @Expose
    private String messengerColor;
    @SerializedName("fcm_token")
    @Expose
    private Object fcmToken;
    @SerializedName("body")
    @Expose
    private String body;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(Integer isPurchased) {
        this.isPurchased = isPurchased;
    }

    public Object getPatientId() {
        return patientId;
    }

    public void setPatientId(Object patientId) {
        this.patientId = patientId;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public Object getInsurance() {
        return insurance;
    }

    public void setInsurance(Object insurance) {
        this.insurance = insurance;
    }

    public Object getHeight() {
        return height;
    }

    public void setHeight(Object height) {
        this.height = height;
    }

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }

    public Object getDob() {
        return dob;
    }

    public void setDob(Object dob) {
        this.dob = dob;
    }

    public Object getRace() {
        return race;
    }

    public void setRace(Object race) {
        this.race = race;
    }

    public Object getBloodType() {
        return bloodType;
    }

    public void setBloodType(Object bloodType) {
        this.bloodType = bloodType;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getHabits() {
        return habits;
    }

    public void setHabits(Object habits) {
        this.habits = habits;
    }

    public Object getOtcMedication() {
        return otcMedication;
    }

    public void setOtcMedication(Object otcMedication) {
        this.otcMedication = otcMedication;
    }

    public Object getBpLevel() {
        return bpLevel;
    }

    public void setBpLevel(Object bpLevel) {
        this.bpLevel = bpLevel;
    }

    public Object getRxMedicationName() {
        return rxMedicationName;
    }

    public void setRxMedicationName(Object rxMedicationName) {
        this.rxMedicationName = rxMedicationName;
    }

    public Object getSugar() {
        return sugar;
    }

    public void setSugar(Object sugar) {
        this.sugar = sugar;
    }

    public Object getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(Object familyHistory) {
        this.familyHistory = familyHistory;
    }

    public Object getConditions() {
        return conditions;
    }

    public void setConditions(Object conditions) {
        this.conditions = conditions;
    }

    public Object getAllergies() {
        return allergies;
    }

    public void setAllergies(Object allergies) {
        this.allergies = allergies;
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

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getDarkMode() {
        return darkMode;
    }

    public void setDarkMode(Integer darkMode) {
        this.darkMode = darkMode;
    }

    public String getMessengerColor() {
        return messengerColor;
    }

    public void setMessengerColor(String messengerColor) {
        this.messengerColor = messengerColor;
    }

    public Object getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(Object fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getBody() {
        return body == null ? "" : body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
