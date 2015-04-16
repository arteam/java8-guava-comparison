package com.github.arteam.jgcompare;

import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
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

}
