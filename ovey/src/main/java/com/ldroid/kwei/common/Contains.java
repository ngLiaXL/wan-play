package com.ldroid.kwei.common;

import java.util.ArrayList;
import java.util.List;

public class Contains<T> {

    private Function<T> equalityFunction;
    private List<T> sourceList;

    public Contains(Function<T> equalityFunction) {
        this.equalityFunction = equalityFunction;
    }

    public Contains(List<T> sourceList, Function<T> equalityFunction) {
        this.sourceList = sourceList;
        this.equalityFunction = equalityFunction;
    }


    public void appendIf() {
        if (sourceList == null) {
            return;
        }
        List<T> resultList = new ArrayList<>();
        for (T outer : sourceList) {
            if (!equalityFunction.test(outer)) {
                resultList.add(outer);
                continue;
            }
            T inner = contains(resultList, outer);
            if (inner != null) {
                equalityFunction.append(inner, outer);
            } else {
                resultList.add(outer);
            }
        }
        sourceList.clear();
        sourceList.addAll(resultList);
    }


    public T contains(List<T> innerList, T outer) {
        if (innerList == null || outer == null) {
            return null;
        }
        for (T inner : innerList) {
            if (inner != null && equalityFunction.test2(inner, outer)) {
                return inner;
            }
        }
        return null;
    }


    public interface Function<T> {
        boolean test(T outer);

        boolean test2(T inner, T outer);

        void append(T inner, T outer);
    }

    public static class FunctionImpl<T> implements Function<T> {

        @Override
        public boolean test(T outer) {
            return true;
        }

        @Override
        public boolean test2(T inner, T outer) {
            return true;
        }

        @Override
        public void append(T inner, T outer) {

        }
    }

}
