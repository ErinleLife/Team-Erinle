package com.arnav.pocdoc.SimplyRelief.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SimplyReliefResponse implements Serializable {
    @SerializedName("data")
    @Expose
    private List<DataReliefItem> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<DataReliefItem> getData() {
        return data;
    }

    public void setData(List<DataReliefItem> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
