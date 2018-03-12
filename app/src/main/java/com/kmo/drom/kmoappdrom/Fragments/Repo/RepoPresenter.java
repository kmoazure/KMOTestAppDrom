package com.kmo.drom.kmoappdrom.Fragments.Repo;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kmo.drom.kmoappdrom.HTTP.ApiName;
import com.kmo.drom.kmoappdrom.HTTP.ApiService.LimitsGetApi;
import com.kmo.drom.kmoappdrom.HTTP.ApiService.ReposGetApi;
import com.kmo.drom.kmoappdrom.HTTP.ApiService.SearchReposGetApi;
import com.kmo.drom.kmoappdrom.HTTP.Backend;
import com.kmo.drom.kmoappdrom.HTTP.Models.RepoModel;
import com.kmo.drom.kmoappdrom.HTTP.Models.SearchReposModel;
import com.kmo.drom.kmoappdrom.HTTP.Models.ResultLimitsModel;
import com.kmo.drom.kmoappdrom.Utils.Messages;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.terrakok.cicerone.Router;

/**
 * Created by kmoaz on 05.03.2018.
 */

@InjectViewState
public class RepoPresenter extends MvpPresenter<RepoView> {

    private Router router;

    public RepoPresenter(Router router) {
        this.router = router;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        getData("1");
        //getSearchData("a", "7");
        //resetLimitsInfo(false);
    }

    public void getData(final String since) {
        Backend.getInstance()
                .create(ReposGetApi.class)
                .callApi(
                        ApiName.GET_REPOS,
                        since
                ).enqueue(new Callback<List<RepoModel>>() {
            @Override
            public void onResponse(Call<List<RepoModel>> call, Response<List<RepoModel>> response) {
                switch (response.code()) {
                    case 200:
                        if (response.body().size() > 0) {
                            if (since.equals("1")) {
                                getViewState().SuccessGetRepos(response.body());
                            } else {
                                getViewState().SuccessGetReposYet(response.body());
                            }
                        } else {
                            getViewState().SuccessGetReposEmpty();
                        }
                        break;

                    case 403:
                        resetLimitsInfo(false);
                        break;

                    default:
                        getViewState().Error(Messages.ERROR_GET_REPOS);
                }
            }

            @Override
            public void onFailure(Call<List<RepoModel>> call, Throwable t) {
                getViewState().Error(Messages.FAIL_GET_REPOS);
            }
        });
    }

    public void getSearchData(final String search, final String page) {
        Backend.getInstance()
                .create(SearchReposGetApi.class)
                .callApi(
                        ApiName.GET_SEARCH_REPOS,
                        search,
                        page
                ).enqueue(new Callback<SearchReposModel>() {
            @Override
            public void onResponse(Call<SearchReposModel> call, Response<SearchReposModel> response) {
                switch (response.code()) {
                    case 200:
                        if (response.body().getItems().size() > 0) {
                            if (page.equals("1")) {
                                getViewState().SuccessGetSearchRepos(response.body());
                            } else {
                                getViewState().SuccessGetSearchReposYet(response.body());
                            }
                        } else {
                            getViewState().SuccessGetSearchReposEmpty();
                        }
                        break;

                    case 403:
                        resetLimitsInfo(true);
                        break;

                    default:
                        getViewState().Error(Messages.ERROR_GET_SEARCH_REPOS);
                }
            }

            @Override
            public void onFailure(Call<SearchReposModel> call, Throwable t) {
                getViewState().Error(Messages.FAIL_GET_SEARCH_REPOS);
            }
        });
    }

    private void resetLimitsInfo(final Boolean search) {
        Backend.getInstance()
                .create(LimitsGetApi.class)
                .callApi(
                        ApiName.GET_LIMITS
                ).enqueue(new Callback<ResultLimitsModel>() {
            @Override
            public void onResponse(Call<ResultLimitsModel> call, Response<ResultLimitsModel> response) {
                switch (response.code()) {
                    case 200:
                        if (search) {
                            getViewState().Info(Messages.INFO_GET_LIMITS_SEARCH_REPOS + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(response.body().getResources().getSearch().getReset() * 1000L)));
                        } else {
                            getViewState().Info(Messages.INFO_GET_LIMITS_REPOS + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(response.body().getResources().getCore().getReset() * 1000L)));
                        }
                        break;

                    default:
                        getViewState().Error(Messages.ERROR_GET_LIMITS);
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResultLimitsModel> call, Throwable t) {
                getViewState().Error(Messages.FAIL_GET_LIMITS);
            }
        });
    }

    public void onBackPressed() {
        router.exit();
    }
}
