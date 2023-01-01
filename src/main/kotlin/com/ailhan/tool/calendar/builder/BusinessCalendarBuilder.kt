package com.ailhan.tool.calendar.builder

import com.ailhan.tool.calendar.BusinessCalendar
import com.ailhan.tool.calendar.BusinessCalendarImpl
import com.ailhan.tool.calendar.holiday.calendarify.CalendarifyConfig
import com.ailhan.tool.calendar.holiday.calendarify.HolidayCalendarifyDao
import com.ailhan.tool.calendar.holiday.dummy.DummyHolidayDao
import com.ailhan.tool.calendar.holiday.file.HolidayFileDao

class BusinessCalendarBuilder {
    private lateinit var _holidayRepoType: HolidayRepoType

    private lateinit var _calendarifyConfig: CalendarifyConfig

    fun dummy(): BusinessCalendarBuilder {
        this._holidayRepoType = HolidayRepoType.DUMMY
        return this
    }

    fun file(): BusinessCalendarBuilder {
        this._holidayRepoType = HolidayRepoType.FILE
        return this
    }

    fun calendarify(config: CalendarifyConfig): BusinessCalendarBuilder {
        this._holidayRepoType = HolidayRepoType.CALENDARIFY
        this._calendarifyConfig = config
        return this
    }

    fun build(): BusinessCalendar {
        if (!::_holidayRepoType.isInitialized) throw IllegalStateException("CALENDAR_MODULE_BUILD_FAILED")
        if (_holidayRepoType == HolidayRepoType.CALENDARIFY && !::_calendarifyConfig.isInitialized) throw IllegalStateException(
            "CALENDAR_MODULE_BUILD_FAILED"
        )

        val repo = when (_holidayRepoType) {
            HolidayRepoType.DUMMY -> DummyHolidayDao()
            HolidayRepoType.FILE -> HolidayFileDao()
            HolidayRepoType.CALENDARIFY -> HolidayCalendarifyDao(_calendarifyConfig)
        }

        return BusinessCalendarImpl(repo)
    }

    private enum class HolidayRepoType { DUMMY, FILE, CALENDARIFY }
}
