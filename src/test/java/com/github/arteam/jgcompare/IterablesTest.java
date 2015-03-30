package com.github.arteam.jgcompare;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Date: 3/23/14
 * Time: 5:57 PM
 *
 * @author Artem Prigoda
 */
public class IterablesTest {

    Iterable<String> source = Lists.newArrayList("as", "q", "def");
    Stream<String> stream = Stream.of("as", "q", "def");

    @Test
    public void testAll() {
        assertTrue(Iterables.all(source, it -> !it.isEmpty()));
        assertTrue(stream.allMatch(it -> !it.isEmpty()));
    }

    @Test
    public void testAny() {
        assertTrue(Iterables.any(source, it -> it.length() > 2));
        assertTrue(stream.anyMatch(it -> it.length() > 2));
    }

    @Test
    public void testConcat() {
        List<String> addition = ImmutableList.of("ger", "d", "fm");
        List<String> result = ImmutableList.of("as", "q", "def", "ger", "d", "fm");
        assertEquals(ImmutableList.copyOf(Iterables.concat(source, addition)), result);
        assertEquals(Stream.concat(stream, addition.stream()).collect(Collectors.toList()), result);
    }

    @Test
    public void testContains() {
        assertTrue(Iterables.contains(source, "q"));
        assertTrue(stream.anyMatch(s -> s.equals("q")));
    }

    @Test
    public void testCycle() {
        ImmutableList<String> expected = ImmutableList.of("as", "q", "def", "as", "q", "def", "as", "q", "def", "as");

        Iterator<String> iterator = Iterables.cycle(source).iterator();
        List<String> cycled = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            cycled.add(iterator.next());
        }
        assertEquals(expected, cycled);

        String[] streamAsArray = stream.toArray(String[]::new);
        assertEquals(expected, IntStream.iterate(0, i -> (i + 1) % 3)
                .mapToObj(i -> streamAsArray[i])
                .limit(10)
                .collect(Collectors.toList()));
    }

    @Test
    public void testFilter() {
        Iterable<String> result = Iterables.filter(Iterables.filter(source, s -> s.length() > 1),
                s -> s.startsWith("d"));
        assertEquals(ImmutableList.copyOf(result), ImmutableList.of("def"));

        assertEquals(stream
                .filter(s -> s.length() > 1)
                .filter(s -> s.startsWith("d"))
                .collect(Collectors.toList()), ImmutableList.of("def"));
    }

    @Test
    public void testFind() {
        assertThat(Iterables.find(source, it -> it.length() == 1), equalTo("q"));
        assertThat(stream.filter(it -> it.length() == 1).findAny().get(), equalTo("q"));
    }

    @Test
    public void testFindDefaultValue() {
        assertThat(Iterables.find(source, it -> it.length() == 4, "abcd"), equalTo("abcd"));
        assertThat(stream.filter(it -> it.length() == 4).findAny().orElse("abcd"), equalTo("abcd"));
    }


    @Test
    public void testFrequency() {
        List<String> source = ImmutableList.<String>builder()
                .addAll(this.source)
                .add("def")
                .add("try")
                .add("q")
                .add("def")
                .build();
        Stream<String> stream = source.stream();

        assertEquals(Iterables.frequency(source, "def"), 3);
        assertEquals(stream
                .filter(s -> s.equals("def"))
                .count(), 3);
    }

    @Test
    public void testTryFind() {
        assertThat(Iterables.tryFind(source, it -> it.length() == 4).or("abcd"), equalTo("abcd"));
        assertThat(stream.filter(it -> it.length() == 4).findAny().orElse("abcd"), equalTo("abcd"));
    }

    @Test
    public void testGetFirst() {
        assertThat(Iterables.getFirst(source, ""), equalTo("as"));
        assertThat(stream.findFirst().orElse(""), equalTo("as"));
    }

    @Test
    public void testGetLast() {
        assertThat(Iterables.getLast(source, ""), equalTo("def"));
        assertThat(stream.reduce((l, r) -> r).get(), equalTo("def"));
    }

    @Test
    public void testIndexOf() {
        assertThat(Iterables.indexOf(source, it -> it.length() == 1), equalTo(1));

        // No way for indexOf for Stream
    }

    @Test
    public void testRemoveIf() {
        Iterables.removeIf(source, it -> it.length() < 3);
        assertEquals(Arrays.asList("def"), source);

        List<String> removed = stream
                .filter(((Predicate<String>) it -> it.length() < 3).negate())
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("def"), removed);
    }

    @Test
    public void testTransform() {
        assertThat(Lists.newArrayList(Iterables.transform(source, String::length)), equalTo(Arrays.asList(2, 1, 3)));
        assertThat(stream.map(String::length).collect(Collectors.toList()), equalTo(Arrays.asList(2, 1, 3)));
    }

    @Test
    public void testSkip() {
        assertThat(Lists.newArrayList(Iterables.skip(source, 2)), equalTo(Arrays.asList("def")));
        assertThat(stream.skip(2).collect(Collectors.toList()), equalTo(Arrays.asList("def")));
    }

    @Test
    public void testLimit() {
        assertThat(Lists.newArrayList(Iterables.limit(source, 2)), equalTo(Arrays.asList("as", "q")));
        assertThat(stream.limit(2).collect(Collectors.toList()), equalTo(Arrays.asList("as", "q")));
    }

}


