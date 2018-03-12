package com.kmo.drom.kmoappdrom.HTTP.Models;

/**
 * Created by kmoaz on 07.03.2018.
 */

public class RepoModel {
    private Long id;
    private String name;
    private String full_name;
    private String description;
    private OwnerModel owner;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getDescription() {
        return description;
    }

    public OwnerModel getOwner() {
        return owner;
    }
}
