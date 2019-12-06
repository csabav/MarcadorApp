package com.vcsaba.beerware.marcadorapp.data;

public class Match {
    public Long homeTeamId;
    public Long awayTeamId;
    public String date;
    public String time;
    public Integer homeTeamScore;
    public Integer awayTeamScore;

    public Match(Long homeTeamId, Long awayTeamId, String date, String time, int homeTeamScore, int awayTeamScore) {
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.date = date;
        this.time = time;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
    }
}
