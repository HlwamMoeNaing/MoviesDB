package com.hmn.moviesdb.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.utils.noRippleClickable
import com.hmn.moviesdb.R
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val navController = rememberNavController()

    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val topBarState = rememberSaveable { (mutableStateOf(true)) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("RoutT", "AppScaffold: $currentRoute")

    when {

        currentRoute?.startsWith(Routes.SplashScreen.name) == true -> {
            bottomBarState.value = false
            topBarState.value = false
        }

        currentRoute?.startsWith(Routes.LoginScreen.name) == true -> {
            bottomBarState.value = false
            topBarState.value = false
        }

        currentRoute?.contains(Routes.DetailScreen.name) == true -> {
            bottomBarState.value = false
            topBarState.value = true
        }

        currentRoute?.startsWith(Routes.SearchScreen.name) == true -> {
            bottomBarState.value = false
            topBarState.value = false
        }



        else -> {
            bottomBarState.value = true
            topBarState.value = true
        }
    }

    Scaffold(
        topBar = {
//            TopAppBar(
//                goToSearchScreen = {
//                    navController.navigate(Graph.OTHER_SESSION)
//                },
//                topBarState, scrollBehavior
//            )
        },

        bottomBar = {
            BottomAppBar(bottomBarState, navController)
        }
    ) { innerPadding ->
        val test = innerPadding
        /*This nav controller must be pass in here. Don't change it. Please follow this way*/
        BottomBarNavigation(navController,innerPadding)
    }
}

@Composable
fun BottomAppBar(
    bottomBarState: MutableState<Boolean>,
    navController: NavController
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val navigationBarItems = remember { NavigationBarItems.entries.toTypedArray() }
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            AnimatedNavigationBar(
                //modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                selectedIndex = selectedIndex,
                //cornerRadius = shapeCornerRadius(cornerRadius = 18.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(300)),
                barColor = MaterialTheme.colorScheme.secondary,
                ballColor = MaterialTheme.colorScheme.primary,
            ) {
                navigationBarItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .noRippleClickable {
                                selectedIndex = item.ordinal
                                navController.navigate(item.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(26.dp),
                            imageVector = item.icon,
                            contentDescription = item.name,
                            tint = if (selectedIndex == item.ordinal) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.inversePrimary
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    goToSearchScreen: () -> Unit,
    topBarState: MutableState<Boolean>,
    scrollBehavior: TopAppBarScrollBehavior
) {

    AnimatedVisibility(
        visible = topBarState.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {
            val primaryColor = MaterialTheme.colorScheme.primary
            val primaryColorHex =
                Integer.toHexString(primaryColor.toArgb()).toUpperCase(Locale.ROOT)
            androidx.compose.material3.TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(),
                title = {
                    Text(
                        modifier = Modifier.padding(top = 15.dp, bottom = 10.dp),
                        text = stringResource(id = R.string.home_toolbar_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },

                scrollBehavior = scrollBehavior
            )
        }
    )


}