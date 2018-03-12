package com.kmo.drom.kmoappdrom.HTTP.ApiService;

import com.kmo.drom.kmoappdrom.HTTP.Models.SearchReposModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kmoaz on 11.03.2018.
 */

public interface SearchReposGetApi {
    @GET("{path_to_api}")
    Call<SearchReposModel> callApi (
            @Path(value = "path_to_api", encoded = true) String path,
            @Query("q") String search,
            @Query("page") String page
    );
}
