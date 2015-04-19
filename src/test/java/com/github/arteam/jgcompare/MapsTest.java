package com.github.arteam.jgcompare;

import com.google.common.collect.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Date: 5/15/14
 * Time: 12:06 AM
 *
 * @author Artem Prigoda
 */
public class MapsTest {

    private final List<Team> teams = ImmutableList.of(
            new Team(12, "Chicago Blackhawks"),
            new Team(42, "St. Louis Blues"),
            new Team(88, "Colorado Avalanche"));

    @Test
    public void testUniqueIndex() {
        Map<Long, Team> expected = ImmutableMap.of(
                12L, new Team(12, "Chicago Blackhawks"),
                42L, new Team(42, "St. Louis Blues"),
                88L, new Team(88, "Colorado Avalanche"));
        assertThat(Maps.uniqueIndex(teams, Team::getId)).isEqualTo(expected);

        assertThat(teams.stream().collect(Collectors.toMap(Team::getId, Function.identity())))
                .isEqualTo(expected);
    }

    @Test
    public void testAsNavigableMap() {
        Map<Integer, String> teams = ImmutableMap.<Integer, String>builder()
                .put(21, "Boston Bruins")
                .put(24, "Los Angeles Kings")
                .put(12, "Chicago Blackhawks")
                .put(42, "St. Louis Blues")
                .put(29, "Arizona Coyotes")
                .put(92, "Winnipeg Jets")
                .put(88, "Colorado Avalanche")
                .build();
        Map<Integer, String> expected = ImmutableSortedMap.of(12, "Chicago Blackhawks", 42, "St. Louis Blues", 88, "Colorado Avalanche");

        Set<Integer> source = ImmutableSortedSet.of(42, 88, 12);
        assertThat(Maps.asMap(source, teams::get)).isEqualTo(expected);

        Map<Integer, String> result = source.stream()
                .collect(Collectors.toMap(Function.identity(), teams::get,
                        (s1, s2) -> {
                            throw new IllegalArgumentException("Two values for the same key");
                        },
                        TreeMap::new));
        assertThat(result).isEqualTo(expected);
    }
}
