package com.cos.androidtest.service;

import com.cos.androidtest.model.Contact;
import com.cos.androidtest.model.CMRespDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ContactService {

    @GET("phone")
    Call<CMRespDto<List<Contact>>> findAll();

    @GET("phone/{id}")
    Call<CMRespDto<Contact>> findById(@Path("id")Long id);

    @POST("phone")
    Call<CMRespDto<Contact>> save(@Body Contact contact);

    @PUT("phone/{id}")
    Call<CMRespDto<Contact>> update(@Path("id")Long id,@Body Contact contact);

    @DELETE("phone/{id}")
    Call<CMRespDto> delete(@Path("id") Long id);



    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.35.175:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
