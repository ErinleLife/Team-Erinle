package com.arnav.pocdoc.data.model.cosultantlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseConsultantList {
    @SerializedName("data")
    @Expose
    private List<DataConsultant> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<DataConsultant> getData() {
        return data;
    }

    public void setData(List<DataConsultant> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
