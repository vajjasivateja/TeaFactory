package com.example.vajjasivateja.teafactory.Retrofit;

import com.example.vajjasivateja.teafactory.Model.CheckUserResponse;
import com.example.vajjasivateja.teafactory.Model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TeaFactoryAPI {
    @FormUrlEncoded
    @POST("checkUser.php")
    Call<CheckUserResponse> checkUserExists(@Field("Phone") String Phone);

    @FormUrlEncoded
    @POST("registerUser.php")
    Call<User> registerNewUser(@Field("Phone") String Phone,
                               @Field("Name") String Name,
                               @Field("Birthdate") String Birthdate,
                               @Field("Address") String Address)
                              ;


}
