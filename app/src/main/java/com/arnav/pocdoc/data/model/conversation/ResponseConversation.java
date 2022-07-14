package com.arnav.pocdoc.data.model.conversation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseConversation {
    @SerializedName("data")
    @Expose
    private List<DataConversation> data = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    public List<DataConversation> getData() {
        return data;
    }

    public void setData(List<DataConversation> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
