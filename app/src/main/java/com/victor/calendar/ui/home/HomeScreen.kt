package com.victor.calendar.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onEditButtonClicked: () -> Unit
) {
    val homeScroll = rememberScrollState()
    val heightState = remember { mutableStateOf(0.dp) }

    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Column(modifier = modifier
            .verticalScroll(homeScroll)
            .onSizeChanged { heightState.value = it.height.dp }) {
            for (i in 1..12) {
                TimeLabel(time = i, dayPart = "AM")
            }
            for (i in 1..11) {
                TimeLabel(time = i, dayPart = "PM")
            }
            Spacer(modifier = modifier.height(64.dp))
        }
        VerticalLine(width = 0.5.dp)
        Row(
            modifier = modifier
                .verticalScroll(homeScroll)
                .height(heightState.value)
        ) {
            for (i in 1..7) {
                CalendarDay(
                    modifier = modifier
                        .fillMaxHeight()
                        .weight(1F)
                )
                VerticalLine(width = 0.25.dp)
            }
        }
    }
}

@Composable
fun CalendarDay(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        CalendarHour()
    }
}

@Composable
fun CalendarHour(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(top = 1.dp, end = 2.dp, bottom = 1.dp)
            .fillMaxWidth(1F)
            .height(64.dp)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(4.dp)

    ) {
        Text(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            text = "Hello",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun TimeLabel(modifier: Modifier = Modifier, time: Int, dayPart: String) {

    Column {
        Spacer(modifier = modifier.height(64.dp))
        Text(
            modifier = modifier
                .padding(start = 8.dp, end = 8.dp),
            text = "$time $dayPart",
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun VerticalLine(width: Dp) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(width)
            .background(color = MaterialTheme.colorScheme.outline)
    )
}