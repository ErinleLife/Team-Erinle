package com.arnav.pocdoc.data.network;

import com.arnav.pocdoc.SimplyRelief.models.ResponseCommon;
import com.arnav.pocdoc.data.model.chat.ResponseChat;
import com.arnav.pocdoc.data.model.conversation.ResponseConversation;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface APIInterface {
    //    String prefix = "Service.php?Service=";
    @POST("chatify/api/getConversation")
    Call<ResponseConversation> getConversation(@Body HashMap<String, String> user);

    @POST("chatify/api/getNewConversation")
    Call<ResponseConversation> getNewConversation(@Body HashMap<String, String> user);

    @POST("api/update-token")
    Call<ResponseCommon> updateToken(@Body HashMap<String, String> user);

    @FormUrlEncoded
    @POST("chatify/api/chatList")
    Call<ResponseChat> chatList(@FieldMap HashMap<String, String> user);

    @Multipart
    @POST("chatify/api/sendMessage")
    Call<ResponseCommon> sendMessage(@PartMap HashMap<String, RequestBody> params,
                                     @Part MultipartBody.Part[] body);

//    @Multipart
//    @POST(prefix + "editGroup")
//    Call<ResponseCreateGroup> editGroup(@PartMap HashMap<String, RequestBody> params,
//                                        @Part MultipartBody.Part part);
}