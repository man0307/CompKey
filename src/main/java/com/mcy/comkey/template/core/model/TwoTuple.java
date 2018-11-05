package com.mcy.comkey.template.core.model;

public class TwoTuple<L,E> {
     private L first;
     private E second;

    public L getFirst() {
        return first;
    }

    public void setFirst(L first) {
        this.first = first;
    }

    public E getSecond() {
        return second;
    }

    public void setSecond(E second) {
        this.second = second;
    }
}
