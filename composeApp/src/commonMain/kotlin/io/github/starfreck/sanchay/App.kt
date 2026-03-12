package io.github.starfreck.sanchay

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.starfreck.sanchay.navigation.SanchayNavHost
import io.github.starfreck.sanchay.navigation.Screen
import io.github.starfreck.sanchay.theme.SanchayTheme

@Composable
fun App() {
    SanchayTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val topLevelDestinations = listOf(
            TopLevelDestination(
                route = Screen.TasksList,
                icon = Icons.Default.Checklist,
                label = "Tasks"
            ),
            TopLevelDestination(
                route = Screen.NotesList,
                icon = Icons.Default.Description,
                label = "Notes"
            ),
            TopLevelDestination(
                route = Screen.Calendar,
                icon = Icons.Default.DateRange,
                label = "Calendar"
            )
        )

        val navSuiteType = androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
            .calculateFromAdaptiveInfo(androidx.compose.material3.adaptive.currentWindowAdaptiveInfo())

        NavigationSuiteScaffold(
            layoutType = navSuiteType,
            navigationSuiteItems = {
                topLevelDestinations.forEach { destination ->
                    item(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(destination.route::class) } == true,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) }
                    )
                }
                
                // Settings - For Desktop/Rail, we'll put it in the nav suite.
                // For Mobile, it will be in the top bar of individual screens.
                // NavigationSuiteScaffold handles the switching between Rail/BottomBar.
                // We can use the layoutType to decide.
                if (navSuiteType != androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType.NavigationBar) {
                    item(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(Screen.Settings::class) } == true,
                        onClick = {
                            navController.navigate(Screen.Settings) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") }
                    )
                }
            }
        ) {
            SanchayNavHost(navController = navController)
        }
    }
}

private data class TopLevelDestination(
    val route: Screen,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
)