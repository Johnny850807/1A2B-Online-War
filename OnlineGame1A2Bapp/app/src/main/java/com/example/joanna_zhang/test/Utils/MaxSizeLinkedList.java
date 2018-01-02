package com.example.joanna_zhang.test.Utils;


import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.LinkedList;

public class MaxSizeLinkedList<E> extends LinkedList<E>{
    private final int maxSize;

    public MaxSizeLinkedList(int maxSize) {
        this.maxSize = maxSize;
    }

    public MaxSizeLinkedList( int maxSize, @NonNull Collection<? extends E> c) {
        super(c);
        this.maxSize = maxSize;
        trim();
    }

    public void trim(){
        while(size() > maxSize)
            pop();
    }

    @Override
    public boolean add(E e) {
        super.add(e);
        trim();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        boolean contain = super.remove(o);
        trim();
        return contain;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = super.addAll(c);
        trim();
        return changed;
    }


}
