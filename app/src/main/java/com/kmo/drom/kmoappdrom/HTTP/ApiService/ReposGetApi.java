package com.kmo.drom.kmoappdrom.HTTP.ApiService;

import com.kmo.drom.kmoappdrom.HTTP.Models.RepoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kmoaz on 10.03.2018.
 */

public interface ReposGetApi {
    @GET("{path_to_api}")
    Call<List<RepoModel>> callApi (
            @Path("path_to_api") String path,
            @Query("since") String since
    );
}
