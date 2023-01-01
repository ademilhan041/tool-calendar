package com.ailhan.tool.calendar.holiday.calendarify

import com.ailhan.tool.calendar.util.loadStream
import java.util.*

/**
 * Available Properties
 *
 * calendarify.apiKey=<Api Key>
 * calendarify.country=<Country>
 */
class CalendarifyConfig {
    lateinit var apiKey: String
        private set
    lateinit var country: String
        private set

    private constructor()

    constructor(location: String) {
        loadConfig(Properties().loadStream(javaClass.getResourceAsStream(location)!!))
        validate()
    }

    constructor(props: Properties) {
        loadConfig(props)
        validate()
    }

    private fun loadConfig(prop: Properties) {
        apiKey = prop.getProperty("calendarify.apiKey")
        country = prop.getProperty("calendarify.country")
    }

    private fun validate() {
        if (apiKey.isEmpty()) throw IllegalArgumentException("CALENDARIFY_API_KEY_MISSING")
        if (country.isEmpty()) throw IllegalArgumentException("CALENDARIFY_COUNTRY_MISSING")
    }
}
