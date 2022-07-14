package com.arnav.pocdoc.data.network;

import com.arnav.pocdoc.SimplyRelief.models.ResponseCommon;
import com.arnav.pocdoc.data.model.chat.ResponseChat;
import com.arnav.pocdoc.data.model.cosultantlist.ResponseConsultantList;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface APIInterface {
    //    String prefix = "Service.php?Service=";
    @GET("chatify/api/getContacts")
    Call<ResponseConsultantList> getContacts(@Body HashMap<String, String> user);

    @FormUrlEncoded
    @POST("chatify/api/fetchMessages")
    Call<ResponseChat> fetchMessages(@FieldMap HashMap<String, String> user);

    @Multipart
    @POST("chatify/api/sendMessage")
    Call<ResponseCommon> sendMessage(@PartMap HashMap<String, RequestBody> params,
                                     @Part MultipartBody.Part[] body);

//    @Multipart
//    @POST(prefix + "editGroup")
//    Call<ResponseCreateGroup> editGroup(@PartMap HashMap<String, RequestBody> params,
//                                        @Part MultipartBody.Part part);
}