package com.example.elogapp.util

import androidx.room.TypeConverter
import com.example.elogapp.repository.responses.login.Permissions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.reflect.Type


class DataConverter {
    @TypeConverter
    fun fromList(value : List<String>) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<String>>(value)


    @TypeConverter
    fun fromPermissionList(permissions: List<Permissions?>?): String? {
        if (permissions == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Permissions?>?>() {}.type
        return gson.toJson(permissions, type)
    }

    @TypeConverter
    fun toPermissionsList(permissions: String?): List<Permissions>? {
        if (permissions == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Permissions?>?>() {}.type
        return gson.fromJson<List<Permissions>>(permissions, type)
    }
}