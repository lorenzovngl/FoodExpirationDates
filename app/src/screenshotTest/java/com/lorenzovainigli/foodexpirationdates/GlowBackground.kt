package com.lorenzovainigli.foodexpirationdates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GlowBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .clipToBounds()
    ) {
        Box(
            modifier = Modifier
                .size(580.dp)
                .offset(x = (-100).dp, y = (-80).dp)
                .blur(
                    radius = 300.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .background(
                    color = Color(0xFF2196F3).copy(alpha = 0.35f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 80.dp)
                .size(460.dp)
                .blur(
                    radius = 200.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .background(
                    color = Color(0xFFE91E63).copy(alpha = 0.20f),
                    shape = CircleShape
                )
        )

        content()
    }
}

@Preview(
    device = "spec:width=1080px,height=1080px,dpi=160",
    showBackground = true
)
@Composable
fun GlowBackgroundPreview() {
    GlowBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            Text(
                text = "Hello",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )
        }
    }
}