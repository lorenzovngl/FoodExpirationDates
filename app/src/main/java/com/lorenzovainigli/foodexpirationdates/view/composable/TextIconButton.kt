package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.preview.DefaultPreviews

@Composable
fun TextIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    iconImageVector: ImageVector? = null,
    imagePainter: Painter? = null,
    text: String
) {
    Button(
        modifier = modifier.padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val iconModifier = Modifier.size(36.dp)
            val iconTint = MaterialTheme.colorScheme.onTertiaryContainer
            if (imagePainter != null) {
                Icon(
                    modifier = iconModifier,
                    painter = imagePainter,
                    contentDescription = text,
                    tint = iconTint
                )
            } else if (iconImageVector != null) {
                Icon(
                    modifier = iconModifier,
                    imageVector = iconImageVector,
                    contentDescription = text,
                    tint = iconTint
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = text)
        }
    }
}

@DefaultPreviews
@Composable
fun TextIconButtonPreview(){
    FoodExpirationDatesTheme {
        TextIconButton(
            onClick = {},
            iconImageVector = Icons.Outlined.Email,
            text = stringResource(id = R.string.send_an_email)
        )
    }
}

data class TextIconButtonData(
    val iconImageVector: ImageVector? = null,
    val imagePainter: Painter? = null,
    val text: String,
    val onClick: () -> Unit,
)