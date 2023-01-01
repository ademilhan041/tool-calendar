package com.ailhan.tool.calendar.holiday.dummy

import com.ailhan.tool.calendar.HolidayRepo
import java.time.LocalDate

class DummyHolidayDao : HolidayRepo {
    override fun isoHoliday(date: LocalDate): Boolean {
        return false
    }

    override fun islHoliday(date: LocalDate): Boolean {
        return false
    }
}