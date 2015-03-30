package com.github.arteam.jgcompare;

import java.util.*;
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

    public static class ElementIndex<T> {

        private final int index;
        private final T element;

        public ElementIndex(int index, T element) {
            this.index = index;
            this.element = element;
        }

        public int getIndex() {
            return index;
        }

        public T getElement() {
            return element;
        }
    }
}
