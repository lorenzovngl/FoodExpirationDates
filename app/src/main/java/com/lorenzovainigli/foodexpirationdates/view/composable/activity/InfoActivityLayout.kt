package com.lorenzovainigli.foodexpirationdates.view.composable.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzovainigli.foodexpirationdates.BuildConfig
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.contributors
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.MyTopAppBar
import com.lorenzovainigli.foodexpirationdates.view.composable.TextIconButton
import com.lorenzovainigli.foodexpirationdates.view.preview.DefaultPreviews
import com.lorenzovainigli.foodexpirationdates.view.preview.DevicePreviews
import com.lorenzovainigli.foodexpirationdates.view.preview.LanguagePreviews
import com.lorenzovainigli.foodexpirationdates.viewmodel.PreferencesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoActivityLayout(
    context: Context = LocalContext.current,
    prefsViewModel: PreferencesViewModel? = viewModel()
) {
    val activity = (context as? Activity)
    val uriHandler = LocalUriHandler.current
    val darkThemeState = prefsViewModel?.getThemeMode(context)?.collectAsState()?.value
        ?: PreferencesRepository.Companion.ThemeMode.SYSTEM
    val dynamicColorsState = prefsViewModel?.getDynamicColors(context)?.collectAsState()?.value
        ?: false
    val isInDarkTheme = when (darkThemeState) {
        PreferencesRepository.Companion.ThemeMode.LIGHT.ordinal -> false
        PreferencesRepository.Companion.ThemeMode.DARK.ordinal -> true
        else -> isSystemInDarkTheme()
    }
    val features = stringArrayResource(id = R.array.features)
        .joinToString(separator = "\n") { it.asListItem() }
    FoodExpirationDatesTheme(
        darkTheme = isInDarkTheme,
        dynamicColor = dynamicColorsState
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    MyTopAppBar(
                        title = stringResource(id = R.string.about_this_app),
                        navigationIcon = {
                            IconButton(onClick = { activity?.finish() }) {
                                Icon(
                                    imageVector = Icons.Outlined.ArrowBack,
                                    contentDescription = stringResource(id = R.string.back),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        prefsViewModel = prefsViewModel
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(
                        modifier = Modifier
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 16.dp),
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.headlineLarge,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.bodySmall,
                            text = stringResource(
                                id = R.string.version_x,
                                BuildConfig.VERSION_NAME
                            ),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(
                                id = R.string.app_description,
                                stringResource(id = R.string.app_name)
                            )
                        )
                        TextIconButton(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                uriHandler.openUri(
                                    uri = "https://github.com/lorenzovngl/FoodExpirationDates"
                                )
                            },
                            imagePainter = painterResource(id = R.drawable.github),
                            contentDescription = stringResource(id = R.string.source_code),
                            text = stringResource(id = R.string.source_code)
                        )
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = stringResource(id = R.string.features),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = features
                        )
                        /*TextIconButton(
                        modifier = Modifier.align(CenterHorizontally),
                        onClick = {

                        },
                        imagePainter = painterResource(id = R.drawable.bug_report),
                        contentDescription = "Star",
                        text = stringResource(id = R.string.report_a_bug)
                    )*/
                        ContributorsList()
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = stringResource(id = R.string.support_this_project),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )
                        TextIconButton(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                uriHandler.openUri(
                                    uri = "https://github.com/lorenzovngl/FoodExpirationDates"
                                )
                            },
                            iconImageVector = Icons.Outlined.Star,
                            contentDescription = stringResource(id = R.string.leave_a_star_on_github),
                            text = stringResource(id = R.string.leave_a_star_on_github)
                        )
                        TextIconButton(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                uriHandler.openUri(
                                    uri = "https://play.google.com/store/apps/details?id=com.lorenzovainigli.foodexpirationdates"
                                )
                            },
                            iconImageVector = Icons.Outlined.Edit,
                            contentDescription = "Star",
                            text = stringResource(id = R.string.write_a_review)
                        )
                        TextIconButton(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "https://play.google.com/store/apps/details?id=com.lorenzovainigli.foodexpirationdates"
                                    )
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, null)
                                context.startActivity(shareIntent)
                            },
                            iconImageVector = Icons.Outlined.Share,
                            contentDescription = stringResource(id = R.string.share),
                            text = stringResource(id = R.string.share)
                        )
                        ClickableText(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 4.dp),
                            text = AnnotatedString(text = stringResource(id = R.string.privacy_policy)),
                            style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.primary),
                            onClick = {
                                uriHandler.openUri(
                                    uri = "https://github.com/lorenzovngl/FoodExpirationDates/blob/main/privacy-policy.md"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContributorsList(
    modifier: Modifier = Modifier
) {
    val contributorsText = remember {
        contributors.joinToString(separator = "\n") {
            "${it.name} (@${it.username})".asListItem()
        }
    }
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.contributors_list_title),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            text = stringResource(id = R.string.contributors_list_subtitle)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = contributorsText
        )
    }
}

private fun String.asListItem() = "  ● $this"

@DefaultPreviews
@DevicePreviews
@LanguagePreviews
@Composable
fun InfoActivityLayoutPreview() {
    InfoActivityLayout()
}

@DefaultPreviews
@DevicePreviews
@LanguagePreviews
@Composable
fun ContributorsListPreview() {
    ContributorsList()
}
