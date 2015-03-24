package jgcompare;

import com.google.common.collect.*;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    public void testRemoveIf() {
        Iterables.removeIf(source, it -> it.length() < 3);
        assertEquals(Arrays.asList("def"), source);
        source.removeIf(it -> it.length() < 3);
        assertEquals(Arrays.asList("def"), source);
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
    public void testTryFind() {
        assertThat(Iterables.tryFind(source, it -> it.length() == 4).or("abcd"), equalTo("abcd"));
        assertThat(source.stream().filter(it -> it.length() == 4).findAny().orElse("abcd"), equalTo("abcd"));
    }

    @Test
    public void testIndexOf() {
        assertThat(Iterables.indexOf(source, it -> it.length() == 1), equalTo(1));
        assertThat(source.indexOf(source.stream().filter(it -> it.length() == 1).findAny().get()), equalTo(1));
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


