package com.kmo.drom.kmoappdrom.HTTP.ApiService;

import com.kmo.drom.kmoappdrom.HTTP.Models.LoginModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kmoaz on 11.03.2018.
 */

public interface LoginGetApi {
    @GET("{path_to_api}")
    Call<LoginModel> callApi(
            @Path("path_to_api") String path
    );
}
