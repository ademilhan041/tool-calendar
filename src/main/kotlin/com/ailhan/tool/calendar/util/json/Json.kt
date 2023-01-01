package com.ailhan.tool.calendar.util.json

import com.ailhan.tool.calendar.util.json.impl.JsonJacksonImpl

interface Json {
    fun <T> fromJson(json: String, classOfT: Class<T>): T

    fun toJson(obj: Any): String

    companion object {
        fun get(): Json = JsonJacksonImpl()
    }
}
