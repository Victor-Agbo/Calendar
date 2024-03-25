package com.victor.calendar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.victor.calendar.data.Event
import com.victor.calendar.ui.home.CalendarHour
import com.victor.calendar.ui.theme.CalendarTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AppTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
}


@RunWith(AndroidJUnit4::class)
class CalendarHourTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCalendarHourContent() {
        // Define a sample Event and dayStart time for testing
        val startTimeMillis = 100000000L
        val event = Event(1, "Sample Event", 100000000L, 100060000L)

        // Launch the CalendarHour composable with the sample data
        composeTestRule.setContent {
            CalendarTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CalendarHour(
                        event = event,
                        dayStart = startTimeMillis,
                        onCalendarHourClicked = {}
                    )
                }

            }

        }

        // Verify that the text content of the CalendarHour matches the event title
        composeTestRule
            .onNodeWithText("Sample Event", useUnmergedTree = true)
            .assertIsDisplayed()
    }
}
