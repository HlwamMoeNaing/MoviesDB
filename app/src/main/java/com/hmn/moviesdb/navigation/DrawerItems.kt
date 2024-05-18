package com.hmn.moviesdb.navigation

import com.hmn.moviesdb.R

data class NavigationDrawerItems(
    val title: String,
    val selectedIcon: Int,
    val unSelectedIcon: Int,
    val badeCount: Int? = null,
    val routes:String
)
