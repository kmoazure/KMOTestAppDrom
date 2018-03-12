package com.kmo.drom.kmoappdrom.Fragments.Repo;

import com.arellomobile.mvp.MvpView;
import com.kmo.drom.kmoappdrom.HTTP.Models.RepoModel;
import com.kmo.drom.kmoappdrom.HTTP.Models.SearchReposModel;

import java.util.List;

/**
 * Created by kmoaz on 05.03.2018.
 */

public interface RepoView extends MvpView {
    void SuccessGetRepos(List<RepoModel> repos);
    void SuccessGetReposYet(List<RepoModel> repos);
    void SuccessGetReposEmpty();
    void SuccessGetSearchRepos(SearchReposModel repos);
    void SuccessGetSearchReposYet(SearchReposModel repos);
    void SuccessGetSearchReposEmpty();
    void Error(String detail);
    void Info(String detail);
}
