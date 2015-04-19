package com.github.arteam.jgcompare;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
}
