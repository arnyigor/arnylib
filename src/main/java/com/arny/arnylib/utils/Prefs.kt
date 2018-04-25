package com.arny.arnylib.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.arny.arnylib.database.SingletonHolder

class Prefs private constructor(val context: Context) {
    private var settings: SharedPreferences? = null

    init {
        settings = PreferenceManager.getDefaultSharedPreferences(context)
    }

    companion object : SingletonHolder<Prefs, Context>(::Prefs)

    /**
     * Получение конфига по ключу
     *
     * @param key     Ключ
     * @param context Контекст
     * @return Значение конфига
     */
    fun getString(key: String): String? {
        return settings?.getString(key, null)
    }

    /**
     * Получение конфига по ключу
     *
     * @param key        Ключ
     * @param context    Контекст
     * @param defaultVal Значение по умолчанию
     * @return Значение конфига
     */
    fun getString(key: String, defaultVal: String): String? {
        return settings?.getString(key, defaultVal)
    }

    /**
     * Получение конфига по ключу
     *
     * @param key     Ключ
     * @param context Контекст
     * @return Значение конфига
     */
    fun getInt(key: String): Int {
        return settings?.getInt(key, 0) ?: 0
    }

    fun getLong(key: String): Long {
        return settings?.getLong(key, 0) ?: 0.toLong()
    }

    /**
     * Получение конфига по ключу
     *
     * @param key     Ключ
     * @param context Контекст
     * @return Значение конфига
     */
    fun getBoolean(key: String, defaultVal: Boolean): Boolean {
        return settings?.getBoolean(key, defaultVal) ?: defaultVal
    }

    /**
     * Установка конфига
     *
     * @param key     Ключ
     * @param value   Значение
     * @param context Текущий контекст
     */
    fun setString(key: String, value: String) {
        settings?.edit { putString(key, value) }
    }

    /**
     * Установка числового конфига
     *
     * @param key     Ключ
     * @param value   Значение
     * @param context Текущий контекст
     */
    fun setBoolean(key: String, value: Boolean) {
        settings?.edit { putBoolean(key, value) }
    }

    /**
     * Установка числового конфига
     *
     * @param key     Ключ
     * @param value   Значение
     * @param context Текущий контекст
     */
    fun setInt(key: String, value: Int?) {
        settings?.edit { putInt(key, value ?: 0) }
    }

    fun setLong(key: String, value: Long) {
        settings?.edit { putLong(key, value) }
    }

    /**
     * Удаление ключа из конфига
     *
     * @param key     Ключ
     * @param context Контекст
     */
    fun remove(key: String) {
        settings?.edit { remove(key) }
    }
}
