package jgcompare;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Date: 5/15/14
 * Time: 12:06 AM
 *
 * @author Artem Prigoda
 */
public class MapsTest {

    List<Team> teams = ImmutableList.of(
            new Team(12, "Chicago Blackhawks"),
            new Team(42, "St. Louis Blues"),
            new Team(88, "Colorado Avalanche"));

    @Test
    public void testUniqueIndex() {
        Map<Long, Team> expected = ImmutableMap.of(
                12L, new Team(12, "Chicago Blackhawks"),
                42L, new Team(42, "St. Louis Blues"),
                88L, new Team(88, "Colorado Avalanche"));
        assertThat(Maps.uniqueIndex(teams, Team::getId), equalTo(expected));

        assertThat(teams
                .stream()
                .collect(Collectors.toMap(Team::getId, (team) -> team)),
                equalTo(expected));
    }

    private static class Team {
        private final long id;
        private final String name;

        private Team(long id, String name) {
            this.id = id;
            this.name = name;
        }

        private long getId() {
            return id;
        }

        private String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Team) {
                Team that = (Team) o;
                return Objects.equals(id, that.id) && Objects.equals(name, that.name);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }


    }
}
