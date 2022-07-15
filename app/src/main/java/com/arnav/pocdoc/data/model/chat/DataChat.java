package com.arnav.pocdoc.data.model.chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.arnav.pocdoc.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import okhttp3.internal.Util;

public class DataChat implements Parcelable {
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

    public DataChat() {
    }

    protected DataChat(Parcel in) {
        if (in.readByte() == 0) {
            from_id = null;
        } else {
            from_id = in.readInt();
        }
        if (in.readByte() == 0) {
            to_id = null;
        } else {
            to_id = in.readInt();
        }
        body = in.readString();
        attachment = in.readString();
        if (in.readByte() == 0) {
            seen = null;
        } else {
            seen = in.readInt();
        }
        time = in.readString();
        type = in.readString();
        created_at = in.readString();
    }

    public static final Creator<DataChat> CREATOR = new Creator<DataChat>() {
        @Override
        public DataChat createFromParcel(Parcel in) {
            return new DataChat(in);
        }

        @Override
        public DataChat[] newArray(int size) {
            return new DataChat[size];
        }
    };

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
        return created_at == null ? Utils.getCurrentUTCTime() : created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (from_id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(from_id);
        }
        if (to_id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(to_id);
        }
        parcel.writeString(body);
        parcel.writeString(attachment);
        if (seen == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(seen);
        }
        parcel.writeString(time);
        parcel.writeString(type);
        parcel.writeString(created_at);
    }
}
