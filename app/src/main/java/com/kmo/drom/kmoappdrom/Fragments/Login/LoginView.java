package com.kmo.drom.kmoappdrom.Fragments.Login;

import com.arellomobile.mvp.MvpView;

/**
 * Created by kmoaz on 07.03.2018.
 */

public interface LoginView extends MvpView {
    void SuccessLogin();
    void ErrorLogin(String detail);
    void InfoLogin(String detail);
}
