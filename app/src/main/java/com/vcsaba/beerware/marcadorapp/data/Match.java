package com.vcsaba.beerware.marcadorapp.data;

import java.util.Date;

public class Match {
    public long homeTeamId;
    public String homeTeam;

    public long awayTeamId;
    public String awayTeam;

    public String date;
    public String time;

    public Match(long homeTeamId, String homeTeam, long awayTeamId, String awayTeam, String date, String time) {
        this.homeTeamId = homeTeamId;
        this.homeTeam = homeTeam;
        this.awayTeamId = awayTeamId;
        this.awayTeam = awayTeam;
        this.date = date;
        this.time = time;
    }
}
