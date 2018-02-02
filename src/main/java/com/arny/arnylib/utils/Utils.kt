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

fun <T> findPosition(list: List<T>, item: T): Int {
    return list.indexOf(item)
}

fun <T> findPosition(list: Array<T>, item: T): Int {
    return list.indexOf(item)
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

/**
 * Универсальная функция окончаний
 * @param [count] число
 * @param [zero_other] слово с окончанием значения  [count] либо ноль,либо все остальные варианты включая от 11 до 19 (слов)
 * @param [one] слово с окончанием значения  [count]=1 (слово)
 * @param [two_four] слово с окончанием значения  [count]=2,3,4 (слова)
 */
fun getTermination(count: Int, zero_other: String, one: String, two_four: String): String {
    if (count % 100 in 11..19) {
        return count.toString() + " " + zero_other;
    }
    return when (count % 10) {
        1 -> count.toString() + " " + one;
        2, 3, 4 -> count.toString() + " " + two_four;
        else -> count.toString() + " " + zero_other;
    }
}
