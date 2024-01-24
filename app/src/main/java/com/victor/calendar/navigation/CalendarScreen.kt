package com.victor.calendar.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.victor.calendar.R
import com.victor.calendar.data.getMonth
import com.victor.calendar.ui.event.EventEditScreen
import com.victor.calendar.ui.event.EventEntryScreen
import com.victor.calendar.ui.event.EventViewModel
import com.victor.calendar.ui.home.HomeScreen
import com.victor.calendar.ui.home.HomeViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

enum class CalendarScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Entry(title = R.string.add_event),
    Edit(title = R.string.edit_event),
    Search(title = R.string.search_event)
}


@SuppressLint("RestrictedApi")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarApp(
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = CalendarScreen.valueOf(
        backStackEntry?.destination?.route ?: CalendarScreen.Start.name
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()

    val eventViewModel: EventViewModel = hiltViewModel<EventViewModel>()
    val homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.80F)) {
                Text("Drawer title", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Drawer Item") },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                CalendarAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    onDrawerButtonClicked = {
                        drawerScope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )

            }, floatingActionButton = {
                if (currentScreen == CalendarScreen.Start) {
                    FloatingActionButton(
                        onClick = { navController.navigate(CalendarScreen.Entry.name) }
                    ) {
                        Icon(Icons.Filled.Add, stringResource(R.string.add_event))
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = CalendarScreen.Start.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = CalendarScreen.Start.name) {
                    HomeScreen(homeViewModel = homeViewModel,
                        onEditButtonClicked = { navController.navigate(CalendarScreen.Edit.name) })
                }
                composable(route = CalendarScreen.Entry.name) {
                    EventEntryScreen(
                        eventViewModel = eventViewModel,
                        navigateBack = { navController.navigate(CalendarScreen.Start.name) }
                    )
                }
                composable(route = CalendarScreen.Edit.name) {
                    EventEditScreen()
                }
                composable(route = CalendarScreen.Search.name) {
                    EventEditScreen()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarAppBar(
    currentScreen: CalendarScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    onDrawerButtonClicked: () -> Unit
) {
    TopAppBar(
        actions = {
            if (currentScreen == CalendarScreen.Start) {
                Row(
                    modifier = modifier.padding(end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Now"
                    )
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Account"
                    )
                }
            }
        },
        title = {
            if (currentScreen == CalendarScreen.Start) {
                Row {
                    Text(
                        text = getMonth(
                            Calendar.getInstance()
                                .get(Calendar.MONTH),
                        ), style = MaterialTheme.typography.displayMedium
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            } else {
                Text(
                    text = stringResource(id = currentScreen.title),
                    style = MaterialTheme.typography.displayMedium
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            } else {
                IconButton(
                    onClick = onDrawerButtonClicked
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

