package com.github.arteam.jgcompare;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(Iterables.all(source, it -> !it.isEmpty())).isTrue();
        assertThat(stream.allMatch(it -> !it.isEmpty())).isTrue();
    }

    @Test
    public void testAny() {
        assertThat(Iterables.any(source, it -> it.length() > 2)).isTrue();
        assertThat(stream.anyMatch(it -> it.length() > 2)).isTrue();
    }

    @Test
    public void testConcat() {
        List<String> addition = ImmutableList.of("ger", "d", "fm");
        List<String> result = ImmutableList.of("as", "q", "def", "ger", "d", "fm");
        assertThat(ImmutableList.copyOf(Iterables.concat(source, addition))).isEqualTo(result);
        assertThat(Stream.concat(stream, addition.stream()).collect(Collectors.toList())).isEqualTo(result);
    }

    @Test
    public void testContains() {
        assertThat(Iterables.contains(source, "q")).isTrue();
        assertThat(stream.anyMatch(s -> s.equals("q"))).isTrue();
    }

    @Test
    public void testCycle() {
        ImmutableList<String> expected = ImmutableList.of("as", "q", "def", "as", "q", "def", "as", "q", "def", "as");

        Iterator<String> iterator = Iterables.cycle(source).iterator();
        List<String> cycled = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            cycled.add(iterator.next());
        }
        assertThat(expected).isEqualTo(cycled);

        String[] streamAsArray = stream.toArray(String[]::new);
        assertThat(expected).isEqualTo(IntStream.iterate(0, i -> (i + 1) % 3)
                .mapToObj(i -> streamAsArray[i])
                .limit(10)
                .collect(Collectors.toList()));
    }

    @Test
    public void testFilter() {
        Iterable<String> result = Iterables.filter(Iterables.filter(source, s -> s.length() > 1),
                s -> s.startsWith("d"));
        assertThat(ImmutableList.copyOf(result)).isEqualTo(ImmutableList.of("def"));

        assertThat(stream
                .filter(s -> s.length() > 1)
                .filter(s -> s.startsWith("d"))
                .collect(Collectors.toList())).isEqualTo(ImmutableList.of("def"));
    }

    @Test
    public void testFind() {
        assertThat(Iterables.find(source, it -> it.length() == 1)).isEqualTo("q");
        assertThat(stream.filter(it -> it.length() == 1).findAny().get()).isEqualTo("q");
    }

    @Test
    public void testFindDefaultValue() {
        assertThat(Iterables.find(source, it -> it.length() == 4, "abcd")).isEqualTo("abcd");
        assertThat(stream.filter(it -> it.length() == 4).findAny().orElse("abcd")).isEqualTo("abcd");
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

        assertThat(Iterables.frequency(source, "def")).isEqualTo(3);
        assertThat(stream
                .filter(s -> s.equals("def"))
                .count()).isEqualTo(3);
    }

    @Test
    public void testGetFirst() {
        assertThat(Iterables.getFirst(source, "")).isEqualTo("as");
        assertThat(stream.findFirst().orElse("")).isEqualTo("as");
    }

    @Test
    public void testGetLast() {
        assertThat(Iterables.getLast(source, "")).isEqualTo("def");
        assertThat(stream.reduce((l, r) -> r).get()).isEqualTo("def");
    }

    @Test
    public void testGetOnlyElement() {
        assertThat(Iterables.getOnlyElement(Iterables.filter(source, s -> s.length() == 3))).isEqualTo("def");
        assertThat(stream.filter(s -> s.length() == 3).findFirst().get()).isEqualTo("def");
    }

    @Test
    public void testGetOnlyElementWithDefault() {
        assertThat(Iterables.getOnlyElement(Iterables.filter(source, s -> s.length() == 4), "mann")).isEqualTo("mann");
        assertThat(stream.filter(s -> s.length() == 4).findFirst().orElse("mann")).isEqualTo("mann");
    }

    @Test
    public void testIndexOf() {
        assertThat(Iterables.indexOf(source, it -> it.length() == 1)).isEqualTo(1);

        // Rather tricky way
        assertThat(StreamUtils.withIndex(stream)
                .filter(e -> e.getElement().length() == 1)
                .map(e -> e.getIndex())
                .findFirst()
                .get())
                .isEqualTo(1);
    }

    @Test
    public void testIsEmpty() {
        assertThat(Iterables.isEmpty(source)).isEqualTo(false);
        assertThat(stream.noneMatch(s -> true)).isEqualTo(false);
    }

    @Test
    public void testLimit() {
        assertThat(Lists.newArrayList(Iterables.limit(source, 2))).isEqualTo(Arrays.asList("as", "q"));
        assertThat(stream.limit(2).collect(Collectors.toList())).isEqualTo(Arrays.asList("as", "q"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPartition() {
        List<String> source = ImmutableList.of("trash", "talk", "arg", "loose", "fade", "cross", "dump", "bust");
        assertThat(Iterables.partition(source, 3)).containsExactly(
                ImmutableList.of("trash", "talk", "arg"),
                ImmutableList.of("loose", "fade", "cross"),
                ImmutableList.of("dump", "bust"));

        int partitionSize = 3;
        List<List<String>> partitions = StreamUtils.withIndex(source.stream())
                .collect(ArrayList::new, (lists, el) -> {
                    int place = el.getIndex() % partitionSize;
                    List<String> part;
                    if (place == 0) {
                        part = new ArrayList<>();
                        lists.add(part);
                    } else {
                        part = lists.get(lists.size() - 1);
                    }
                    part.add(el.getElement());
                }, ArrayList::addAll);
        assertThat(partitions).containsExactly(
                ImmutableList.of("trash", "talk", "arg"),
                ImmutableList.of("loose", "fade", "cross"),
                ImmutableList.of("dump", "bust"));
    }

    @Test
    public void testTransform() {
        assertThat(Lists.newArrayList(Iterables.transform(source, String::length))).isEqualTo(Arrays.asList(2, 1, 3));
        assertThat(stream.map(String::length).collect(Collectors.toList())).isEqualTo(Arrays.asList(2, 1, 3));
    }

    @Test
    public void testTryFind() {
        assertThat(Iterables.tryFind(source, it -> it.length() == 4).or("abcd")).isEqualTo("abcd");
        assertThat(stream.filter(it -> it.length() == 4).findAny().orElse("abcd")).isEqualTo("abcd");
    }


    @Test
    public void testRemoveIf() {
        Iterables.removeIf(source, it -> it.length() < 3);
        assertThat(Arrays.asList("def")).isEqualTo(source);

        List<String> removed = stream
                .filter(((Predicate<String>) it -> it.length() < 3).negate())
                .collect(Collectors.toList());
        assertThat(Arrays.asList("def")).isEqualTo(removed);
    }


    @Test
    public void testSkip() {
        assertThat(Lists.newArrayList(Iterables.skip(source, 2))).isEqualTo(Arrays.asList("def"));
        assertThat(stream.skip(2).collect(Collectors.toList())).isEqualTo(Arrays.asList("def"));
    }

}


