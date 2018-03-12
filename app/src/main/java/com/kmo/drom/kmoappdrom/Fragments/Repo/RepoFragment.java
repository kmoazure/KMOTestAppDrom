package com.kmo.drom.kmoappdrom.Fragments.Repo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.kmo.drom.kmoappdrom.Adapters.Repos.ReposAdapter;
import com.kmo.drom.kmoappdrom.Cicerone.BackButtonListener;
import com.kmo.drom.kmoappdrom.Cicerone.RouterProvider;
import com.kmo.drom.kmoappdrom.HTTP.Models.RepoModel;
import com.kmo.drom.kmoappdrom.HTTP.Models.SearchReposModel;
import com.kmo.drom.kmoappdrom.R;
import com.kmo.drom.kmoappdrom.Utils.UtilsFiles;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * Created by kmoaz on 05.03.2018.
 */

public class RepoFragment extends MvpAppCompatFragment implements RepoView, BackButtonListener {

    private static final String TAG = "REPO FRAGMENT";

    @InjectPresenter
    RepoPresenter presenter;

    @ProvidePresenter
    RepoPresenter providePresenter () {
        return new RepoPresenter(
                ((RouterProvider) getParentFragment()).getRouter()
        );
    }

    public static RepoFragment getNewInstance (String name, Boolean anonymous) {
        RepoFragment fragment = new RepoFragment();

        Bundle arguments = new Bundle();
        arguments.putString(TAG, name);
        arguments.putBoolean(TAG, anonymous);
        fragment.setArguments(arguments);

        return fragment;
    }

    private Unbinder unbinder;
    private View rootView;
    private PopupMenu popup;

    private ReposAdapter reposAdapter;

    private Boolean hasAuth;
    private final Integer COUNT_ITEMS_PER_PAGE = 100;
    private final Integer COUNT_ITEMS_FOR_LOAD = 15;
    private List<RepoModel> repos;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @BindView(R.id.overflow_menu)
    ImageView overflowMenu;

    @BindView(R.id.search)
    SearchView search;

    @BindView(R.id.repos_list)
    RecyclerView reposList;

    @BindView(R.id.repos_is_empty)
    TextView reposIsEmpty;

    @BindColor(R.color.colorBlack)
    int colorBlack;

    @BindString(R.string.sign_in)
    String signIn;

    @BindString(R.string.log_out)
    String logOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hasAuth = getArguments().getBoolean(TAG);
        repos = new ArrayList<>();
        reposAdapter = new ReposAdapter(getContext(), repos);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_repo, container, false);

        unbinder = ButterKnife.bind(this, rootView);
        ButterKnife.bind(this, rootView);

        refresh.setRefreshing(true);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getData("1");
            }
        });

        reposList.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        reposList.setItemAnimator(new DefaultItemAnimator());
        reposList.setAdapter(reposAdapter);

        initOverFlowMenu();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.sign_in_out:
                        if (hasAuth) {
                            UtilsFiles.writeFile(rootView.getContext(), "");
                        }

                        presenter.onBackPressed();
                        break;

                    default:
                        break;
                }

                return false;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                reposAdapter.setLocaleFilter(s);
                foundData(reposAdapter.getItemCount() != 0);
                return false;
            }
        });

        reposList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!refresh.isRefreshing() & search.getQuery().toString().isEmpty() /*reposAdapter.getItemCount() > COUNT_ITEMS_PER_PAGE - COUNT_ITEMS_FOR_LOAD*/ & ((LinearLayoutManager) reposList.getLayoutManager()).findLastVisibleItemPosition() + COUNT_ITEMS_FOR_LOAD > reposAdapter.getItemCount()) {
                    refresh.setRefreshing(true);
                    presenter.getData(String.valueOf(repos.get(repos.size() - 1).getId() + 1));
                }
            }
        });

        return rootView;
    }

    @OnClick(R.id.overflow_menu)
    void clickOverFlowMenu() {
        popup.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void SuccessGetRepos(List<RepoModel> repos) {
        this.repos = repos;
        reposAdapter.setRepos(this.repos);
        foundData(true);
    }

    @Override
    public void SuccessGetReposYet(List<RepoModel> repos) {
        this.repos.addAll(repos);
        reposAdapter.notifyDataSetChanged();
        foundData(true);
    }

    @Override
    public void SuccessGetReposEmpty() {
        foundData(false);
    }

    @Override
    public void SuccessGetSearchRepos(SearchReposModel repos) {

    }

    @Override
    public void SuccessGetSearchReposYet(SearchReposModel repos) {

    }

    @Override
    public void SuccessGetSearchReposEmpty() {

    }

    @Override
    public void Error(String detail) {
        refresh.setRefreshing(false);
        Toasty.error(rootView.getContext(), detail, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void Info(String detail) {
        refresh.setRefreshing(false);
        Toasty.info(rootView.getContext(), detail, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public boolean onBackPressed() {
        if (!hasAuth) {
            presenter.onBackPressed();
        }

        return true;
    }

    private void foundData(Boolean isFound) {
        reposList.setVisibility(isFound ? View.VISIBLE : View.GONE);
        reposIsEmpty.setVisibility(isFound ? View.GONE : View.VISIBLE);
        refresh.setRefreshing(false);
    }

    private void initOverFlowMenu() {
        popup = new PopupMenu(rootView.getContext(), overflowMenu);
        popup.inflate(R.menu.toolbar_menu);
        popup.getMenu().getItem(0).setTitle(hasAuth ? logOut : signIn);
    }
}