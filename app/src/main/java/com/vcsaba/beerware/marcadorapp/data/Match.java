package com.vcsaba.beerware.marcadorapp.data;

import java.util.Date;

public class Match {
    public Long idHomeTeam;
    public Long idAwayTeam;
    public String dateEvent;
    public String strTimeLocal;

    public Match(Long idHomeTeam, Long idAwayTeam, String dateEvent, String strTimeLocal) {
        this.idHomeTeam = idHomeTeam;
        this.idAwayTeam = idAwayTeam;
        this.dateEvent = dateEvent;
        this.strTimeLocal = strTimeLocal;
    }
}
