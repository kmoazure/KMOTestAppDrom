package com.kmo.drom.kmoappdrom.HTTP.Models;

import java.util.List;

/**
 * Created by kmoaz on 11.03.2018.
 */

public class SearchReposModel {
    private Integer total_count;
    private Boolean incomplete_results;
    private List<RepoModel> items;
    private String page = "1";

    public Integer getTotal_count() {
        return total_count;
    }

    public Boolean getIncomplete_results() {
        return incomplete_results;
    }

    public List<RepoModel> getItems() {
        return items;
    }

    public void setItems(List<RepoModel> items) {
        this.items = items;
    }

    public String getPage() {
        return page;
    }

    public void setNextPage() {
        this.page = String.valueOf(Integer.valueOf(this.page) + 1);
    }

    public void setFirstPage() {
        this.page = "1";
    }
}
