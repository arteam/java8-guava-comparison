package com.github.arteam.jgcompare;

import com.google.common.collect.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Date: 3/23/14
 * Time: 5:57 PM
 *
 * @author Artem Prigoda
 */
public class IterablesTest {
    List<String> source = Lists.newArrayList("as", "q", "def");

    @Test
    public void testAll() {
        assertTrue(Iterables.all(source, it -> !it.isEmpty()));
        assertTrue(source.stream().allMatch(it -> !it.isEmpty()));
    }

    @Test
    public void testAny() {
        assertTrue(Iterables.any(source, it -> it.length() > 2));
        assertTrue(source.stream().anyMatch(it -> it.length() > 2));
    }

    @Test
    public void testConcat() {
        List<String> addition = ImmutableList.of("ger", "d", "fm");
        List<String> result = ImmutableList.of("as", "q", "def", "ger", "d", "fm");
        assertEquals(ImmutableList.copyOf(Iterables.concat(source, addition)), result);
        assertEquals(Stream.concat(source.stream(), addition.stream()).collect(Collectors.toList()), result);
    }

    @Test
    public void testContains() {
        assertTrue(Iterables.contains(source, "q"));
        assertTrue(source.contains("q"));
    }

    @Test
    public void testCycle() {
        Iterator<String> iterator = Iterables.cycle(source).iterator();
        for (int i = 0; i < 10; i++) {
            System.out.println(iterator.next());
        }
        long count = Stream.iterate(0, i -> (i + 1) % 3)
                .map(i -> source.get(i))
                .peek(System.out::println)
                .limit(10)
                .count();
        assertEquals(count, 10);
    }

    @Test
    public void testFilter() {
        Iterable<String> result = Iterables.filter(Iterables.filter(source, s -> s.length() > 1),
                s -> s.startsWith("d"));
        assertEquals(ImmutableList.copyOf(result), ImmutableList.of("def"));

        assertEquals(source.stream()
                .filter(s -> s.length() > 1)
                .filter(s -> s.startsWith("d"))
                .collect(Collectors.toList()), ImmutableList.of("def"));
    }

    @Test
    public void testFind() {
        assertThat(Iterables.find(source, it -> it.length() == 1), equalTo("q"));
        assertThat(source.stream().filter(it -> it.length() == 1).findAny().get(), equalTo("q"));
    }

    @Test
    public void testFindDefaultValue() {
        assertThat(Iterables.find(source, it -> it.length() == 4, "abcd"), equalTo("abcd"));
        assertThat(source.stream().filter(it -> it.length() == 4).findAny().orElse("abcd"), equalTo("abcd"));
    }

    @Test
    public void testFrequency() {
        List<String> newSource = ImmutableList.<String>builder()
                .addAll(source)
                .add("def")
                .add("try")
                .add("q")
                .add("def")
                .build();
        assertEquals(Iterables.frequency(newSource, "def"), 3);
        assertEquals(newSource.stream()
                .filter(s -> s.equals("def"))
                .count(), 3);
    }

    @Test
    public void testTryFind() {
        assertThat(Iterables.tryFind(source, it -> it.length() == 4).or("abcd"), equalTo("abcd"));
        assertThat(source.stream().filter(it -> it.length() == 4).findAny().orElse("abcd"), equalTo("abcd"));
    }

    @Test
    public void testGetFirst() {
        assertThat(Iterables.getFirst(source, ""), equalTo("as"));
        assertThat(source.stream().findFirst().orElse(""), equalTo("as"));
    }

    @Test
    public void testGetLast() {
        assertThat(Iterables.getLast(source, ""), equalTo("def"));
        assertThat(source.stream().skip(source.size() - 1).findFirst().orElse(""), equalTo("def"));
    }

    @Test
    public void testIndexOf() {
        assertThat(Iterables.indexOf(source, it -> it.length() == 1), equalTo(1));
        assertThat(source.indexOf(source.stream().filter(it -> it.length() == 1).findAny().get()), equalTo(1));
    }

    @Test
    public void testRemoveIf() {
        Iterables.removeIf(source, it -> it.length() < 3);
        assertEquals(Arrays.asList("def"), source);
        source.removeIf(it -> it.length() < 3);
        assertEquals(Arrays.asList("def"), source);
    }

    @Test
    public void testTransform() {
        assertThat(Lists.newArrayList(Iterables.transform(source, String::length)), equalTo(Arrays.asList(2, 1, 3)));
        assertThat(source.stream().map(String::length).collect(Collectors.toList()), equalTo(Arrays.asList(2, 1, 3)));
    }

    @Test
    public void testSkip() {
        assertThat(Lists.newArrayList(Iterables.skip(source, 2)), equalTo(Arrays.asList("def")));
        assertThat(source.stream().skip(2).collect(Collectors.toList()), equalTo(Arrays.asList("def")));
    }

    @Test
    public void testLimit() {
        assertThat(Lists.newArrayList(Iterables.limit(source, 2)), equalTo(Arrays.asList("as", "q")));
        assertThat(source.stream().limit(2).collect(Collectors.toList()), equalTo(Arrays.asList("as", "q")));
    }

}


