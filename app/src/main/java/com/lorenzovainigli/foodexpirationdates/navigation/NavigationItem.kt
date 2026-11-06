package com.lorenzovainigli.foodexpirationdates.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val showBadge: Boolean = false,
    val badgeNumber: Int? = null
)