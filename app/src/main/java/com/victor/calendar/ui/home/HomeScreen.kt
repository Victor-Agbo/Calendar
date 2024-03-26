package com.victor.calendar.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.victor.calendar.data.Event
import com.victor.calendar.ui.event.EventViewModel
import com.victor.calendar.util.MILLIS_IN_DAY
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    eventViewModel: EventViewModel,
    homeViewModel: HomeViewModel,
    onCalendarHourClicked: () -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = { 3 }, initialPage = 1
    )

    LaunchedEffect(pagerState.currentPage) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page == 2) {
                homeViewModel.updateStartOfWeek(true)
            } else if (page == 0) {
                homeViewModel.updateStartOfWeek(false)
            }
            homeViewModel.observeEvents().run {
                delay(10)
                pagerState.scrollToPage(1)
            }

        }
    }

    val homeUiState by homeViewModel.homeUiState.collectAsState()

    HorizontalPager(state = pagerState) {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxWidth(),
            columns = GridCells.Fixed(8)
        ) {

            item {
                Column(
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
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

            items(7) { index ->
                val dayStart = homeUiState.startOfWeek + (MILLIS_IN_DAY * index)
                CalendarDay(
                    eventList = homeUiState.eventList[index],
                    eventViewModel = eventViewModel,
                    dayStart = dayStart,
                    onCalendarHourClicked = onCalendarHourClicked
                )
            }
        }
    }
}

fun calculateTopPadding(
    dayStart: Long,
    eventStart: Long
): Float {
    return ((((eventStart - dayStart).toFloat()) / MILLIS_IN_DAY.toFloat()) * 1920)
}

fun calculateHourHeight(
    startTime: Long,
    endTime: Long
): Float {
    return (((endTime - startTime) / MILLIS_IN_DAY.toFloat()) * 1920) - 2
}

@Composable
fun CalendarDay(
    modifier: Modifier = Modifier,
    eventViewModel: EventViewModel,
    eventList: MutableList<Event>,
    dayStart: Long,
    onCalendarHourClicked: () -> Unit
) {
    Row(modifier = modifier) {
        VerticalLine(height = 1920, width = 0.25)
        Box {
            for (event in eventList) {
                CalendarHour(
                    dayStart = dayStart,
                    event = event,
                    onCalendarHourClicked = {
                        eventViewModel.getEvent(event.id)
                        onCalendarHourClicked.invoke()
                    }
                )
            }
        }
    }
}

@Composable
fun CalendarHour(
    modifier: Modifier = Modifier,
    event: Event,
    dayStart: Long,
    onCalendarHourClicked: () -> Unit
) {
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
            .clickable { onCalendarHourClicked.invoke() }
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
            .padding(top = 66.dp),
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