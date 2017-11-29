package com.arny.arnylib.utils

fun <T> find(list: List<T>, c:T, comp: Comparator<T>): T? {
    for (t in list) {
        if (comp.compare(c, t) == 0) {
            return t
        }
    }
    return null
}