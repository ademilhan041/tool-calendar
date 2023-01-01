package com.ailhan.tool.calendar.util.json.impl

import com.ailhan.tool.calendar.util.json.Json
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class JsonJacksonImpl : Json {
    private val mapper = jacksonObjectMapper()

    init {
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true)
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.dateFormat = StdDateFormat()

        mapper.registerModule(JavaTimeModule())
    }

    override fun <T> fromJson(json: String, classOfT: Class<T>): T = mapper.readValue(json, classOfT)

    override fun toJson(obj: Any): String = mapper.writeValueAsString(obj)
}
