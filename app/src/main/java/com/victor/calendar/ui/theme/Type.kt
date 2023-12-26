package com.victor.calendar.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.victor.calendar.R

// Set of Material typography styles to start with

val robotoFamily = FontFamily(
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp
    ),
    displayMedium = TextStyle(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Light,
        fontSize = 10.sp
    )
)