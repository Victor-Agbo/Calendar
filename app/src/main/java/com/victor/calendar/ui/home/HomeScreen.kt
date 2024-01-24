package com.victor.calendar.ui.home

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.victor.calendar.data.Event
import com.victor.calendar.util.MILLIS_IN_DAY

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    onEditButtonClicked: () -> Unit
) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth(),
        columns = GridCells.Fixed(8)
    ) {

        item {
            Column(
                modifier = modifier
                    .width(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in 1..11) {
                    TimeLabel(time = i, dayPart = "AM")

                }
                TimeLabel(time = 12, dayPart = "PM")
                for (i in 1..11) {
                    TimeLabel(time = i, dayPart = "PM")

                }
                Spacer(modifier = modifier.height(64.dp))
            }
        }

        val calendar = Calendar.getInstance()
        //calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val originalList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        val condition =
            { element: Int -> element % 2 == 0 } // Example condition, you can replace it with your own

        val groupedLists = originalList.groupBy { if (condition(it)) "even" else "odd" }

        val result = groupedLists.values.toList()

        println(result)
        var sortedEventList = mapOf<Int, List<Event>>(
            0 to listOf<Event>(),
            1 to listOf<Event>(),
            2 to listOf<Event>(),
            3 to listOf<Event>(),
            4 to listOf<Event>(),
            5 to listOf<Event>(),
            6 to listOf<Event>(),
        )

        items(7) {
            CalendarDay(eventList = homeUiState.eventList, dayStart = calendar.timeInMillis)
        }

    }
}

fun calculateTopPadding(
    dayStart: Long,
    eventStart: Long
): Float {
    return (((eventStart - dayStart) / MILLIS_IN_DAY.toFloat()) * 1920)
}

fun calculateHourHeight(
    startTime: Long,
    endTime: Long
): Float {
    return (((endTime - startTime) / MILLIS_IN_DAY.toFloat()) * 1920) - 2
}

@Composable
fun CalendarDay(modifier: Modifier = Modifier, eventList: List<Event>, dayStart: Long) {
    Row(modifier = modifier) {
        VerticalLine(height = 1920, width = 0.25)
        Box {
            for (event in eventList) {
                CalendarHour(
                    dayStart = dayStart,
                    event = event
                )
            }

        }
    }
}

@Composable
fun CalendarHour(modifier: Modifier = Modifier, event: Event, dayStart: Long) {
    Box(
        modifier = modifier
            .padding(
                top = calculateTopPadding(
                    eventStart = event.start,
                    dayStart = dayStart
                ).dp, end = 2.dp, bottom = 1.dp
            )
            .shadow(elevation = 1.2.dp)
            .clip(MaterialTheme.shapes.small)
            .height(calculateHourHeight(event.start, event.end).dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(4.dp)


    ) {
        Text(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            text = event.title,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun TimeLabel(modifier: Modifier = Modifier, time: Int, dayPart: String) {
    Text(
        modifier = modifier
            .padding(top = 64.dp),
        text = "$time $dayPart",
        style = MaterialTheme.typography.labelMedium
    )

}

@Composable
fun VerticalLine(height: Int, width: Double) {
    Box(
        modifier = Modifier
            .height(height.dp)
            .width(width.dp)
            .background(color = MaterialTheme.colorScheme.outline)
    )
}