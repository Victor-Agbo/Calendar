package com.victor.calendar.dialogs

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.victor.calendar.util.extractMillsDay
import com.victor.calendar.util.extractMillsMonth
import com.victor.calendar.util.extractMillsYear

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(
    modifier: Modifier = Modifier,
    onConfirmRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    date: Calendar,
    datePickerState: DatePickerState
) {
    Column(modifier = modifier.fillMaxWidth()) {
        DatePickerDialog(
            modifier = modifier.weight(0.95F),
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = {
                    date.set(
                        extractMillsYear(datePickerState.selectedDateMillis),
                        extractMillsMonth(datePickerState.selectedDateMillis),
                        extractMillsDay(datePickerState.selectedDateMillis),
                        date.get(Calendar.HOUR_OF_DAY),
                        date.get(Calendar.MINUTE)
                    )
                    onConfirmRequest.invoke()
                }) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}
