package com.kmo.drom.kmoappdrom.Activities.Login;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kmo.drom.kmoappdrom.Cicerone.Screens;

import ru.terrakok.cicerone.Router;

/**
 * Created by kmoaz on 05.03.2018.
 */

@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginView> {

    private Router router;

    public LoginPresenter(Router router) {
        this.router = router;
    }

    public void goToLogin() {
        router.replaceScreen(Screens.ROOT_SCREEN);
    }

    public void onBackPressed () {
        router.exit();
    }
}