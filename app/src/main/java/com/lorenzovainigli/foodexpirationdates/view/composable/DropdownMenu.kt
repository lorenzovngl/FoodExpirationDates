package com.lorenzovainigli.foodexpirationdates.view.composable

import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.view.InfoActivity
import com.lorenzovainigli.foodexpirationdates.view.SettingsActivity

data class MenuItem(
    val label: String,
    val icon: ImageVector,
    val action: () -> Unit
)

@Composable
fun getMenuItems(): List<MenuItem> {
    val context = LocalContext.current
    return arrayListOf(
        MenuItem(
            label = stringResource(id = R.string.settings),
            icon = Icons.Outlined.Settings,
            action = {
                context.startActivity(Intent(context, SettingsActivity::class.java))
            }
        ),
        MenuItem(
            label = stringResource(id = R.string.about_this_app),
            icon = Icons.Outlined.Info,
            action = {
                context.startActivity(Intent(context, InfoActivity::class.java))
            }
        )
    )
}

@Composable
fun DropdownMenu() {
    var isDropdownOpen by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = {
        isDropdownOpen = true
    }) {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = stringResource(id = R.string.menu),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
    DropdownMenu(
        modifier = Modifier.wrapContentSize(),
        expanded = isDropdownOpen,
        onDismissRequest = {
            isDropdownOpen = false
        },
        properties = PopupProperties()
    ) {
        val list = getMenuItems()
        // adding each menu item
        list.forEach { menuItem ->
            DropdownMenuItem(
                text = {
                    Row {
                        Icon(
                            imageVector = menuItem.icon,
                            contentDescription = menuItem.label
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = menuItem.label
                        )
                    }
                },
                onClick = {
                    menuItem.action()
                    isDropdownOpen = false
                },
                enabled = true
            )
        }
    }
}