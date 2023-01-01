package com.ailhan.tool.calendar.holiday.calendarify

import com.ailhan.tool.calendar.util.json.Json
import com.ailhan.tool.calendar.HolidayRepo
import com.ailhan.tool.calendar.util.toLocalDate
import org.http4k.client.ApacheClient
import org.http4k.core.Method
import org.http4k.core.Request
import java.io.File
import java.nio.charset.StandardCharsets
import java.time.LocalDate

class HolidayCalendarifyDao(private val config: CalendarifyConfig) : HolidayRepo {
    private val fileLocation = "_temp/calendar/calendarify"

    private val client = ApacheClient()

    init {
        File(fileLocation).mkdirs()
    }

    override fun isoHoliday(date: LocalDate): Boolean {
        val dtoList = getDto(date).response.holidays
        val isoHolidays = dtoList.filter { !it.name.contains("ramadan", true) && !it.name.contains("feast", true) }

        return isoHolidays.map { it.date.iso.toLocalDate() }.any { it == date }
    }

    override fun islHoliday(date: LocalDate): Boolean {
        val dtoList = getDto(date).response.holidays
        val islHolidays = dtoList.filter { it.name.contains("ramadan", true) || it.name.contains("feast", true) }

        return islHolidays.map { it.date.iso.toLocalDate() }.any { it == date }
    }

    private fun getDto(date: LocalDate): CalendarifyDto {
        val file = File("$fileLocation/${date.year}.json")
        return if (!file.exists()) {
            val dto = callCalendarify(date.year)
            file.writeText(Json.get().toJson(dto))
            dto
        } else {
            Json.get().fromJson(file.readText(), CalendarifyDto::class.java)
        }
    }

    private fun callCalendarify(year: Int): CalendarifyDto {
        try {
            val request = Request(Method.GET, "https://calendarific.com/api/v2/holidays")
                .query("api_key", config.apiKey)
                .query("country", config.country)
                .query("year", year.toString())
                .query("type", "national")

            val bytebuffer = client(request).body.payload
            val resp = String(bytebuffer.array(), StandardCharsets.UTF_8)

            return Json.get().fromJson(resp, CalendarifyDto::class.java)
        } catch (e: Exception) {
            throw RuntimeException("CALENDAR_CALENDARIFY_CALL_FAILED", e)
        }
    }
}
