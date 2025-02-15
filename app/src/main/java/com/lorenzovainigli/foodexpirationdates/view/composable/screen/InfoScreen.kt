package com.lorenzovainigli.foodexpirationdates.view.composable.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lorenzovainigli.foodexpirationdates.BuildConfig
import com.lorenzovainigli.foodexpirationdates.DEVELOPER_EMAIL
import com.lorenzovainigli.foodexpirationdates.GITHUB_URL
import com.lorenzovainigli.foodexpirationdates.PLAY_STORE_URL
import com.lorenzovainigli.foodexpirationdates.PRIVACY_POLICY_URL
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.WEBSITE_URL_EN
import com.lorenzovainigli.foodexpirationdates.WEBSITE_URL_IT
import com.lorenzovainigli.foodexpirationdates.model.Platform
import com.lorenzovainigli.foodexpirationdates.model.contributors
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.TextIconButton
import com.lorenzovainigli.foodexpirationdates.view.composable.TextIconButtonData
import java.util.Locale

@Composable
fun InfoScreen(
    context: Context = LocalContext.current
) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .padding(top = 16.dp),
            painter = painterResource(id = R.drawable.fed_icon),
            alignment = Alignment.Center,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
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
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(256.dp),
            onClick = {
                uriHandler.openUri(
                    uri = "https://github.com/lorenzovngl/FoodExpirationDates"
                )
            },
            imagePainter = painterResource(id = R.drawable.github),
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
            text = stringResource(id = R.string.feature_list),
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.support_this_project),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        arrayOf(
            TextIconButtonData(
                iconImageVector = Icons.Outlined.Star,
                text = stringResource(id = R.string.leave_a_star_on_github),
                onClick = {
                    uriHandler.openUri(
                        uri = GITHUB_URL
                    )
                },
            ),
            TextIconButtonData(
                iconImageVector = Icons.Outlined.Edit,
                text = stringResource(id = R.string.write_a_review),
                onClick = {
                    uriHandler.openUri(
                        uri = PLAY_STORE_URL
                    )
                },
            ),
            TextIconButtonData(
                iconImageVector = Icons.Outlined.Share,
                text = stringResource(id = R.string.share),
                onClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            if (Locale.getDefault().language == "it") WEBSITE_URL_IT
                            else WEBSITE_URL_EN
                        )
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                },
            ),
        ).forEach {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextIconButton(
                    modifier = Modifier.width(256.dp),
                    onClick = it.onClick,
                    iconImageVector = it.iconImageVector,
                    imagePainter = it.imagePainter,
                    text = it.text
                )
            }
        }
        ContactSection()
        ContributorsList()
        ClickableText(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp),
            text = AnnotatedString(text = stringResource(id = R.string.privacy_policy)),
            style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.primary),
            onClick = {
                uriHandler.openUri(
                    uri = PRIVACY_POLICY_URL
                )
            }
        )
    }
}

@Composable
fun ContactSection(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.contacts),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            text = stringResource(id = R.string.contacts_text)
        )
        val uriHandler = LocalUriHandler.current
        TextIconButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(256.dp),
            onClick = {
                uriHandler.openUri(
                    uri = "mailto:$DEVELOPER_EMAIL"
                )
            },
            iconImageVector = Icons.Outlined.Email,
            text = stringResource(id = R.string.send_an_email)
        )
    }
}

@Composable
fun ContributorsList(
    modifier: Modifier = Modifier
) {
    val contributorsText = remember {
        contributors.joinToString(separator = "\n") {
            "${it.name} - ${it.platform.url.substring(0, 1)}/@${it.username}".asListItem()
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 4.dp),
            text = stringResource(id = R.string.contributors_list_subtitle)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            text = Platform.entries.joinToString(" | ") {
                "(${it.url.substring(0, 1)}) ${it.url}"
            },
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = contributorsText
        )
    }
}

private fun String.asListItem() = "  • $this"

@PreviewLightDark
@Composable
fun InfoScreenPreview() {
    FoodExpirationDatesTheme {
        Surface (modifier = Modifier.fillMaxHeight()) {
            InfoScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContributorsListPreview() {
    FoodExpirationDatesTheme {
        Surface (modifier = Modifier.fillMaxWidth()) {
            ContributorsList()
        }
    }
}
