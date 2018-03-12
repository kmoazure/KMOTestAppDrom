package com.kmo.drom.kmoappdrom.Fragments.Root;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kmo.drom.kmoappdrom.Cicerone.BackButtonListener;
import com.kmo.drom.kmoappdrom.Cicerone.LocalCiceroneHolder;
import com.kmo.drom.kmoappdrom.Cicerone.RouterProvider;
import com.kmo.drom.kmoappdrom.Cicerone.Screens;
import com.kmo.drom.kmoappdrom.Dagger.KMOAppDromApplication;
import com.kmo.drom.kmoappdrom.Fragments.Login.LoginFragment;
import com.kmo.drom.kmoappdrom.Fragments.Repo.RepoFragment;
import com.kmo.drom.kmoappdrom.R;

import javax.inject.Inject;

import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.SupportAppNavigator;

/**
 * Created by kmoaz on 07.03.2018.
 */

public class RootFragment extends Fragment implements RouterProvider, BackButtonListener {

    private static final String TAG = "ROOT";

    private Navigator navigator;

    @Inject
    LocalCiceroneHolder ciceroneHolder;

    public static RootFragment getNewInstance(String name) {
        RootFragment fragment = new RootFragment();

        Bundle arguments = new Bundle();
        arguments.putString(TAG, name);
        fragment.setArguments(arguments);

        return fragment;
    }

    private String getContainerName() {
        return getArguments().getString(TAG);
    }

    private Cicerone<Router> getCicerone() {
        return ciceroneHolder.getCicerone(getContainerName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        KMOAppDromApplication.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_root, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getChildFragmentManager().findFragmentById(R.id.tab_container) == null) {
            getCicerone().getRouter().replaceScreen(Screens.LOGIN_SCREEN, 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCicerone().getNavigatorHolder().setNavigator(getNavigator());
    }

    @Override
    public void onPause() {
        getCicerone().getNavigatorHolder().removeNavigator();
        super.onPause();
    }

    private Navigator getNavigator() {
        if (navigator == null) {
            navigator = new SupportAppNavigator(getActivity(), getChildFragmentManager(), R.id.tab_container) {

                @Override
                protected Intent createActivityIntent(Context context, String screenKey, Object data) {
                    return null;
                }

                @Override
                protected Fragment createFragment(String screenKey, Object data) {
                    switch (screenKey) {
                        case Screens.LOGIN_SCREEN:
                            return LoginFragment.getNewInstance(getContainerName());

                        case Screens.REPOS_SCREEN:
                            return RepoFragment.getNewInstance(getContainerName(), (Boolean) data);

                        default:
                            return null;
                    }
                }

                @Override
                protected void exit() {
                    ((RouterProvider) getActivity()).getRouter().exit();
                }
            };
        }
        return navigator;
    }

    @Override
    public Router getRouter() {
        return getCicerone().getRouter();
    }

    @Override
    public boolean onBackPressed() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.tab_container);

        if (fragment != null
                && fragment instanceof BackButtonListener
                && ((BackButtonListener) fragment).onBackPressed()) {
            return true;
        } else {
            ((RouterProvider) getActivity()).getRouter().exit();
            return true;
        }
    }
}