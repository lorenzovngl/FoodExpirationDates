package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun TextIconButton(
    modifier: Modifier,
    onClick: () -> Unit,
    iconImageVector: ImageVector? = null,
    imagePainter: Painter? = null,
    contentDescription: String,
    text: String
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
        onClick = onClick
    ) {
        if (imagePainter != null) {
            Icon(
                painter = imagePainter,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onTertiary
            )
        } else if (iconImageVector != null) {
            Icon(
                imageVector = iconImageVector,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text)
    }
}