package com.kmo.drom.kmoappdrom.HTTP.Models;

/**
 * Created by kmoaz on 10.03.2018.
 */

public class LimitsModel {
    private LimitModel core;
    private LimitModel search;
    private LimitModel rate;

    public LimitModel getCore() {
        return core;
    }

    public LimitModel getSearch() {
        return search;
    }

    public LimitModel getRate() {
        return rate;
    }
}
