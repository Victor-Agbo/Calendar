package com.victor.calendar.ui.event

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.victor.calendar.R
import com.victor.calendar.dialogs.DateDialog
import com.victor.calendar.dialogs.TimeDialog
import com.victor.calendar.util.convertMillisToFormattedDate
import com.victor.calendar.util.convertMillisToFormattedTime
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventEntryScreen(
    modifier: Modifier = Modifier,
    eventViewModel: EventViewModel,
    navigateBack: () -> Unit
) {
    val eventUiState by eventViewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier
            .padding(8.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        item {
            val onSurfaceVariantColor = MaterialTheme.colorScheme.onSurfaceVariant
            val surfaceColor: Color = MaterialTheme.colorScheme.surface

            TextField(
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = surfaceColor),
                label = { Text(text = "Add title") },
                onValueChange = { eventViewModel.updateTitle(it) },
                singleLine = true,
                value = eventUiState.eventDetails.title,
            )
            EditScreenDivider()
            var checked by remember { mutableStateOf(false) }
            AddEventOptionRow(
                modifier = modifier,
                icon = { m, t ->
                    Icon(
                        imageVector = Icons.Filled.Face,
                        modifier = m,
                        contentDescription = "Face",
                        tint = t
                    )
                },
                content = "All-day"
            ) {
                Switch(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }
            val calendar = Calendar.getInstance()

            val startDate by remember {
                mutableStateOf(Calendar.getInstance())
            }
            val endDate by remember {
                mutableStateOf(Calendar.getInstance())
            }

            var showTimeDialog by remember { mutableStateOf(false) }

            var showStartTimeDialog by remember {
                mutableStateOf(false)
            }
            var showEndTimeDialog by remember {
                mutableStateOf(false)
            }

            var timePickerState by remember {
                mutableStateOf(TimePickerState(0, 0, false))
            }

            fun setTimeAndDismiss(dialogType: String) {
                val targetDate: Calendar = if (dialogType == "start") {
                    startDate
                } else {
                    endDate
                }

                targetDate.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                targetDate.set(Calendar.MINUTE, timePickerState.minute)

                if (dialogType == "start") {
                    eventViewModel.updateStart(startDate.timeInMillis)
                } else {
                    eventViewModel.updateEnd(endDate.timeInMillis)
                }

                showTimeDialog = false
                showEndTimeDialog = false
                showStartTimeDialog = false

            }

            if (showTimeDialog) {
                TimeDialog(
                    onDismissRequest = { setTimeAndDismiss(if (showStartTimeDialog) "start" else "end") },
                    timePickerState = timePickerState,
                )
            }

            val datePickerState =
                rememberDatePickerState(initialSelectedDateMillis = calendar.timeInMillis)

            var showStartDateDialog by remember {
                mutableStateOf(false)
            }
            var showEndDateDialog by remember {
                mutableStateOf(false)
            }


            if (showStartDateDialog || showEndDateDialog) {
                DateDialog(
                    onConfirmRequest = {
                        if (showStartDateDialog) {
                            eventViewModel.updateStart(startDate.timeInMillis)
                            showStartDateDialog = false
                        } else {
                            eventViewModel.updateEnd(endDate.timeInMillis)
                            showEndDateDialog = false
                        }

                    },
                    onDismissRequest = {
                        showStartDateDialog = false
                        showEndDateDialog = false
                    },
                    date = if (showStartDateDialog) startDate else endDate,
                    datePickerState = datePickerState
                )
            }
            AddEventOptionRow(
                modifier = modifier,
                mainCompose = {
                    Text(
                        modifier = modifier
                            .clickable {
                                datePickerState.setSelection(startDate.timeInMillis)
                                showStartDateDialog = true
                            },
                        color = onSurfaceVariantColor,
                        style = it,
                        text = convertMillisToFormattedDate(startDate.timeInMillis)
                    )
                },
                misc = {
                    if (!checked) {
                        Text(
                            modifier = modifier
                                .clickable {
                                    timePickerState = TimePickerState(
                                        initialHour = startDate.get(Calendar.HOUR_OF_DAY),
                                        initialMinute = startDate.get(Calendar.MINUTE),
                                        is24Hour = false
                                    )
                                    showTimeDialog = true
                                    showStartTimeDialog = true
                                }
                                .padding(4.dp),
                            text = convertMillisToFormattedTime(startDate.timeInMillis),
                            style = it,
                            color = onSurfaceVariantColor
                        )
                    }
                })

            AddEventOptionRow(
                modifier = modifier,
                mainCompose = {
                    Text(
                        modifier = modifier
                            .clickable {
                                datePickerState.setSelection(endDate.timeInMillis)
                                showEndDateDialog = true
                            },
                        color = onSurfaceVariantColor,
                        style = it,
                        text = convertMillisToFormattedDate(endDate.timeInMillis)
                    )
                },
                misc = {
                    if (!checked) {
                        Text(
                            modifier = modifier
                                .clickable {
                                    timePickerState = TimePickerState(
                                        initialHour = endDate.get(Calendar.HOUR_OF_DAY),
                                        initialMinute = endDate.get(Calendar.MINUTE),
                                        is24Hour = false
                                    )
                                    showTimeDialog = true
                                    showEndTimeDialog = true
                                }

                                .padding(4.dp),
                            text = convertMillisToFormattedTime(endDate.timeInMillis),
                            style = it,
                            color = onSurfaceVariantColor
                        )
                    }
                })

            AddEventOptionRow(
                modifier = modifier,
                icon = { m, t ->
                    Icon(
                        modifier = m,
                        painter = painterResource(id = R.drawable.public_24px),
                        contentDescription = "globe",
                        tint = t
                    )
                },
                content = "Indian Standard Time"
            )
            AddEventOptionRow(
                modifier = modifier,
                icon = { m, t ->
                    Icon(
                        modifier = m,
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "globe",
                        tint = t
                    )
                },
                content = "Does not repeat"
            )
            EditScreenDivider()

            AddEventOptionRow(
                modifier = modifier,
                content = "Add notification"
            )
            EditScreenDivider()

            AddEventOptionRow(
                modifier = modifier,
                icon = { m, t ->
                    Icon(
                        modifier = m,
                        imageVector = Icons.Filled.List,
                        contentDescription = "List",
                        tint = t
                    )
                },
                mainCompose = {
                    TextField(
                        modifier = modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = surfaceColor,
                            focusedIndicatorColor = surfaceColor,
                            unfocusedIndicatorColor = surfaceColor
                        ),
                        value = eventUiState.eventDetails.description,
                        onValueChange = { eventViewModel.updateDescription(it) },
                        placeholder = {
                            Text(
                                text = "Add description",
                            )
                        }
                    )
                }
            )
            val coroutineScope = rememberCoroutineScope()
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { eventViewModel.resetEventDetails() },
                    enabled = eventUiState.eventDetails != EventDetails()
                ) {
                    Text(text = "Clear")
                }
                Button(onClick = {
                    coroutineScope.launch {
                        eventViewModel.saveEvent()
                        navigateBack.invoke()
                        eventViewModel.resetEventDetails()
                    }

                }, enabled = eventUiState.isEventValid) {
                    Text(text = "Save")
                }
            }

//            Text(
//                modifier = modifier.background(Color.Red),
//                text = eventViewModel.ll.collectAsState().value.title
//            )

        }
    }
}


@Composable
fun AddEventOptionRow(
    modifier: Modifier = Modifier,
    icon: @Composable ((modifier: Modifier, tint: Color) -> Unit)? = null,
    content: String? = null,
    mainCompose: @Composable ((textStyle: TextStyle) -> Unit)? = null,
    misc: @Composable ((textStyle: TextStyle) -> Unit)? = null
) {
    val bodyMedium = MaterialTheme.typography.bodyMedium
    Row(
        modifier = modifier
            .fillMaxWidth(1F)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (icon != null) {
            icon(modifier.width(45.dp), MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            Spacer(modifier = modifier.width(48.dp))
        }
        if (content != null) {
            Text(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = content,
                style = bodyMedium
            )
        } else {
            if (mainCompose != null) {
                mainCompose(bodyMedium)
            }
        }
        Spacer(modifier = modifier.weight(1F))
        if (misc != null) {
            misc(bodyMedium)
        }

    }
}

@Composable
fun EditScreenDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    )
}


