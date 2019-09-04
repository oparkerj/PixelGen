package com.ssplugins.pixelgen.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class Comparing<T> implements Comparator<T> {
    
    private List<Comparator<? super T>> extractors = new ArrayList<>();
    
    public <U extends Comparable<? super U>> Comparing(Function<? super T, ? extends U> extractor) {
        extractors.add(Comparator.comparing(extractor));
    }
    
    public Comparing(Comparator<? super T> comparator) {
        extractors.add(comparator);
    }
    
    public static <T, U extends Comparable<? super U>> Comparing<T> value(Function<? super T, ? extends U> extractor) {
        return new Comparing<>(extractor);
    }
    
    public static <T, U extends Comparable<? super U>> Comparing<T> reverse(Function<? super T, ? extends U> extractor) {
        return new Comparing<>(Comparator.comparing(extractor).reversed());
    }
    
    public <U extends Comparable<? super U>> Comparing<T> then(Function<? super T, ? extends U> extractor) {
        extractors.add(Comparator.comparing(extractor));
        return this;
    }
    
    public <U extends Comparable<? super U>> Comparing<T> then(Comparator<? super T> comparator) {
        extractors.add(comparator);
        return this;
    }
    
    public <U extends Comparable<? super U>> Comparing<T> thenReversed(Function<? super T, ? extends U> extractor) {
        extractors.add(Comparator.comparing(extractor).reversed());
        return this;
    }
    
    @Override
    public int compare(T o1, T o2) {
        int c;
        for (int i = 0, l = extractors.size(); i < l; i++) {
            if (i == l - 1) return extractors.get(i).compare(o1, o2);
            c = extractors.get(i).compare(o1, o2);
            if (c != 0) return c;
        }
        return 0;
    }
    
}
