package com.vcsaba.beerware.marcadorapp.data;

import java.net.URL;

public class Team {
    public Long id;

    public String name;

    public String badgeURL;

    public Team(Long id, String name, String badgeURL) {
        this.id = id;
        this.name = name;
        this.badgeURL = badgeURL;
    }
}
