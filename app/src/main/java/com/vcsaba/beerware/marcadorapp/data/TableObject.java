package com.vcsaba.beerware.marcadorapp.data;

public class TableObject {
    public Long teamId;
    public String played;
    public String wins;
    public String draws;
    public String losses;
    public String goalsfor;
    public String goalsagainst;
    public String goalsdifference;
    public String points;

    public TableObject(Long teamId, String played, String wins, String draws, String losses, String goalsfor, String goalsagainst, String goalsdifference, String points) {
        this.teamId = teamId;
        this.played = played;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.goalsfor = goalsfor;
        this.goalsagainst = goalsagainst;
        this.goalsdifference = goalsdifference;
        this.points = points;
    }
}
