package com.tennisteam.service;

import com.tennisteam.model.Match;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {

    private Match match(String teamA, String teamB) {
        return new Match(
                LocalDate.of(2024, 1, 1),
                teamA,
                teamB,
                "PlayerA_" + teamA,
                "PlayerB_" + teamB,
                "6-4",
                "3-6"
        );
    }

    @Test
    void testGetTeamsReturnsAllUniqueTeams() {
        List<Match> matches = List.of(
                match("TeamA", "TeamB"),
                match("TeamA", "TeamC"),
                match("TeamB", "TeamC")
        );

        Set<String> teams = Scheduler.getTeams(matches);

        assertEquals(Set.of("TeamA", "TeamB", "TeamC"), teams,
                "Should contain all 3 unique teams");
        assertEquals(3, teams.size(),
                "There should be exactly 3 unique teams");
    }

    @Test
    void testSuggestNextOpponentsReturnsUnplayedTeams() {
        List<Match> matches = List.of(
                match("TeamA", "TeamB"),
                match("TeamA", "TeamC"),
                match("TeamD", "TeamE")
        );

        List<String> opponents = Scheduler.suggestNextOpponents("TeamA", matches);

        assertTrue(opponents.containsAll(List.of("TeamD", "TeamE")),
                "TeamA should get D and E as next opponents");
        assertEquals(2, opponents.size());
    }

    @Test
    void testSuggestNextOpponentsWhenNoRemainingTeams() {
        List<Match> matches = List.of(
                match("TeamA", "TeamB")
        );

        List<String> opponents = Scheduler.suggestNextOpponents("TeamA", matches);

        assertTrue(opponents.isEmpty(),
                "Should return empty list if no new teams are available");
    }

    @MethodSource("teamDataProvider")
    @ParameterizedTest
    void testSuggestNextOpponentsParameterized(String team, int expectedUnplayedCount) {
        List<Match> matches = List.of(
                match("TeamA", "TeamB"),
                match("TeamA", "TeamC"),
                match("TeamD", "TeamE")
        );

        List<String> opponents = Scheduler.suggestNextOpponents(team, matches);
        assertEquals(expectedUnplayedCount, opponents.size(),
                () -> team + " should have " + expectedUnplayedCount + " unplayed opponents");
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> teamDataProvider() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("TeamA", 2),
                org.junit.jupiter.params.provider.Arguments.of("TeamB", 2),
                org.junit.jupiter.params.provider.Arguments.of("TeamC", 3),
                org.junit.jupiter.params.provider.Arguments.of("TeamE", 3)
        );
    }

    @Test
    void testIntegration_CSVLoaderAndScheduler() {
        List<Match> matches = CSVLoader.loadMatches("src/test/resources/matches.csv");

        assertFalse(matches.isEmpty(), "CSVLoader should load matches correctly");

        Set<String> teams = Scheduler.getTeams(matches);
        assertTrue(teams.containsAll(List.of("TeamA", "TeamB", "TeamC", "TeamD", "TeamE")),
                "All teams from CSV should be detected");

        List<String> nextOpponents = Scheduler.suggestNextOpponents("TeamA", matches);
        assertEquals(Set.of("TeamD", "TeamE"), new HashSet<>(nextOpponents),
                "Integration test: TeamA should be matched with D and E next");
    }
}