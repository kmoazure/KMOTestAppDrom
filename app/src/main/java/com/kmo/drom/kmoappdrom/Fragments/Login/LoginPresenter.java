package com.kmo.drom.kmoappdrom.Fragments.Login;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kmo.drom.kmoappdrom.Cicerone.Screens;
import com.kmo.drom.kmoappdrom.HTTP.ApiName;
import com.kmo.drom.kmoappdrom.HTTP.ApiService.LoginGetApi;
import com.kmo.drom.kmoappdrom.HTTP.Backend;
import com.kmo.drom.kmoappdrom.HTTP.Models.LoginModel;
import com.kmo.drom.kmoappdrom.Utils.Messages;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.terrakok.cicerone.Router;

/**
 * Created by kmoaz on 07.03.2018.
 */

@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginView> {

    private Router router;

    public LoginPresenter(Router router) {
        this.router = router;
    }

    public void login(String email, String password) {
        if (!email.isEmpty() | !password.isEmpty()) {
            Backend.getInstanceCredentials(email, password)
                    .create(LoginGetApi.class)
                    .callApi(
                            ApiName.GET_LOGIN
                    ).enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    switch (response.code()) {
                        case 200:
                            getViewState().SuccessLogin();
                            goToRepos(true);
                            break;

                        case 401:
                            getViewState().ErrorLogin(Messages.LOGIN_ERROR_CREDENTIALS);
                            break;

                        default:
                            getViewState().ErrorLogin(Messages.LOGIN_ERROR);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    getViewState().ErrorLogin(Messages.LOGIN_FAIL);
                }
            });

            getViewState().SuccessLogin();
        } else {
            getViewState().InfoLogin(Messages.LOGIN_INFO);
        }
    }

    public void goToRepos(Boolean hasAuth) {
        router.navigateTo(Screens.REPOS_SCREEN, hasAuth);
    }

    public void onBackPressed() {
        router.exit();
    }
}
