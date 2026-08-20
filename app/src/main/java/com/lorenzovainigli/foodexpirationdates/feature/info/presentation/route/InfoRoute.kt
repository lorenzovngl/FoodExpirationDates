package com.lorenzovainigli.foodexpirationdates.feature.info.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lorenzovainigli.foodexpirationdates.BuildConfig
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InfoScreen

@Composable
fun InfoRoute(
    onClickShare: () -> Unit,
    onClickReview: () -> Unit
){
    val versionText =
        if (BuildConfig.DEBUG) "Build ${BuildConfig.APP_VERSION_LABEL}"
        else stringResource(
            id = R.string.version_x,
            BuildConfig.APP_VERSION_LABEL
        )
    InfoScreen (
        onClickShare = onClickShare,
        onClickReview = onClickReview,
        versionText = versionText
    )
}