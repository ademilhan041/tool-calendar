package com.ailhan.tool.calendar.holiday.file

import com.ailhan.tool.calendar.HolidayRepo
import java.io.IOException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class HolidayFileDao : HolidayRepo {
    private val islTable: List<Entry>

    init {
        this.islTable = loadIslamicTable()
    }

    override fun isoHoliday(date: LocalDate): Boolean {
        val dayOfWeek = date.dayOfWeek.value
        if (dayOfWeek == DayOfWeek.SATURDAY.value || dayOfWeek == DayOfWeek.SUNDAY.value) return true

        return isoHolidays.any { it.matches(date) }
    }

    override fun islHoliday(date: LocalDate): Boolean {
        val isl = convertToIslamic(date)
        return islHolidays.any { it.matches(isl) }
    }

    private fun loadIslamicTable(): List<Entry> {
        val map = arrayListOf<Entry>()
        try {
            var i = 0
            javaClass.getResourceAsStream("/tool/calendar/cal_to_islamic.txt")!!.bufferedReader()
                .useLines { lines -> lines.forEach { map.add(i++, Entry.parse(it)) } }

            val count = i
            i = 0
            while (i < count - 1) {
                val monthDays = ChronoUnit.DAYS.between(map[i].isoDate, map[i + 1].isoDate).toInt()
                map[i].rawDate.monthDays = monthDays
                i++
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw IllegalStateException("LOCAL_CALENDAR_COULD_NOT_INITIALIZED")
        }

        return map
    }

    private fun convertToIslamic(date: LocalDate): RawDate {
        val year = date.year
        if (year < 1900 || year > 2022) throw IllegalStateException("Calendar not support for <1900 and >2022!")

        var index = (year - 1900) * 12 * 365 / 354 - 5
        while (islTable[index].isoDate.isBefore(date)) index++

        if (islTable[index].isoDate.isEqual(date)) return islTable[index].rawDate

        index--
        val days = ChronoUnit.DAYS.between(islTable[index].isoDate, date).toInt()

        return islTable[index].rawDate.plusDays(days)
    }

    private class Holiday(val type: DateType, var date: RawDate, var startTime: LocalTime, var name: String) {
        fun matches(localDate: LocalDate): Boolean {
            return ((date.year == 0 || date.year == localDate.year) && date.month == localDate.monthValue && date.day == localDate.dayOfMonth)
        }

        fun matches(rawDate: RawDate): Boolean {
            return if (date.flag) (date.year == 0 || date.year == rawDate.year) && date.month == rawDate.month && date.day == rawDate.monthDays - rawDate.day + 1 else (date.year == 0 || date.year == rawDate.year) && date.month == rawDate.month && date.day == rawDate.day
        }
    }

    private class RawDate(val year: Int, val month: Int, val day: Int) {
        var flag: Boolean = false
        var monthDays: Int = 0

        fun plusDays(days: Int): RawDate {
            val rawDate = RawDate(year, month, day + days)
            rawDate.monthDays = monthDays
            return rawDate
        }

        override fun toString() = "$year - $month - $day"

        companion object {
            fun parse(s: String): RawDate {
                val year = Integer.parseInt(s.substring(0, 4))
                val month = Integer.parseInt(s.substring(5, 7))
                val day = Integer.parseInt(s.substring(8, 10))
                val rawDate = RawDate(year, month, day)
                rawDate.flag = s[7] == '+'
                return rawDate
            }
        }
    }

    private class Entry(val isoDate: LocalDate, val rawDate: RawDate) {
        companion object {
            fun parse(s: String): Entry {
                val f = s.split(",".toRegex())
                return Entry(LocalDate.parse(f[0]), RawDate.parse(f[1]))
            }
        }
    }

    enum class DateType {
        ISO, ISL
    }

    private val isoHolidays = arrayOf(
        Holiday(DateType.ISO, RawDate.parse("0000-01-01"), LocalTime.parse("00:00"), "YILBAŞI"),
        Holiday(
            DateType.ISO, RawDate.parse("0000-04-23"), LocalTime.parse("00:00"), "ULUSAL EGEMENLİK VE ÇOCUK BAYRAMI"
        ),
        Holiday(DateType.ISO, RawDate.parse("0000-05-01"), LocalTime.parse("00:00"), "EMEK VE DAYANIŞMA GÜNÜ"),
        Holiday(
            DateType.ISO,
            RawDate.parse("0000-05-19"),
            LocalTime.parse("00:00"),
            "ATATÜRK'Ü ANMA GENÇLİK VE SPOR BAYRAMI"
        ),
        Holiday(DateType.ISO, RawDate.parse("0000-08-30"), LocalTime.parse("00:00"), "ZAFER BAYRAMI"),
        Holiday(DateType.ISO, RawDate.parse("0000-10-28"), LocalTime.parse("12:00"), "CUMHURİYET BAYRAMI ÖNCESİ"),
        Holiday(DateType.ISO, RawDate.parse("0000-10-29"), LocalTime.parse("00:00"), "CUMHURİYET BAYRAMI")
    )

    private val islHolidays = arrayOf(
        Holiday(DateType.ISL, RawDate.parse("0000-09+01"), LocalTime.parse("12:00"), "RAMAZAN BAYRAMI AREFESİ"),
        Holiday(DateType.ISL, RawDate.parse("0000-10-01"), LocalTime.parse("00:00"), "RAMAZAN BAYRAMI 1. GÜN"),
        Holiday(DateType.ISL, RawDate.parse("0000-10-02"), LocalTime.parse("00:00"), "RAMAZAN BAYRAMI 2. GÜN"),
        Holiday(DateType.ISL, RawDate.parse("0000-10-03"), LocalTime.parse("00:00"), "RAMAZAN BAYRAMI 3. GÜN"),
        Holiday(DateType.ISL, RawDate.parse("0000-12-09"), LocalTime.parse("12:00"), "KURBAN BAYRAMI AREFESİ"),
        Holiday(DateType.ISL, RawDate.parse("0000-12-10"), LocalTime.parse("00:00"), "KURBAN BAYRAMI 1. GÜN"),
        Holiday(DateType.ISL, RawDate.parse("0000-12-11"), LocalTime.parse("00:00"), "KURBAN BAYRAMI 2. GÜN"),
        Holiday(DateType.ISL, RawDate.parse("0000-12-12"), LocalTime.parse("00:00"), "KURBAN BAYRAMI 3. GÜN"),
        Holiday(DateType.ISL, RawDate.parse("0000-12-13"), LocalTime.parse("00:00"), "KURBAN BAYRAMI 4. GÜN")
    )
}
