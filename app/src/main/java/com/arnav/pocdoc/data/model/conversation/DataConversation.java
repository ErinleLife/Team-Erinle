package com.arnav.pocdoc.data.model.conversation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataConversation implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("fcm_token")
    @Expose
    private Object fcmToken;
    @SerializedName("active_status")
    @Expose
    private Integer activeStatus;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("zipcode")
    @Expose
    private String zipcode;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("last_message")
    @Expose
    private String lastMessage;

    public DataConversation() {
    }

    protected DataConversation(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            activeStatus = null;
        } else {
            activeStatus = in.readInt();
        }
        name = in.readString();
        email = in.readString();
        image = in.readString();
        description = in.readString();
        address = in.readString();
        lat = in.readString();
        _long = in.readString();
        zipcode = in.readString();
        time = in.readString();
        lastMessage = in.readString();
    }

    public static final Creator<DataConversation> CREATOR = new Creator<DataConversation>() {
        @Override
        public DataConversation createFromParcel(Parcel in) {
            return new DataConversation(in);
        }

        @Override
        public DataConversation[] newArray(int size) {
            return new DataConversation[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(Object fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLastMessage() {
        return lastMessage == null ? "" : lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        if (activeStatus == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(activeStatus);
        }
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(image);
        parcel.writeString(description);
        parcel.writeString(address);
        parcel.writeString(lat);
        parcel.writeString(_long);
        parcel.writeString(zipcode);
        parcel.writeString(time);
        parcel.writeString(lastMessage);
    }
}
