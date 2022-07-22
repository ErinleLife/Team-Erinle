package com.arnav.pocdoc.symptomchecker.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSymptoms {
    @SerializedName("data")
    @Expose
    private DataSymptomsHeader data;
    @SerializedName("message")
    @Expose
    private String message;

    public DataSymptomsHeader getData() {
        return data;
    }

    public void setData(DataSymptomsHeader data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
