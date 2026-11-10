package com.lorenzovainigli.foodexpirationdates.onboarding.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme

@Composable
fun NotificationPermissionPage(
    notificationsEnabled: Boolean,
    onEnableNotifications: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.NotificationsActive,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = stringResource(
                R.string.onboarding_notifications_title
            ),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(
                R.string.onboarding_notifications_description
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(32.dp))

        if (notificationsEnabled) {
            Text(
                text = stringResource(
                    R.string.onboarding_notifications_enabled
                ),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        } else {
            Button(
                onClick = onEnableNotifications
            ) {
                Text(
                    stringResource(
                        R.string.onboarding_notifications_enable
                    )
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(
                    R.string.onboarding_notifications_optional
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationPermissionPagePreview() {
    FoodExpirationDatesTheme {
        NotificationPermissionPage(
            notificationsEnabled = false,
            onEnableNotifications = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationPermissionPageEnabledPreview() {
    FoodExpirationDatesTheme {
        NotificationPermissionPage(
            notificationsEnabled = true,
            onEnableNotifications = {}
        )
    }
}
