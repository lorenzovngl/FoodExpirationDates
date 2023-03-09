package com.lorenzovainigli.foodexpirationdates.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.di.AppModule
import com.lorenzovainigli.foodexpirationdates.di.DaggerAppComponent
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.ui.theme.*
import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerAppComponent.builder()
            .appModule(AppModule())
            .build()
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
    fun FoodCard(item: ExpirationDate, onClickEdit: () -> Unit, onClickDelete: () -> Unit) {
        val sdf = SimpleDateFormat("d MMM", Locale.getDefault())
        val today = Calendar.getInstance()
        val twoDaysAgo = Calendar.getInstance()
        twoDaysAgo.add(Calendar.DAY_OF_MONTH, -2)
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_MONTH, -1)
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_MONTH, 1)
        val withinAWeek = Calendar.getInstance()
        withinAWeek.add(Calendar.DAY_OF_MONTH, 7)
        val msInADay = (1000 * 60 * 60 * 24)
        val expiration =
            if (item.expirationDate < twoDaysAgo.time.time) {
                val days = (today.time.time - item.expirationDate) / msInADay
                stringResource(R.string.n_days_ago, days)
            } else if (item.expirationDate < yesterday.time.time)
                stringResource(R.string.yesterday)
            else if (item.expirationDate < today.time.time)
                stringResource(R.string.today)
            else if (item.expirationDate < tomorrow.time.time)
                stringResource(R.string.tomorrow)
            else if (item.expirationDate < withinAWeek.time.time) {
                val days = (item.expirationDate - today.time.time) / msInADay
                stringResource(R.string.in_n_days, days + 1)
            } else sdf.format(item.expirationDate)
        val bgColor =
            if (item.expirationDate < today.time.time) Red700
            else if (item.expirationDate < tomorrow.time.time) Orange500
            else if (item.expirationDate < withinAWeek.time.time) Yellow500
            else Color.Transparent
        val textColor =
            if (item.expirationDate < today.time.time) Color.White
            else if (item.expirationDate < tomorrow.time.time) Color.Black
            else if (item.expirationDate < withinAWeek.time.time) Color.Black
            else MaterialTheme.colorScheme.onBackground
        Box(
            modifier = Modifier
                .padding(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(bgColor)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icons8_food_basket_96),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = item.foodName,
                    color = textColor,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .clickable(onClick = onClickEdit),
                    fontSize = 18.sp
                )
                Text(
                    modifier = Modifier.padding(4.dp),
                    color = textColor,
                    text = expiration
                )
                Icon(
                    tint = textColor,
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(start = 8.dp)
                        .clickable(onClick = onClickDelete)
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainActivityLayout(
        items: List<ExpirationDate>? = null,
        viewModel: ExpirationDateViewModel? = viewModel(),
        deleteExpirationDate: ((ExpirationDate) -> Unit)? = viewModel!!::deleteExpirationDate,
        topBarTitle: String = stringResource(id = R.string.app_name)
    ) {
        val context = LocalContext.current
        FoodExpirationDatesTheme {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            Text(text = topBarTitle)
                        })
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            context.startActivity(Intent(context, InsertActivity::class.java))
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = null
                            )
                        }
                    }
                ) { padding ->
                    Column(Modifier.padding(padding)) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            if (items != null) {
                                for (item in items) {
                                    FoodCard(
                                        item = item,
                                        onClickEdit = {
                                            val intent = Intent(context, InsertActivity::class.java)
                                            intent.putExtra("ITEM_ID", item.id)
                                            context.startActivity(intent)
                                        },
                                        onClickDelete =  {
                                            if (deleteExpirationDate != null) {
                                                deleteExpirationDate(item)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun getItemsForPreview(): List<ExpirationDate> {
        val items = ArrayList<ExpirationDate>()
        val cal = Calendar.getInstance()
        items.add(
            ExpirationDate(
                id = 0,
                foodName = "Mozzarella",
                expirationDate = cal.time.time
            )
        )
        cal.add(Calendar.DATE, 1)
        items.add(
            ExpirationDate(
                id = 0,
                foodName = "Prosciutto",
                expirationDate = cal.time.time
            )
        )
        cal.add(Calendar.DATE, 2)
        items.add(
            ExpirationDate(
                id = 0,
                foodName = "Pancetta",
                expirationDate = cal.time.time
            )
        )
        cal.add(Calendar.DATE, 4)
        items.add(
            ExpirationDate(
                id = 0,
                foodName = "Tortellini",
                expirationDate = cal.time.time
            )
        )
        cal.add(Calendar.DATE, 7)
        items.add(
            ExpirationDate(
                id = 0,
                foodName = "Yogurt",
                expirationDate = cal.time.time
            )
        )
        return items
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        val items = getItemsForPreview()
        FoodExpirationDatesTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainActivityLayout(
                    items = items,
                    viewModel = null,
                    deleteExpirationDate = null
                )
            }
        }
    }

    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
    @Composable
    fun PreviewNight() {
        val items = getItemsForPreview()
        FoodExpirationDatesTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainActivityLayout(items, null, null)
            }
        }
    }

    @Preview(locale = "it", showBackground = true)
    @Composable
    fun PreviewIT() {
        val items = getItemsForPreview()
        FoodExpirationDatesTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainActivityLayout(items, null, null)
            }
        }
    }
}