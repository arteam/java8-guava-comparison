package com.github.arteam.jgcompare;

import com.google.common.base.Splitter;
import com.google.common.collect.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Date: 4/16/15
 * Time: 11:52 PM
 *
 * @author Artem Prigoda
 */
public class FluentIterablesTest {

    Iterable<String> source = Lists.newArrayList("1 18", "20 24 9", "92");
    Stream<String> stream = Stream.of("1 18", "20 24 9", "92");

    @Test
    public void testTransformAndConcat() {
        Splitter splitter = Splitter.on(" ");
        assertThat(FluentIterable.from(source)
                .transformAndConcat(splitter::split)
                .transform(Integer::parseInt)
                .toList()).containsOnly(1, 18, 20, 24, 9, 92);
        assertThat(stream.flatMap(s -> Stream.of(s.split(" ")))
                .map(Integer::parseInt)
                .collect(Collectors.toList())).containsOnly(1, 18, 20, 24, 9, 92);
    }


    @Test
    public void index() {
        Splitter splitter = Splitter.on(" ");
        assertThat(FluentIterable.from(source)
                .transformAndConcat(splitter::split)
                .index(s -> s.substring(0, 1))).isEqualTo(ImmutableListMultimap.<String, String>builder()
                .putAll("1", "1", "18")
                .putAll("2", "20", "24")
                .putAll("9", "9", "92")
                .build());

        assertThat(stream.flatMap(s -> Stream.of(s.split(" ")))
                .collect(Collectors.groupingBy(s -> s.substring(0, 1))))
                .isEqualTo(ImmutableMap.<String, List<String>>builder()
                        .put("1", ImmutableList.of("1", "18"))
                        .put("2", ImmutableList.of("20", "24"))
                        .put("9", ImmutableList.of("9", "92"))
                        .build());
    }
}
