package com.github.arteam.jgcompare;

import com.google.common.base.Splitter;
import com.google.common.collect.*;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * Date: 5/15/14
 * Time: 12:06 AM
 *
 * @author Artem Prigoda
 */
public class MapsTest {

    private static Splitter WHITESPACE_SPLITTER = Splitter.on(" ");

    private static String getLastWord(String text) {
        return Iterables.getLast(WHITESPACE_SPLITTER.split(text));
    }

    private static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> toMap() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    Map<Integer, String> teams = ImmutableMap.<Integer, String>builder()
            .put(21, "Boston Bruins")
            .put(24, "Los Angeles Kings")
            .put(12, "Chicago Blackhawks")
            .put(42, "St. Louis Blues")
            .put(29, "Arizona Coyotes")
            .put(92, "Winnipeg Jets")
            .put(88, "Colorado Avalanche")
            .build();


    @Test
    public void testUniqueIndex() {
        List<Team> teams = ImmutableList.of(
                new Team(12, "Chicago Blackhawks"),
                new Team(42, "St. Louis Blues"),
                new Team(88, "Colorado Avalanche"));
        Map<Long, Team> expected = ImmutableMap.of(
                12L, new Team(12, "Chicago Blackhawks"),
                42L, new Team(42, "St. Louis Blues"),
                88L, new Team(88, "Colorado Avalanche"));
        assertThat(Maps.uniqueIndex(teams, Team::getId)).isEqualTo(expected);

        assertThat(teams.stream()
                .collect(Collectors.toMap(Team::getId, Function.identity())))
                .isEqualTo(expected);
    }

    @Test
    public void testAsNavigableMap() {
        Map<Integer, String> expected = ImmutableSortedMap.of(12, "Chicago Blackhawks", 42, "St. Louis Blues", 88, "Colorado Avalanche");

        NavigableSet<Integer> source = ImmutableSortedSet.of(42, 88, 12);
        assertThat(Maps.asMap(source, teams::get)).isEqualTo(expected);

        Map<Integer, String> result = source.stream()
                .collect(Collectors.toMap(Function.identity(), teams::get,
                        (s1, s2) -> {
                            throw new IllegalArgumentException("Two values for the same key");
                        },
                        TreeMap::new));
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testSetAsMap() {
        Map<Integer, String> expected = ImmutableMap.of(12, "Chicago Blackhawks", 42, "St. Louis Blues", 88, "Colorado Avalanche");
        Set<Integer> source = ImmutableSet.of(42, 88, 12);

        assertThat(Maps.asMap(source, teams::get)).isEqualTo(expected);
        assertThat(source.stream().collect(Collectors.toMap(Function.identity(), teams::get))).isEqualTo(expected);
    }

    @Test
    public void testDifference() {
        Map<Integer, String> stanleyCupWinners = ImmutableMap.<Integer, String>builder()
                .put(21, "Boston Bruins")
                .put(24, "Los Angeles Kings")
                .put(12, "Chicago Blackhawks")
                .put(88, "Colorado Avalanche")
                .build();
        assertThat(Maps.difference(teams, stanleyCupWinners).entriesOnlyOnLeft())
                .contains(entry(42, "St. Louis Blues"), entry(92, "Winnipeg Jets"), entry(29, "Arizona Coyotes"));

        assertThat(teams.entrySet()
                .stream()
                .filter(e -> !stanleyCupWinners.containsKey(e.getKey()))
                .collect(toMap()))
                .contains(entry(42, "St. Louis Blues"), entry(92, "Winnipeg Jets"), entry(29, "Arizona Coyotes"));
    }

    @Test
    public void testFilterEntries() {
        assertThat(Maps.filterEntries(teams, e -> e.getKey() > 20 && e.getValue().startsWith("C")))
                .containsOnly(entry(88, "Colorado Avalanche"));

        assertThat(teams.entrySet().stream()
                .filter(e -> e.getKey() > 20 && e.getValue().startsWith("C"))
                .collect(toMap()))
                .containsOnly(entry(88, "Colorado Avalanche"));
    }

    @Test
    public void testFilterKeys() {
        assertThat(Maps.filterKeys(teams, k -> k > 50)).containsOnly(entry(88, "Colorado Avalanche"),
                entry(92, "Winnipeg Jets"));
        assertThat(teams.entrySet().stream()
                .filter(e -> e.getKey() > 50)
                .collect(toMap()))
                .containsOnly(entry(88, "Colorado Avalanche"),
                        entry(92, "Winnipeg Jets"));

    }

    @Test
    public void testFilterValues() {
        assertThat(Maps.filterValues(teams, v -> getLastWord(v).startsWith("B")))
                .containsOnly(entry(21, "Boston Bruins"), entry(12, "Chicago Blackhawks"), entry(42, "St. Louis Blues"));
        assertThat(teams.entrySet().stream().filter(e -> getLastWord(e.getValue()).startsWith("B"))
                .collect(toMap()))
                .containsOnly(entry(21, "Boston Bruins"), entry(12, "Chicago Blackhawks"), entry(42, "St. Louis Blues"));
    }


    @Test
    public void testTransformValues() {
        assertThat(Maps.transformValues(teams, v -> getLastWord(v))).containsOnly(
                entry(21, "Bruins"),
                entry(24, "Kings"),
                entry(12, "Blackhawks"),
                entry(42, "Blues"),
                entry(29, "Coyotes"),
                entry(92, "Jets"),
                entry(88, "Avalanche")
        );
        assertThat(teams.entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), getLastWord(e.getValue())))
                .collect(toMap())).containsOnly(
                entry(21, "Bruins"),
                entry(24, "Kings"),
                entry(12, "Blackhawks"),
                entry(42, "Blues"),
                entry(29, "Coyotes"),
                entry(92, "Jets"),
                entry(88, "Avalanche")
        );

    }
}
