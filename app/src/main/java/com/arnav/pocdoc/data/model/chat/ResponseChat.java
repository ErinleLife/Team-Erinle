package com.arnav.pocdoc.data.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseChat {
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("last_page")
    @Expose
    private Integer lastPage;
    @SerializedName("last_message_id")
    @Expose
    private Integer lastMessageId;
    @SerializedName("data")
    @Expose
    private List<DataChat> data = null;
    @SerializedName("attachment_url")
    @Expose
    private String attachmentUrl;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public Integer getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(Integer lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public List<DataChat> getData() {
        return data;
    }

    public void setData(List<DataChat> messages) {
        this.data = messages;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
}
