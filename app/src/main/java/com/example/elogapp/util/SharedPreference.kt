package com.example.elogapp.util

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.runBlocking

/***
 * @author Vaibhav
 */


class PreferenceHelper {


    private fun getEditor(context: Context) : SharedPreferences{

        return context?.getSharedPreferences(AppConstants.PREF_DB, Context.MODE_PRIVATE)!!
    }

    fun save(context: Context, KEY_NAME: String, text: String) : Any {

        var result: Any

        runBlocking {

             val editor: SharedPreferences.Editor = getEditor(context).edit()

            editor.putString(KEY_NAME, text)

            result = editor.apply()
        }

        return result
    }

    fun save(context: Context, KEY_NAME: String, value: Int): Any {
        var result: Any
        runBlocking {
            val editor: SharedPreferences.Editor = getEditor(context).edit()

            editor.putInt(KEY_NAME, value)

            result = editor.apply()
        }
        return result
    }

    fun save(context: Context, KEY_NAME: String, status: Boolean): Any {
        var result: Any
        runBlocking {
            val editor: SharedPreferences.Editor = getEditor(context).edit()

            editor.putBoolean(KEY_NAME, status!!)

            result = editor.apply()
        }
        return result
    }

    fun getString(context: Context, KEY_NAME: String): String? {
        var result = ""
        runBlocking {
            result = getEditor(context).getString(KEY_NAME, "").toString()
        }
        return result
    }

    fun getInt(context: Context, KEY_NAME: String): Int {
        var result = 0
        runBlocking {
            result = getEditor(context).getInt(KEY_NAME, 0)
        }
        return result
    }

    fun getBoolean(context: Context, KEY_NAME: String, defaultValue: Boolean): Boolean {

        var result = false
        runBlocking {
            result = getEditor(context).getBoolean(KEY_NAME, defaultValue)
        }
        return result

    }

    fun clearSharedPreference(context: Context) {
        val editor: SharedPreferences.Editor = getEditor(context).edit()
        editor.clear()
        editor.commit()
    }

    fun removeValue(context: Context, KEY_NAME: String) {
        val editor: SharedPreferences.Editor = getEditor(context).edit()
        editor.remove(KEY_NAME)
        editor.commit()
    }
}
