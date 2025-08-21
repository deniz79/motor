package com.motorlar.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.motorlar.app.R
import com.motorlar.app.ui.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotorlarNavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                listOf(
                    Screen.Home,
                    Screen.Map,
                    Screen.Record,
                    Screen.Team,
                    Screen.Profile
                ).forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    Screen.Home -> Icons.Default.Home
                                    Screen.Map -> Icons.Default.Map
                                    Screen.Record -> Icons.Default.RadioButtonChecked
                                    Screen.Team -> Icons.Default.Group
                                    Screen.Profile -> Icons.Default.Person
                                    else -> Icons.Default.Home
                                },
                                contentDescription = stringResource(screen.resourceId)
                            )
                        },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Auth.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Auth.route) {
                AuthScreen(
                    onLoginSuccess = { username ->
                        viewModel.loginUser(username)
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(viewModel = viewModel)
            }
            composable(Screen.Map.route) {
                MapScreen(viewModel = viewModel)
            }
            composable(Screen.Record.route) {
                RecordScreen(viewModel = viewModel)
            }
            composable(Screen.Team.route) {
                TeamScreen(viewModel = viewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(viewModel = viewModel)
            }
            composable(Screen.RouteDetail.route + "/{routeId}") { backStackEntry ->
                val routeId = backStackEntry.arguments?.getString("routeId") ?: ""
                RouteDetailScreen(routeId = routeId)
            }
        }
    }
}
