package com.github.arteam.jgcompare;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Date: 4/14/15
 * Time: 9:04 PM
 *
 * @author Artem Prigoda
 */
public class SetsTest {

    Set<String> first = ImmutableSet.of("tweak", "perf", "bully", "vertex");
    Set<String> second = ImmutableSet.of("perf", "moan", "tweak");

    @Test
    @SuppressWarnings("unchecked")
    public void testCartesianProduct() {
        Set<String> first = ImmutableSet.of("1", "2");
        Set<String> second = ImmutableSet.of("floor", "arch", "gamble");
        assertThat(Sets.cartesianProduct(first, second)).containsOnly(
                ImmutableList.of("1", "floor"),
                ImmutableList.of("1", "arch"),
                ImmutableList.of("1", "gamble"),
                ImmutableList.of("2", "floor"),
                ImmutableList.of("2", "arch"),
                ImmutableList.of("2", "gamble"));

        assertThat(first.stream()
                .flatMap(s1 -> second.stream().map(s2 -> Arrays.asList(s1, s2)))
                .collect(Collectors.toSet())).containsOnly(
                ImmutableList.of("1", "floor"),
                ImmutableList.of("1", "arch"),
                ImmutableList.of("1", "gamble"),
                ImmutableList.of("2", "floor"),
                ImmutableList.of("2", "arch"),
                ImmutableList.of("2", "gamble"));
    }

    @Test
    public void testDifference() {
        assertThat(Sets.difference(first, second)).containsOnly("bully", "vertex");

        assertThat(first.stream()
                .filter(s -> !second.contains(s))
                .collect(Collectors.toSet()))
                .containsOnly("bully", "vertex");
    }

    @Test
    public void testSymmetricDifference() {
        assertThat(Sets.symmetricDifference(first, second)).containsOnly("bully", "vertex", "moan");

        assertThat(Stream.concat(first.stream().filter(s -> !second.contains(s)),
                second.stream().filter(s -> !first.contains(s)))
                .collect(Collectors.toSet()))
                .containsOnly("bully", "vertex", "moan");

    }

    @Test
    public void testUnion() {
        assertThat(Sets.union(first, second)).containsOnly("tweak", "perf", "bully", "vertex", "moan");
        assertThat(Stream.concat(first.stream(), second.stream()).collect(Collectors.toSet()))
                .containsOnly("tweak", "perf", "bully", "vertex", "moan");
    }
}
