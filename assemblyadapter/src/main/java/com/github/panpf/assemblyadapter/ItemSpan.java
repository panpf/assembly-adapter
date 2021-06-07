package com.github.panpf.assemblyadapter;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Locale;

public class ItemSpan {

    public int span;

    private ItemSpan(int span) {
        this.span = span;
    }

    @NonNull
    public static ItemSpan fullSpan() {
        return new ItemSpan(-1);
    }

    @NonNull
    public static ItemSpan span(int span) {
        return new ItemSpan(span);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemSpan itemSpan = (ItemSpan) o;
        return span == itemSpan.span;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[]{span});
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "ItemSpan{span=%d}", span);
    }
}
