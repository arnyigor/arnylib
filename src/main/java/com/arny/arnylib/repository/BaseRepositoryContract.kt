package com.arny.arnylib.repository

import android.content.Context
import android.support.annotation.StringRes

interface BaseRepositoryContract {
    fun getContext(): Context
    fun getString(@StringRes resId: Int): String?
    fun getPrefString(key: String, default: String? = null): String?
    fun getPrefInt(key: String): Int?
    fun setPrefString(key: String,value: String?)
    fun setPrefInt(key: String,value: Int)
    fun setPrefBoolean(key: String,value: Boolean)
    fun getPrefBoolean(key: String,default: Boolean): Boolean
    fun removePref(key: String)
}