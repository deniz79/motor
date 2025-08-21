package com.motorlar.app.ui.navigation

import com.motorlar.app.R

sealed class Screen(val route: String, val resourceId: Int) {
    object Auth : Screen("auth", R.string.auth)
    object Home : Screen("home", R.string.home)
    object Map : Screen("map", R.string.map)
    object Record : Screen("record", R.string.record)
    object Reels : Screen("reels", R.string.reels)
    object Team : Screen("team", R.string.team)
    object Profile : Screen("profile", R.string.profile)
    object RouteDetail : Screen("route_detail", R.string.route_detail)
    object PostCreate : Screen("post_create", R.string.post_create)
    object RouteDrawing : Screen("route_drawing", R.string.route_drawing)
}
