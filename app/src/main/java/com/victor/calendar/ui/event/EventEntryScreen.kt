package com.victor.calendar.ui.event

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.victor.calendar.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventEntryScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        var title by remember { mutableStateOf("") }
        val onSurfaceVariantColor = MaterialTheme.colorScheme.onSurfaceVariant

        TextField(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
            value = title,
            onValueChange = { title = it },
            label = { Text(text = "Add title") }
        )
        EditScreenDivider()
        var checked by remember { mutableStateOf(true) }
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

        AddEventOptionRow(
            modifier = modifier,
            content = "Wed, Dec 27, 2023"
        ) {
            if(!checked) {
                Text(text = "1:30 AM", style = it, color = onSurfaceVariantColor)
            }
        }
        AddEventOptionRow(
            modifier = modifier,
            content = "Thur, Dec 28, 2023"
        ) {
            if (!checked) {
                Text(text = "1:30 AM", style = it, color = onSurfaceVariantColor)
            }
        }

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
                    contentDescription = "globe",
                    tint = t
                )
            },
            content = "Add description"
        )
    }
}


@Composable
fun AddEventOptionRow(
    modifier: Modifier = Modifier,
    icon: @Composable ((modifier: Modifier, tint: Color) -> Unit)? = null,
    content: String,
    misc: @Composable ((textStyle: TextStyle) -> Unit)? = null
) {
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
        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = modifier.weight(1F))
        if (misc != null) {
            misc(MaterialTheme.typography.bodyMedium)
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