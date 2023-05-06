package com.lorenzovainigli.foodexpirationdates.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.di.AppModule
import com.lorenzovainigli.foodexpirationdates.di.DaggerAppComponent
import com.lorenzovainigli.foodexpirationdates.view.composable.DropdownMenu
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.notification.NotificationScheduler
import com.lorenzovainigli.foodexpirationdates.model.notification.ScheduledNotification
import com.lorenzovainigli.foodexpirationdates.ui.theme.*
import com.lorenzovainigli.foodexpirationdates.view.composable.FoodCard
import com.lorenzovainigli.foodexpirationdates.view.composable.MyTopAppBar
import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerAppComponent.builder()
            .appModule(AppModule())
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationScheduler = NotificationScheduler(this, this)
            notificationScheduler.setup()
            notificationScheduler.schedule(
                ScheduledNotification(
                    time = LocalDateTime.now().plusSeconds(10),
                    title = "Hellooo",
                    message = "I'm a scheduled notification!"
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        setContent {
            val viewModel: ExpirationDateViewModel = viewModel()
            val dates by viewModel.getDates().collectAsState(emptyList())
            MainActivityLayout(dates)
        }
    }

    @Composable
    fun MainActivityLayout(
        items: List<ExpirationDate>? = null,
        viewModel: ExpirationDateViewModel? = viewModel(),
        deleteExpirationDate: ((ExpirationDate) -> Unit)? = viewModel!!::deleteExpirationDate,
    ) {
        val context = LocalContext.current
        FoodExpirationDatesTheme {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                Scaffold(
                    topBar = {
                        MyTopAppBar(
                            title = stringResource(id = R.string.app_name),
                            actions = {
                                DropdownMenu()
                            }
                        )
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        if (items != null && items.isNotEmpty()) {
                            FloatingActionButton(
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            InsertActivity::class.java
                                        )
                                    )
                                },
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.onTertiary
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                ) { padding ->
                    if (items != null && items.isNotEmpty()) {
                        Column(Modifier.padding(padding)) {
                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                for (item in items) {
                                    FoodCard(
                                        item = item,
                                        onClickEdit = {
                                            val intent = Intent(context, InsertActivity::class.java)
                                            intent.putExtra("ITEM_ID", item.id)
                                            context.startActivity(intent)
                                        },
                                        onClickDelete = {
                                            if (deleteExpirationDate != null) {
                                                deleteExpirationDate(item)
                                            }
                                        }
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
                            horizontalAlignment = CenterHorizontally
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

    private fun getItemsForPreview(lang: String = "en"): List<ExpirationDate> {
        val items = ArrayList<ExpirationDate>()
        var foods = arrayOf("Eggs", "Cheese", "Milk", "Ham", "Butter", "Mushrooms", "Tomato")
        if (lang == "it")
            foods = arrayOf("Uova", "Formaggio", "Latte", "Prosciutto", "Funghi", "Pomodori")
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
    @Composable
    fun DefaultPreview() {
        val items = getItemsForPreview()
        MainActivityLayout(
            items = items,
            viewModel = null,
            deleteExpirationDate = null
        )
    }

    @Preview(
        name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true,
        wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE
    )
    @Composable
    fun PreviewDarkMode() {
        DefaultPreview()
    }

    @Preview(name = "Italian", locale = "it", showBackground = true)
    @Composable
    fun PreviewItalian() {
        DefaultPreview()
    }

    @Preview(name = "Arabic", locale = "ar", showBackground = true)
    @Composable
    fun PreviewArabic() {
        DefaultPreview()
    }

    @Preview(name = "German", locale = "de", showBackground = true)
    @Composable
    fun PreviewGerman() {
        DefaultPreview()
    }

    @Preview(name = "Hindi", locale = "hi", showBackground = true)
    @Composable
    fun PreviewHindi() {
        DefaultPreview()
    }

    @Preview(name = "Spanish", locale = "es", showBackground = true)
    @Composable
    fun PreviewSpanish() {
        DefaultPreview()
    }

}