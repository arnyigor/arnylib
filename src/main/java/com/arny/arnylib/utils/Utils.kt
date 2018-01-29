package com.arny.java.utils

import android.content.Intent
import java.util.*

fun <T> find(list: List<T>, c: T, comp: Comparator<T>): T? {
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

fun getSQLType(fieldType: String): String {
    val res= when{
        fieldType.equals("int",true)->"INTEGER"
        fieldType.equals("integer",true)->"INTEGER"
        fieldType.equals("float",true)->"REAL"
        fieldType.equals("double",true)->"REAL"
        fieldType.equals("string",true)->"TEXT"
        fieldType.equals("char",true)->"TEXT"
        fieldType.equals("byte",true)->"TEXT"
        else->"TEXT"
    }
    return res
}

fun <T> getIntentExtra(intent: Intent?, extraName: String): T? {
    return intent?.extras?.get(extraName) as? T
}