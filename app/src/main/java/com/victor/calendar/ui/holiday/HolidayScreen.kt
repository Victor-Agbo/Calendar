package com.victor.calendar.ui.holiday

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.victor.calendar.R
import com.victor.calendar.data.Holiday
import com.victor.calendar.util.countriesMap
import com.victor.calendar.util.countriesMapList
import java.util.Locale

@Composable
fun HolidayScreen(
    modifier: Modifier = Modifier,
    holidayUiState: HolidayUiState,
    holidayViewModel: HolidayViewModel
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {


        var mExpanded by remember { mutableStateOf(false) }

        var mSelectedText by remember { mutableStateOf(Locale.getDefault().displayCountry) }

        var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

        val icon = if (mExpanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        Column(Modifier.padding(20.dp)) {
            OutlinedTextField(
                value = mSelectedText,
                onValueChange = { mSelectedText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        mTextFieldSize = coordinates.size.toSize()
                    },
                label = { Text("Select country") },
                trailingIcon = {
                    Icon(icon, "contentDescription",
                        Modifier.clickable { mExpanded = !mExpanded })
                }
            )

            DropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
            ) {
                countriesMapList.forEach { label ->
                    DropdownMenuItem(onClick = {
                        mSelectedText = label
                        mExpanded = false
                    }, text = {
                        Text(text = label)
                    })
                }
            }
        }

        Button(onClick = { countriesMap[mSelectedText]?.let { holidayViewModel.getHolidays(it) } }) {
            Text(text = "Get Holidays")
        }
        when (holidayUiState) {
            is HolidayUiState.Error -> Text(text = "Error")
            is HolidayUiState.Loading -> Text(text = "Loading...")
            is HolidayUiState.Success -> {
                if (holidayUiState.holidayList.isNotEmpty())
                    HolidayList(holidays = holidayUiState.holidayList)
                else
                    Text(text = "No holidays today in $mSelectedText")
            }
        }

    }
}

@Composable
fun HolidayList(holidays: List<Holiday>) {
    LazyColumn {
        items(holidays) {
            HolidayItem(
                holiday = it,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )
        }
    }
}

@Composable
fun HolidayItem(
    holiday: Holiday,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                HolidayInformation(holiday = holiday)
                Spacer(Modifier.weight(1f))
                HolidayItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded },
                )
            }
            if (expanded) {
                HolidayDetails(
                    holiday, modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium)
                    )
                )
            }
        }
    }
}

@Composable
private fun HolidayItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}


@Composable
fun HolidayInformation(
    holiday: Holiday,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        Text(
            text = holiday.name ?: "No name",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
        )

        Text(
            modifier = modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)),
            text = "Country: ${holiday.location ?: "No location"}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun HolidayDetails(
    holiday: Holiday,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.about),
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            text = if (holiday.description.isNullOrEmpty()) "No description" else holiday.description,
            style = MaterialTheme.typography.bodyLarge
        )

    }
}
