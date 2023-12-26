package com.victor.calendar.ui.event

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.victor.calendar.DatePickerFragment

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventEntryScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        var title by remember { mutableStateOf("") }
        TextField(
            modifier = modifier.fillMaxWidth(),
            value = title,
            onValueChange = { title = it },
            label = { Text(text = "Add title") }
        )
        AddEventOptionRow(
            modifier = modifier,
            icon = Icons.Filled.Face, content = "All-day"
        ) {
            Switch(
                checked = false,
                onCheckedChange = {}
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth(1F),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = modifier.width(32.dp))
        }

    }
}

@Composable
fun AddEventOptionRow(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    content: String,
    misc: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(1F),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        if (icon != null) {
            Icon(
                modifier = Modifier.padding(16.dp),
                imageVector = icon,
                contentDescription = "Time"
            )
        } else {
            Spacer(modifier = modifier.width(32.dp))
        }
        Text(text = content)
        Spacer(modifier = modifier.weight(1F))
        misc()
    }
}

//@Preview
//@Composable
//fun PrevOptionRow(modifier: Modifier = Modifier) {
//    AddEventOptionRow(
//        modifier = modifier,
//        icon = Icons.Filled.Face, content = "All-day"
//    ) {
//        Switch(
//            checked = false,
//            onCheckedChange = {}
//        )
//    }
//}