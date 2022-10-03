package com.github.arteam.jgcompare.util;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {

    public static <T> Stream<ElementIndex<T>> withIndex(Stream<? extends T> stream) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<ElementIndex<T>>() {
            private final Iterator<? extends T> streamIterator = stream.iterator();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return streamIterator.hasNext();
            }

            @Override
            public ElementIndex<T> next() {
                return new ElementIndex<>(index++, streamIterator.next());
            }
        }, Spliterator.ORDERED | Spliterator.IMMUTABLE), false);
    }

    public record ElementIndex<T>(int index, T element) {
    }
}
