package com.victor.calendar

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import junit.framework.TestCase
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class CalendarFormatterUnitTest : TestCase() {


    @Mock
    lateinit var mockCalendar: Calendar

    @Mock
    lateinit var mockDateFormat: SimpleDateFormat

    private val millis = 1670686200000L

    @Before
    override fun setUp() {
        mockCalendar = mock<Calendar> {
            on { timeInMillis } doReturn millis
        }
        mockDateFormat = mock<SimpleDateFormat> {
            on { format(mockCalendar) }
        }
    }
}