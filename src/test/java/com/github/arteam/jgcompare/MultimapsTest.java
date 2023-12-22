package com.github.arteam.jgcompare;

import com.github.arteam.jgcompare.domain.TeamDivision;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * Date: 5/6/15
 * Time: 9:12 PM
 *
 * @author Artem Prigoda
 */
public class MultimapsTest {

    List<TeamDivision> teams = ImmutableList.<TeamDivision>builder()
            .add(new TeamDivision("Calgary Flames", "Pacific"))
            .add(new TeamDivision("Toronto Maple Leafs", "Atlantic"))
            .add(new TeamDivision("Florida Panthers", "Atlantic"))
            .add(new TeamDivision("Nashville Predators", "Central"))
            .add(new TeamDivision("Detroit Red Wings", "Metropolitan"))
            .add(new TeamDivision("Vancouver Canucks", "Pacific"))
            .add(new TeamDivision("New York Rangers", "Metropolitan"))
            .add(new TeamDivision("Dallas Stars", "Central"))
            .build();

    ListMultimap<String, TeamDivision> teamsByDivision = ImmutableListMultimap.<String, TeamDivision>builder()
            .putAll("Pacific",
                    new TeamDivision("Calgary Flames", "Pacific"),
                    new TeamDivision("Vancouver Canucks", "Pacific"))
            .putAll("Atlantic",
                    new TeamDivision("Toronto Maple Leafs", "Atlantic"),
                    new TeamDivision("Florida Panthers", "Atlantic"))
            .putAll("Metropolitan",
                    new TeamDivision("Detroit Red Wings", "Metropolitan"),
                    new TeamDivision("New York Rangers", "Metropolitan"))
            .putAll("Central",
                    new TeamDivision("Nashville Predators", "Central"),
                    new TeamDivision("Dallas Stars", "Central"))
            .build();

    @Test
    public void testIndex() {
        assertThat(Multimaps.index(teams, TeamDivision::division)).isEqualTo(teamsByDivision);
        assertThat(teams.stream().collect(Collectors.groupingBy(TeamDivision::division)))
                .contains(entry("Pacific", ImmutableList.of(
                        new TeamDivision("Calgary Flames", "Pacific"),
                        new TeamDivision("Vancouver Canucks", "Pacific"))))
                .contains(entry("Atlantic", ImmutableList.of(
                        new TeamDivision("Toronto Maple Leafs", "Atlantic"),
                        new TeamDivision("Florida Panthers", "Atlantic"))))
                .contains(entry("Metropolitan", ImmutableList.of(
                        new TeamDivision("Detroit Red Wings", "Metropolitan"),
                        new TeamDivision("New York Rangers", "Metropolitan"))))
                .contains(entry("Central", ImmutableList.of(
                        new TeamDivision("Nashville Predators", "Central"),
                        new TeamDivision("Dallas Stars", "Central"))));
    }

    @Test
    public void testFromMap() {
        Map<String, String> topTeams = ImmutableMap.of("Pacific", "Anaheim", "Atlantic", "Montreal",
                "Metropolitan", "New York", "Central", "St. Louis");
        assertThat(Multimaps.forMap(topTeams)).isEqualTo(ImmutableSetMultimap.<String, String>builder()
                .put("Pacific", "Anaheim")
                .put("Atlantic", "Montreal")
                .put("Metropolitan", "New York")
                .put("Central", "St. Louis")
                .build());
        assertThat(topTeams.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList()))))
                .contains(entry("Pacific", ImmutableList.of("Anaheim")))
                .contains(entry("Atlantic", ImmutableList.of("Montreal")))
                .contains(entry("Metropolitan", ImmutableList.of("New York")))
                .contains(entry("Central", ImmutableList.of("St. Louis")));

    }
}
