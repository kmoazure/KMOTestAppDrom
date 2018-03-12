package com.kmo.drom.kmoappdrom.Dagger;

import com.kmo.drom.kmoappdrom.Activities.Login.LoginActivity;
import com.kmo.drom.kmoappdrom.Cicerone.LocalNavigationModule;
import com.kmo.drom.kmoappdrom.Cicerone.NavigationModule;
import com.kmo.drom.kmoappdrom.Fragments.Login.LoginFragment;
import com.kmo.drom.kmoappdrom.Fragments.Repo.RepoFragment;
import com.kmo.drom.kmoappdrom.Fragments.Root.RootFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kmoaz on 07.03.2018.
 */

@Singleton
@Component(modules = {
        NavigationModule.class,
        LocalNavigationModule.class
})
public interface AppComponent {
    void inject(LoginActivity activity);

    void inject(RootFragment fragment);

    void inject(LoginFragment fragment);

    void inject(RepoFragment fragment);
}
