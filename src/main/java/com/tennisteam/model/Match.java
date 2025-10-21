package com.tennisteam.model;

import java.time.LocalDate;

public class Match {
    private LocalDate date;
    private String teamA;
    private String teamB;
    private String playerA;
    private String playerB;
    private String scoreA;
    private String scoreB;

    public Match(LocalDate date, String teamA, String teamB, String playerA, String playerB, String scoreA, String scoreB) {
        this.date = date;
        this.teamA = teamA;
        this.teamB = teamB;
        this.playerA = playerA;
        this.playerB = playerB;
        this.scoreA = scoreA;
        this.scoreB = scoreB;
    }

    public Match(String teamA, String teamB, String teamA1) {
    }

    public LocalDate getDate() { return date; }
    public String getTeamA() { return teamA; }
    public String getTeamB() { return teamB; }
    public String getPlayerA() { return playerA; }
    public String getPlayerB() { return playerB; }
    public String getScoreA() { return scoreA; }
    public String getScoreB() { return scoreB; }
}
