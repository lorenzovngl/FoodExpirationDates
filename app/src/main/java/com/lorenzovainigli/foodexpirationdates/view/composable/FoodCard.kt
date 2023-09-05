package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.PreferencesProvider
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.ui.theme.DarkOrange
import com.lorenzovainigli.foodexpirationdates.ui.theme.DarkRed
import com.lorenzovainigli.foodexpirationdates.ui.theme.DarkYellow
import com.lorenzovainigli.foodexpirationdates.ui.theme.LightOrange
import com.lorenzovainigli.foodexpirationdates.ui.theme.LightRed
import com.lorenzovainigli.foodexpirationdates.ui.theme.LightYellow
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.getItemsForPreview
import com.lorenzovainigli.foodexpirationdates.view.preview.DefaultPreviews
import com.lorenzovainigli.foodexpirationdates.view.preview.LanguagePreviews
import java.text.SimpleDateFormat
import java.util.Calendar

@Composable
fun FoodCard(
    item: ExpirationDate,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    isInDarkTheme: Boolean = false
) {
    val context = LocalContext.current
    val dateFormat = PreferencesProvider.getUserDateFormat(context)
    val sdf = SimpleDateFormat(dateFormat, context.resources.configuration.locales[0])
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
        if (item.expirationDate < today.time.time)
            if (isInDarkTheme) LightRed else DarkRed
        else if (item.expirationDate < tomorrow.time.time)
            if (isInDarkTheme) LightOrange else DarkOrange
        else if (item.expirationDate < withinAWeek.time.time)
            if (isInDarkTheme) LightYellow else DarkYellow
        else Color.Transparent
    val textColor =
        if (item.expirationDate < today.time.time) Color.White.copy(alpha = .9f)
        else if (item.expirationDate < tomorrow.time.time) Color.Black.copy(alpha = .9f)
        else if (item.expirationDate < withinAWeek.time.time) Color.Black.copy(alpha = .9f)
        else MaterialTheme.colorScheme.onSurface
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(10.dp)),
        tonalElevation = TonalElevation.level5()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icons8_cesto_96),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = item.foodName,
                color = textColor,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .clickable(onClick = onClickEdit),
                fontSize = 18.sp
            )
            Text(
                modifier = Modifier.padding(4.dp),
                color = textColor,
                text = expiration
            )
            Button(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .width(32.dp)
                    .height(32.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = onClickDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null
                )
            }
        }
    }
}

@DefaultPreviews
@LanguagePreviews
@Composable
fun FoodCardPreview() {
    val items = getItemsForPreview(LocalContext.current)
    Column {
        items.forEach {
            FoodCard(
                item = ExpirationDate(
                    id = 0,
                    foodName = it.foodName,
                    expirationDate = it.expirationDate
                ),
                onClickEdit = {},
                onClickDelete = {},
                isInDarkTheme = isSystemInDarkTheme()
            )
        }
    }
}