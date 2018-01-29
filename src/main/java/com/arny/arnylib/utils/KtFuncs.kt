package com.arny.arnylib.utils

import java.util.*

fun <T> find(list: List<T>, c:T, comp: Comparator<T>): T? {
    for (t in list) {
        if (comp.compare(c, t) == 0) {
            return t
        }
    }
    return null
}

fun <T> getExcludeList(list: ArrayList<T>, items: List<T>, comparator: Comparator<T>): ArrayList<T> {
    val res = ArrayList<T>()
    for (t in list) {
        val pos = Collections.binarySearch(items, t, comparator)
        if (pos < 0) {
            res.add(t)
        }
    }
    return res
}