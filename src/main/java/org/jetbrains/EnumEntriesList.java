package org.jetbrains;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Vsevolod Tolstopyatov
 * @since 30.11.2021
 */
public class EnumEntriesList<E extends Enum<E>> extends AbstractList<E> {

    private static class SafelyPublishedEntries<E extends Enum<E>> {

        public static final SafelyPublishedEntries<?> NONE = new SafelyPublishedEntries<TimeUnit>(new TimeUnit[0]);

        public final E[] array;

        public SafelyPublishedEntries(E[] array) {
            this.array = array;
        }
    }

    private final Supplier<E[]> entiresSupplier;
    private SafelyPublishedEntries<E> lazyEntries = (SafelyPublishedEntries<E>) SafelyPublishedEntries.NONE;

    public EnumEntriesList(Supplier<E[]> entiresSupplier) {
        this.entiresSupplier = entiresSupplier;
    }

    private E[] getEntries() {
        SafelyPublishedEntries<E> entries = this.lazyEntries;
        if (entries == SafelyPublishedEntries.NONE) {
            entries = new SafelyPublishedEntries<>(entiresSupplier.get());
            lazyEntries = entries;
        }
        return entries.array;
    }

    @Override
    public E get(int index) {
        E[] arr = getEntries();
        rangeCheck(index, arr);
        return arr[index];
    }

    private void rangeCheck(int index, E[] arr) {
        if (index >= arr.length)
            throw new IndexOutOfBoundsException("IOOBE " + index + " 0.." + (arr.length - 1));
    }

    @Override
    public int size() {
        return getEntries().length;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        final E[] arr = getEntries();
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such

        Itr() {}

        public boolean hasNext() {
            return cursor != arr.length;
        }

        @Override
        public E next() {
            int i = cursor;
            if (i >= arr.length)
                throw new NoSuchElementException();

            E[] elementData = arr;
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }
    }
}
