package com.kmo.drom.kmoappdrom.Fragments.Login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.kmo.drom.kmoappdrom.Cicerone.BackButtonListener;
import com.kmo.drom.kmoappdrom.Cicerone.RouterProvider;
import com.kmo.drom.kmoappdrom.HTTP.Backend;
import com.kmo.drom.kmoappdrom.R;
import com.kmo.drom.kmoappdrom.Utils.UtilsFiles;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * Created by kmoaz on 07.03.2018.
 */

public class LoginFragment extends MvpAppCompatFragment implements LoginView, BackButtonListener {

    private static final String TAG = "LOGIN FRAGMENT";
    private FirebaseAuth mAuth;

    @InjectPresenter
    LoginPresenter presenter;

    @ProvidePresenter
    LoginPresenter providePresenter () {
        return new LoginPresenter(
                ((RouterProvider) getParentFragment()).getRouter()
        );
    }

    public static LoginFragment getNewInstance (String name) {
        LoginFragment fragment = new LoginFragment();

        Bundle arguments = new Bundle();
        arguments.putString(TAG, name);
        fragment.setArguments(arguments);

        return fragment;
    }

    private Unbinder unbinder;
    private View rootView;

    @BindView(R.id.client_id)
    EditText clientId;

    @BindView(R.id.client_secret)
    EditText clientSecret;

    @BindView(R.id.login)
    Button login;

    @BindView(R.id.anon)
    Button anon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);

        unbinder = ButterKnife.bind(this, rootView);
        ButterKnife.bind(this, rootView);

        mAuth = FirebaseAuth.getInstance();

        //FirebaseUser user = mAuth.getCurrentUser();

        final String login = UtilsFiles.readFile(rootView.getContext());
        if (!login.isEmpty()) {
            rootView.findViewById(R.id.main_content).setVisibility(View.GONE);
            rootView.findViewById(R.id.loading).setVisibility(View.VISIBLE);
            String[] credentials = login.split(" ");
            presenter.login(credentials[0], credentials[1]);
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.login)
    void clickLogin() {
        enableButtons(false);
        presenter.login(clientId.getText().toString(), clientSecret.getText().toString());

    }

    @OnClick(R.id.anon)
    void clickAnon() {
        enableButtons(false);
        Backend.setInstance();
        presenter.goToRepos(false);
        /*mAuth.signInAnonymously().addOnCompleteListener((LoginActivity) rootView.getContext(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    enableButtons(false);
                    presenter.goToRepos(false);
                } else {
                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                }
            }
        });*/
    }

    @Override
    public void SuccessLogin() {
        if (clientId != null && !clientId.getText().toString().isEmpty() & clientSecret != null && !clientSecret.getText().toString().isEmpty()) {
            UtilsFiles.writeFile(rootView.getContext(), clientId.getText().toString() + " " + clientSecret.getText().toString());
        }
        enableButtons(true);
    }

    @Override
    public void ErrorLogin(String detail) {
        enableButtons(true);
        UtilsFiles.writeFile(rootView.getContext(), "");
        Toasty.error(rootView.getContext(), detail, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void InfoLogin(String detail) {
        enableButtons(true);
        UtilsFiles.writeFile(rootView.getContext(), "");
        Toasty.info(rootView.getContext(), detail, Toast.LENGTH_SHORT, true).show();
    }

    private void enableButtons(Boolean enable) {
        login.setEnabled(enable);
        anon.setEnabled(enable);
        login.setAlpha(enable ? 1.0F : 0.3F);
        anon.setAlpha(enable ? 1.0F : 0.3F);
    }

    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
        return true;
    }
}