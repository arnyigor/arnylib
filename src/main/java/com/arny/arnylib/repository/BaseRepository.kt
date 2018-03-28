package com.arny.arnylib.repository

import android.content.Context
import com.arny.arnylib.utils.Config

abstract class BaseRepository : BaseRepositoryContract {
    abstract fun getContext(): Context?
    override fun getResString(resId: Int): String? {
        return getContext()?.getString(resId)
    }

    override fun getPrefString(key: String, default: String?): String? {
        if (default != null) {
            return getContext()?.let { Config.getString(key, it, default) }
        }
        return getContext()?.let { Config.getString(key, it) }
    }

    override fun setPrefString(key: String, value: String) {
        getContext()?.let { Config.setString(key, value, it) }
    }

    override fun setPrefBoolean(key: String, value: Boolean) {
        getContext()?.let { Config.setBoolean(key, value, it) }
    }

    override fun getPrefBoolean(key: String, default: Boolean): Boolean? {
        return getContext()?.let { Config.getBoolean(key, default, it) }
    }
}