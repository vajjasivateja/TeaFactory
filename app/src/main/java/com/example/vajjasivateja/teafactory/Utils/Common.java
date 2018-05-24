package com.example.vajjasivateja.teafactory.Utils;

import com.example.vajjasivateja.teafactory.Retrofit.RetrofitClient;
import com.example.vajjasivateja.teafactory.Retrofit.TeaFactoryAPI;

import retrofit2.Retrofit;

public class Common {
    // In Emulator, localhost = 10.0.2.2
    private static final String BASE_URL = "http://10.0.2.2/TeaFactory/";
    public static TeaFactoryAPI getAPI()
    {
        return RetrofitClient.getClient(BASE_URL).create(TeaFactoryAPI.class);
    }
}
