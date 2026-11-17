package com.lorenzovainigli.foodexpirationdates.view.composable.scaffold

data class MyScaffoldUiState(
    val currentDestination: String = "",
    val isEditingItem: Boolean = false,
    val showPermissionBanner: Boolean = false,
    val unreadNewsCount: Int = 0,
    val topBarFont: Int? = null,
    val monochromeIconsEnabled: Boolean = false,
)
