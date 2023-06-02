package com.lorenzovainigli.foodexpirationdates.view

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.BuildConfig
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.PreferencesProvider
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation
import com.lorenzovainigli.foodexpirationdates.view.composable.MyTopAppBar
import com.lorenzovainigli.foodexpirationdates.view.composable.TextIconButton

class InfoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfoActivityLayout()
        }
    }

    @Composable
    @Preview
    fun InfoActivityLayout() {
        val context = LocalContext.current
        val activity = (LocalContext.current as? Activity)
        val uriHandler = LocalUriHandler.current
        FoodExpirationDatesTheme(
            darkTheme = PreferencesProvider.getDarkTheme(context),
            dynamicColor = PreferencesProvider.getDynamicColors(context)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                tonalElevation = TonalElevation.level2()
            ) {
                Scaffold(
                    topBar = {
                        MyTopAppBar(
                            title = stringResource(id = R.string.about_this_app),
                            navigationIcon = {
                                IconButton(onClick = { activity?.finish() }) {
                                    Icon(
                                        imageVector = Icons.Outlined.ArrowBack,
                                        contentDescription = stringResource(id = R.string.back),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
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
                                    .align(CenterHorizontally)
                                    .padding(top = 16.dp),
                                text = stringResource(id = R.string.app_name),
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                modifier = Modifier.align(CenterHorizontally),
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
                                modifier = Modifier.align(CenterHorizontally),
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
                            val sb = StringBuilder()
                            stringArrayResource(id = R.array.features).map {
                                sb.append("  ● ").append(it).append("\n")
                            }
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = sb.toString()
                            )
                            /*TextIconButton(
                            modifier = Modifier.align(CenterHorizontally),
                            onClick = {

                            },
                            imagePainter = painterResource(id = R.drawable.bug_report),
                            contentDescription = "Star",
                            text = stringResource(id = R.string.report_a_bug)
                        )*/
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = stringResource(id = R.string.support_this_project),
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center
                            )
                            TextIconButton(
                                modifier = Modifier.align(CenterHorizontally),
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
                                modifier = Modifier.align(CenterHorizontally),
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
                                modifier = Modifier.align(CenterHorizontally),
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
                                    .align(CenterHorizontally)
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

    @Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
    @Composable
    fun PreviewDarkMode() {
        InfoActivityLayout()
    }

    @Preview(name = "Italian", locale = "it", showBackground = true)
    @Composable
    fun PreviewItalian() {
        InfoActivityLayout()
    }

    @Preview(name = "Arabic", locale = "ar", showBackground = true)
    @Composable
    fun PreviewArabic() {
        InfoActivityLayout()
    }

    @Preview(name = "German", locale = "de", showBackground = true)
    @Composable
    fun PreviewGerman() {
        InfoActivityLayout()
    }

    @Preview(name = "Hindi", locale = "hi", showBackground = true)
    @Composable
    fun PreviewHindi() {
        InfoActivityLayout()
    }

    @Preview(name = "Spanish", locale = "es", showBackground = true)
    @Composable
    fun PreviewSpanish() {
        InfoActivityLayout()
    }

}