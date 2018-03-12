package com.kmo.drom.kmoappdrom.Activities.Login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.kmo.drom.kmoappdrom.Cicerone.BackButtonListener;
import com.kmo.drom.kmoappdrom.Cicerone.RouterProvider;
import com.kmo.drom.kmoappdrom.Cicerone.Screens;
import com.kmo.drom.kmoappdrom.Dagger.KMOAppDromApplication;
import com.kmo.drom.kmoappdrom.Fragments.Root.RootFragment;
import com.kmo.drom.kmoappdrom.R;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.commands.Back;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Replace;
import ru.terrakok.cicerone.commands.SystemMessage;

public class LoginActivity extends MvpAppCompatActivity implements LoginView, RouterProvider {

    @InjectPresenter
    LoginPresenter presenter;

    @Inject
    Router router;

    @Inject
    NavigatorHolder navigatorHolder;

    @ProvidePresenter
    public LoginPresenter createPresenter() {
        return new LoginPresenter(router);
    }

    private String TAG = "LOGIN ACTIVITY";
    private RootFragment rootFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        KMOAppDromApplication.INSTANCE.getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter.goToLogin();

        initContainers();
    }

    private void initContainers() {
        FragmentManager fm = getSupportFragmentManager();

        rootFragment = (RootFragment) fm.findFragmentByTag(TAG);
        if (rootFragment == null) {
            rootFragment = RootFragment.getNewInstance(Screens.ROOT_SCREEN);
            fm.beginTransaction()
                    .add(R.id.container, rootFragment, TAG)
                    .detach(rootFragment)
                    .commitNow();
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null
                && fragment instanceof BackButtonListener
                && ((BackButtonListener) fragment).onBackPressed()) {
            return;
        } else {
            presenter.onBackPressed();
        }
    }

    private Navigator navigator = new Navigator() {
        @Override
        public void applyCommands(Command[] commands) {
            for (Command command : commands)
                applyCommand(command);
        }

        private void applyCommand(Command command) {
            if (command instanceof Back) {
                finish();
            } else {
                if (command instanceof SystemMessage) {
                    Toasty.normal(LoginActivity.this, ((SystemMessage) command).getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    if (command instanceof Replace) {
                        FragmentManager fm = getSupportFragmentManager();

                        switch (((Replace) command).getScreenKey()) {
                            case Screens.ROOT_SCREEN:
                                fm.beginTransaction()
                                        .attach(rootFragment)
                                        .commitNow();
                                break;
                        }
                    }
                }
            }
        }
    };

    @Override
    public Router getRouter() {
        return router;
    }
}