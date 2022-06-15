package com.arnav.pocdoc.SimplyRelief.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HospitalLocatorResponse implements Serializable {
    @SerializedName("data")
    @Expose
    private List<DataHospitalItem> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<DataHospitalItem> getData() {
        return data;
    }

    public void setData(List<DataHospitalItem> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
