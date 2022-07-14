package com.arnav.pocdoc.data.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataChat {
    @SerializedName("from_id")
    @Expose
    private Integer from_id;
    @SerializedName("to_id")
    @Expose
    private Integer to_id;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("seen")
    @Expose
    private Integer seen;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    public Integer getFrom_id() {
        return from_id;
    }

    public void setFrom_id(Integer from_id) {
        this.from_id = from_id;
    }

    public Integer getTo_id() {
        return to_id;
    }

    public void setTo_id(Integer to_id) {
        this.to_id = to_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Integer getSeen() {
        return seen;
    }

    public void setSeen(Integer seen) {
        this.seen = seen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated_at() {
        return created_at == null ? "2022-07-14 12:40:00" : created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
