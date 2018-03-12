package com.kmo.drom.kmoappdrom.HTTP.ApiService;

import com.kmo.drom.kmoappdrom.HTTP.Models.ResultLimitsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kmoaz on 10.03.2018.
 */

public interface LimitsGetApi {
    /*@GET("rate_limit")
    Call<ResultLimitsModel> callApi();*/
    @GET("{path_to_api}")
    Call<ResultLimitsModel> callApi(
            @Path("path_to_api") String path
    );
}
