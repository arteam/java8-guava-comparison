package com.github.arteam.jgcompare;

import com.google.common.collect.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.arteam.jgcompare.TeamDivision.of;
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
            .add(of("Calgary Flames", "Pacific"))
            .add(of("Toronto Maple Leafs", "Atlantic"))
            .add(of("Florida Panthers", "Atlantic"))
            .add(of("Nashville Predators", "Central"))
            .add(of("Detroit Red Wings", "Metropolitan"))
            .add(of("Vancouver Canucks", "Pacific"))
            .add(of("New York Rangers", "Metropolitan"))
            .add(of("Dallas Stars", "Central"))
            .build();

    @Test
    public void testIndex() {
        assertThat(Multimaps.index(teams, TeamDivision::getDivision)).isEqualTo(ImmutableMultimap.<String, TeamDivision>builder()
                .putAll("Pacific", of("Calgary Flames", "Pacific"), of("Vancouver Canucks", "Pacific"))
                .putAll("Atlantic", of("Toronto Maple Leafs", "Atlantic"), of("Florida Panthers", "Atlantic"))
                .putAll("Metropolitan", of("Detroit Red Wings", "Metropolitan"), of("New York Rangers", "Metropolitan"))
                .putAll("Central", of("Nashville Predators", "Central"), of("Dallas Stars", "Central"))
                .build());
        assertThat(teams.stream().collect(Collectors.groupingBy(TeamDivision::getDivision)))
                .contains(entry("Pacific", ImmutableList.of(of("Calgary Flames", "Pacific"), of("Vancouver Canucks", "Pacific"))))
                .contains(entry("Atlantic", ImmutableList.of(of("Toronto Maple Leafs", "Atlantic"), of("Florida Panthers", "Atlantic"))))
                .contains(entry("Metropolitan", ImmutableList.of(of("Detroit Red Wings", "Metropolitan"), of("New York Rangers", "Metropolitan"))))
                .contains(entry("Central", ImmutableList.of(of("Nashville Predators", "Central"), of("Dallas Stars", "Central"))));

    }
}
