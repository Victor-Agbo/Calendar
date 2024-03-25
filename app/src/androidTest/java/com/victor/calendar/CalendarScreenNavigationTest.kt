package com.victor.calendar

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.victor.calendar.navigation.CalendarApp
import com.victor.calendar.navigation.CalendarScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//class CalendarScreenNavigationTest {
//
//
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private lateinit var navController: TestNavHostController
//
//    @Before
//    fun setupCupcakeNavHost() {
//        composeTestRule.setContent {
//            navController = TestNavHostController(LocalContext.current).apply {
//                navigatorProvider.addNavigator(ComposeNavigator())
//            }
//            CalendarApp(navController = navController, applicationContext = Context, lifecycleScope = l)
//        }
//    }
//
//    @Test
//    fun calendarNavHost_verifyStartDestination() {
//        navController.assertCurrentRouteName(CalendarScreen.Start.name)
//    }
//}