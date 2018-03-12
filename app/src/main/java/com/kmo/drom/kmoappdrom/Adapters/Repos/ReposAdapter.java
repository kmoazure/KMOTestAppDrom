package com.kmo.drom.kmoappdrom.Adapters.Repos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmo.drom.kmoappdrom.HTTP.Models.RepoModel;
import com.kmo.drom.kmoappdrom.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kmoaz on 05.03.2018.
 */

public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ReposViewHolder> {

    protected static class ReposViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView avatarView;
        public TextView titleRepoView;
        public TextView descriptionRepoView;

        public ReposViewHolder(View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.avatar_owner);
            titleRepoView = itemView.findViewById(R.id.title_repo);
            descriptionRepoView = itemView.findViewById(R.id.description_repo);
        }
    }

    private List<RepoModel> repos;
    private List<RepoModel> filterRepos;
    private Context context;

    public ReposAdapter(Context context, List<RepoModel> repos) {
        this.context = context;
        this.repos = repos;
        this.filterRepos = repos;
    }

    @Override
    public ReposViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_repos, parent, false);

        return new ReposViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReposViewHolder holder, int position) {
        holder.titleRepoView.setText(filterRepos.get(position).getName());
        holder.descriptionRepoView.setText(filterRepos.get(position).getDescription());
        Picasso.with(context)
                .load(filterRepos.get(position).getOwner().getAvatar_url())
                .into(holder.avatarView);
    }

    @Override
    public int getItemCount() {
        return filterRepos.size();
    }

    public void setRepos(List<RepoModel> repos) {
        this.repos = repos;
        this.filterRepos = repos;
        notifyDataSetChanged();
    }

    public void setLocaleFilter(String query) {
        query = query.toLowerCase();
        filterRepos = new ArrayList<>();

        if (query.isEmpty()) {
            filterRepos = repos;
        } else {
            for (RepoModel repo : repos) {
                if ((repo.getName() != null && repo.getName().contains(query)) | (repo.getDescription() != null && repo.getDescription().contains(query))) {
                    filterRepos.add(repo);
                }
            }
        }

        notifyDataSetChanged();
    }

    public RepoModel getRepo(int i) {
        return this.filterRepos.get(i);
    }
}
