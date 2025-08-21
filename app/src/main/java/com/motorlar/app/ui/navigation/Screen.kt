package com.motorlar.app.ui.navigation

import com.motorlar.app.R

sealed class Screen(val route: String, val resourceId: Int) {
    object Home : Screen("home", R.string.home)
    object Map : Screen("map", R.string.map)
    object Record : Screen("record", R.string.record)
    object Team : Screen("team", R.string.team)
    object Profile : Screen("profile", R.string.profile)
    object RouteDetail : Screen("route_detail", R.string.route_detail)
}
