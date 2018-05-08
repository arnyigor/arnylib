package com.arny.arnylib.repository

import com.arny.arnylib.utils.Config

abstract class BaseRepository : BaseRepositoryContract {
    override fun getString(resId: Int): String? {
        return getContext().getString(resId)
    }

    override fun getPrefInt(key: String): Int? {
        return getContext().let { Config.getInt(key, it) }
    }

    override fun getPrefString(key: String, default: String?): String? {
        if (default != null) {
            return getContext().let { Config.getString(key, it, default) }
        }
        return getContext().let { Config.getString(key, it) }
    }

    override fun setPrefString(key: String, value: String?) {
        getContext().let { Config.setString(key, value, it) }
    }

    override fun setPrefBoolean(key: String, value: Boolean) {
        getContext().let { Config.setBoolean(key, value, it) }
    }

    override fun getPrefBoolean(key: String, default: Boolean): Boolean {
        return getContext().let { Config.getBoolean(key, default, it) } ?: default
    }

    override fun removePref(key: String) {
        return getContext().let { Config.remove(key, it) } ?: Unit
    }
}