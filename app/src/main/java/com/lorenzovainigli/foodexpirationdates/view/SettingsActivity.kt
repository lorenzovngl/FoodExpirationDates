package com.lorenzovainigli.foodexpirationdates.view

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.DateFormatProvider
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.DateFormatDialog
import com.lorenzovainigli.foodexpirationdates.view.composable.MyTopAppBar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsActivityLayout()
        }
    }

    @Composable
    @Preview
    fun SettingsActivityLayout() {
        val context = LocalContext.current
        val activity = (LocalContext.current as? Activity)
        var dateFormat = DateFormatProvider.getUserDateFormat(context)
        var sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        var isDialogOpen by remember {
            mutableStateOf(false)
        }
        FoodExpirationDatesTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    topBar = {
                        MyTopAppBar(
                            title = stringResource(id = R.string.settings),
                            navigationIcon = {
                                IconButton(onClick = { activity?.finish() }) {
                                    Icon(
                                        imageVector = Icons.Outlined.ArrowBack,
                                        contentDescription = stringResource(id = R.string.back),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        )
                    }
                ) { padding ->
                    DateFormatDialog(
                        isDialogOpen = isDialogOpen,
                        onDismissRequest = {
                            dateFormat = DateFormatProvider.getUserDateFormat(context)
                            sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
                            isDialogOpen = false
                        }
                    )
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .padding(10.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row {
                            Text(
                               text = stringResource(id = R.string.date_format),
                               style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight())
                            ClickableText(
                                modifier = Modifier.testTag(stringResource(id = R.string.date_format)),
                                text = AnnotatedString(sdf.format(Calendar.getInstance().time)),
                                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                                onClick = {
                                    isDialogOpen = true
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
        SettingsActivityLayout()
    }

    @Preview(name = "Italian", locale = "it", showBackground = true)
    @Composable
    fun PreviewItalian() {
        SettingsActivityLayout()
    }

    @Preview(name = "Arabic", locale = "ar", showBackground = true)
    @Composable
    fun PreviewArabic() {
        SettingsActivityLayout()
    }

    @Preview(name = "German", locale = "de", showBackground = true)
    @Composable
    fun PreviewGerman() {
        SettingsActivityLayout()
    }

    @Preview(name = "Hindi", locale = "hi", showBackground = true)
    @Composable
    fun PreviewHindi() {
        SettingsActivityLayout()
    }

    @Preview(name = "Spanish", locale = "es", showBackground = true)
    @Composable
    fun PreviewSpanish() {
        SettingsActivityLayout()
    }
}