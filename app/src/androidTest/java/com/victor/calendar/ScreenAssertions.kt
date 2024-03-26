package com.victor.calendar

import androidx.navigation.NavController
import junit.framework.TestCase.assertEquals

fun NavController.assertCurrentRouteName(expectedRouteName: String) {
    assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
}

