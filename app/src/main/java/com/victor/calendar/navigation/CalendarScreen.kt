package com.victor.calendar.navigation

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.android.gms.auth.api.identity.Identity
import com.victor.calendar.R
import com.victor.calendar.data.getMonth
import com.victor.calendar.sign_in.GoogleAuthUiClient
import com.victor.calendar.sign_in.SignInViewModel
import com.victor.calendar.sign_in.UserData
import com.victor.calendar.ui.event.EventEditScreen
import com.victor.calendar.ui.event.EventEntryScreen
import com.victor.calendar.ui.event.EventViewModel
import com.victor.calendar.ui.holiday.HolidayScreen
import com.victor.calendar.ui.holiday.HolidayViewModel
import com.victor.calendar.ui.home.HomeScreen
import com.victor.calendar.ui.home.HomeViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

enum class CalendarScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Entry(title = R.string.add_event),
    Edit(title = R.string.edit_event),
    Holiday(title = R.string.holidays),
    Search(title = R.string.search_event)
}


@SuppressLint("RestrictedApi")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarApp(
    navController: NavHostController = rememberNavController(),
    applicationContext: Context,
    lifecycleScope: LifecycleCoroutineScope
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CalendarScreen.valueOf(
        backStackEntry?.destination?.route ?: CalendarScreen.Start.name
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()

    val eventViewModel: EventViewModel = hiltViewModel<EventViewModel>()
    val holidayViewModel: HolidayViewModel = hiltViewModel<HolidayViewModel>()
    val homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>()


    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    val currentMonth by remember {
        mutableStateOf(Calendar.getInstance())
    }
    currentMonth.timeInMillis = homeViewModel.startOfWeek

    var new by remember { mutableStateOf(true) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.80F)) {
                Text("Calendar", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = stringResource(R.string.holidays)) },
                    selected = false,
                    onClick = {
                        drawerScope.launch {
                            drawerState.close()
                        }
                        navController.navigate(CalendarScreen.Holiday.name)
                    }
                )
            }
        },
    ) {
        val signInViewModel = viewModel<SignInViewModel>()
        val state by signInViewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = state.isSignInSuccessful) {
            if (googleAuthUiClient.getSignedInUser() != null) {
                Log.d("SignIn", "SignIn Google User is valid")
            }
        }
        val context = LocalContext.current
        LaunchedEffect(key1 = state.signInError) {
            state.signInError?.let { error ->
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_LONG
                ).show()
                Log.d("SignIn", "SignIn Error $error")
            }

        }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                if (result.resultCode == RESULT_OK) {
                    lifecycleScope.launch {
                        val signInResult = googleAuthUiClient.signInWithIntent(
                            intent = result.data ?: return@launch
                        )
                        signInViewModel.onSignInResult(signInResult)
                    }
                }
            }
        )

        LaunchedEffect(key1 = state.isSignInSuccessful) {
            Log.d("SignIn", "SignIn User Successful")
            if (state.isSignInSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "Sign in successful",
                    Toast.LENGTH_LONG
                ).show()

                signInViewModel.resetState()
            }
        }

        Scaffold(
            topBar = {
                CalendarAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = {
                        new = true
                        eventViewModel.resetEventDetails()
                        navController.navigateUp()
                    },
                    currentMonth = getMonth(currentMonth.get(Calendar.MONTH)),
                    onDrawerButtonClicked = {
                        drawerScope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    signInClicked = {
                        lifecycleScope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    },
                    signOutClicked = {
                        lifecycleScope.launch {
                            googleAuthUiClient.signOut()
                            Toast.makeText(
                                applicationContext,
                                "Signed out",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        Log.d("SignIn", "SignOut Successful")
                    },
                    userData = googleAuthUiClient.getSignedInUser()
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


                    HomeScreen(
                        eventViewModel = eventViewModel,
                        homeViewModel = homeViewModel,
                        onCalendarHourClicked = {
                            new = false
                            navController.navigate(CalendarScreen.Entry.name)
                        })
                }
                composable(route = CalendarScreen.Entry.name) {
                    EventEntryScreen(
                        eventViewModel = eventViewModel,
                        navigateBack = { navController.navigate(CalendarScreen.Start.name) },
                        new = new
                    )
                }
                composable(route = CalendarScreen.Edit.name) {
                    EventEditScreen()
                }
                composable(route = CalendarScreen.Search.name) {
                    EventEditScreen()
                }
                composable(route = CalendarScreen.Holiday.name) {
                    HolidayScreen(
                        holidayUiState = holidayViewModel.holidayUiState,
                        holidayViewModel = holidayViewModel
                    )
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
    currentMonth: String,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    onDrawerButtonClicked: () -> Unit,
    signInClicked: () -> Unit,
    signOutClicked: () -> Unit,
    userData: UserData?
) {
    TopAppBar(
        actions = {
            if (currentScreen == CalendarScreen.Start) {
                Row(
                    modifier = modifier.padding(end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    if (userData?.profilePictureUrl != null) {

                        AsyncImage(
                            model = userData.profilePictureUrl,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .shadow(elevation = 1.dp)
                                .clickable {
                                    signOutClicked.invoke()
                                },
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        Icon(
                            modifier = modifier.clickable {
                                signInClicked.invoke()
                            },
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Account"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        },
        title = {
            if (currentScreen == CalendarScreen.Start) {
                Row {
                    Text(
                        text = currentMonth, style = MaterialTheme.typography.displayMedium
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
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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

