package com.arny.arnylib.adapters;

public interface BindableAdapterFilter<T> {
    boolean onFilterItem(CharSequence constraint, T item);
}