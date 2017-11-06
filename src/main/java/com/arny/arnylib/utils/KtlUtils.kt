package com.arny.java.utils

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
    return res;
}