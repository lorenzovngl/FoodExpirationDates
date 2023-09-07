package com.lorenzovainigli.foodexpirationdates.view.composable.activity

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation
import com.lorenzovainigli.foodexpirationdates.view.activity.InfoActivity
import com.lorenzovainigli.foodexpirationdates.view.activity.InsertActivity
import com.lorenzovainigli.foodexpirationdates.view.activity.SettingsActivity
import com.lorenzovainigli.foodexpirationdates.view.composable.FoodCard
import com.lorenzovainigli.foodexpirationdates.view.composable.MyTopAppBar
import com.lorenzovainigli.foodexpirationdates.view.preview.DevicePreviews
import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDatesViewModel
import com.lorenzovainigli.foodexpirationdates.viewmodel.PreferencesViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.min

@Composable
fun MainActivityLayout(
    context: Context = LocalContext.current,
    viewModel: ExpirationDatesViewModel? = viewModel(),
    prefsViewModel: PreferencesViewModel? = viewModel(),
    items: List<ExpirationDate>? = null,
    addExpirationDate: ((ExpirationDate) -> Unit)? = viewModel!!::addExpirationDate,
    deleteExpirationDate: ((ExpirationDate) -> Unit)? = viewModel!!::deleteExpirationDate
) {
    val darkThemeState = prefsViewModel?.getThemeMode(context)?.collectAsState()?.value
        ?: PreferencesRepository.Companion.ThemeMode.SYSTEM
    val dynamicColorsState = prefsViewModel?.getDynamicColors(context)?.collectAsState()?.value
        ?: false
    val isInDarkTheme = when (darkThemeState) {
        PreferencesRepository.Companion.ThemeMode.LIGHT.ordinal -> false
        PreferencesRepository.Companion.ThemeMode.DARK.ordinal -> true
        else -> isSystemInDarkTheme()
    }
    FoodExpirationDatesTheme(
        darkTheme = isInDarkTheme,
        dynamicColor = dynamicColorsState
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    MyTopAppBar(
                        title = stringResource(id = R.string.app_name),
                        navigationIcon = {
                            Image(
                                modifier = Modifier
                                    .padding(horizontal = 7.dp)
                                    .size(48.dp),
                                painter = painterResource(id = R.drawable.fed_icon),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                    )
                },
                bottomBar = {
                    NavigationBar(
                        tonalElevation = TonalElevation.level0()
                    ) {
                        NavigationBarItem(
                            selected = false,
                            onClick = {
                                context.startActivity(Intent(context, InfoActivity::class.java))
                            },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.about_this_app),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = stringResource(id = R.string.about_this_app),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        )
                        if (!items.isNullOrEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxHeight(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    modifier = Modifier
                                        .size(56.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    onClick = {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                InsertActivity::class.java
                                            )
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.tertiary,
                                        contentColor = MaterialTheme.colorScheme.onTertiary
                                    ),
                                    shape = CircleShape
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Add,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                        NavigationBarItem(
                            selected = false,
                            onClick = {
                                context.startActivity(
                                    Intent(
                                        context,
                                        SettingsActivity::class.java
                                    )
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.settings),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = stringResource(id = R.string.settings),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        )
                    }
                }
            ) { padding ->
                if (!items.isNullOrEmpty()) {
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(4.dp)
                        ) {
                            for (item in items) {
                                FoodCard(
                                    item = item,
                                    onClickEdit = {
                                        val intent =
                                            Intent(context, InsertActivity::class.java)
                                        intent.putExtra("ITEM_ID", item.id)
                                        context.startActivity(intent)
                                    },
                                    onClickDelete = {
                                        if (deleteExpirationDate != null) {
                                            scope.launch {
                                                val snackbarResult =
                                                    snackbarHostState.showSnackbar(
                                                        message = context.resources.getString(
                                                            R.string.x_deleted,
                                                            item.foodName
                                                        ),
                                                        actionLabel = context.resources.getString(R.string.undo),
                                                        duration = SnackbarDuration.Short
                                                    )
                                                when (snackbarResult) {
                                                    SnackbarResult.ActionPerformed -> {
                                                        if (addExpirationDate != null) {
                                                            addExpirationDate(item)
                                                        }
                                                    }

                                                    else -> {}
                                                }
                                            }
                                            deleteExpirationDate(item)
                                        }
                                    },
                                    isInDarkTheme = isInDarkTheme
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(
                            space = 16.dp,
                            alignment = Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_items_found),
                            style = MaterialTheme.typography.displaySmall,
                            color = Color.Gray.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(id = R.string.please_insert_one),
                            textAlign = TextAlign.Center
                        )
                        Button(
                            modifier = Modifier.size(50.dp),
                            contentPadding = PaddingValues(0.dp),
                            onClick = {
                                context.startActivity(
                                    Intent(
                                        context,
                                        InsertActivity::class.java
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.onTertiary
                            ),
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getItemsForPreview(context: Context): List<ExpirationDate> {
    val items = ArrayList<ExpirationDate>()
    val foods = context.resources.getStringArray(R.array.example_foods)
    val daysLeft = arrayOf(-1, 0, 1, 3, 7, 10, 30)
    for (i in 0 until min(foods.size, daysLeft.size)) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, daysLeft[i])
        items.add(
            ExpirationDate(
                id = 0,
                foodName = foods[i],
                expirationDate = cal.time.time
            )
        )
    }
    return items
}

@Preview(showBackground = true, wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@DevicePreviews
@Composable
fun MainActivityLayoutPreview() {
    val context = LocalContext.current
    val items = getItemsForPreview(context)
    MainActivityLayout(
        items = items,
        viewModel = null,
        prefsViewModel = null,
        addExpirationDate = null,
        deleteExpirationDate = null
    )
}