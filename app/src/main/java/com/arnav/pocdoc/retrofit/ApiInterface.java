package com.arnav.pocdoc.retrofit;

import com.arnav.pocdoc.SimplyRelief.models.HospitalLocatorResponse;
import com.arnav.pocdoc.SimplyRelief.models.OtcResponse;
import com.arnav.pocdoc.SimplyRelief.models.ResponseCommon;
import com.arnav.pocdoc.SimplyRelief.models.SimplyReliefResponse;
import com.arnav.pocdoc.data.register.ResponseRegistration;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiInterface {

    // http://165.22.45.58/api/get_symptom?search=&page=2
//    @FormUrlEncoded
    @GET("get_symptom")
    Observable<Response<SimplyReliefResponse>> getSimplyReliefData(@Query("search") String search,
                                                                   @Query("page") String page);

    @GET("get_symptom")
    Observable<Response<SimplyReliefResponse>> getSymptom();

    @GET("get-otc-natural-drugs")
    Observable<Response<OtcResponse>> getOct();

    @GET("get-pharmacy")
    Observable<Response<HospitalLocatorResponse>> getPharmacy();

    @Multipart
    @POST("add-prescription")
    Observable<Response<ResponseCommon>> addProductReview(@PartMap HashMap<String, RequestBody> params,
                                                          @Part MultipartBody.Part part,
                                                          @Part MultipartBody.Part part1);

    @FormUrlEncoded
    @POST("singup")
    Observable<Response<ResponseRegistration>> singUp(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("login")
    Observable<Response<ResponseRegistration>> login(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("add-symptom")
    Observable<Response<ResponseCommon>> addSymptom(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("add-rating")
    Observable<Response<ResponseCommon>> addRating(@FieldMap HashMap<String, String> params);
}
