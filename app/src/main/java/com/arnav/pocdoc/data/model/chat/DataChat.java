package com.arnav.pocdoc.data.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataChat {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("from_id")
    @Expose
    private Integer fromId;
    @SerializedName("to_id")
    @Expose
    private Integer toId;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("attachment")
    @Expose
    private Object attachment;
    @SerializedName("seen")
    @Expose
    private Integer seen;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("message_type")
    @Expose
    private String message_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    public Integer getSeen() {
        return seen;
    }

    public void setSeen(Integer seen) {
        this.seen = seen;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }
}
