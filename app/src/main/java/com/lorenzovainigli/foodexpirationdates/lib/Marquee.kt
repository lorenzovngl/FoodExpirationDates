import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive

/**
 * Marquee is an implementation of marquee effect from Android XML for JetpackCompose
 *
 * @param modifier Adjust the drawing layout or drawing decoration of the content.
 * @param params Params which specify appearance of marquee effect
 * @param content Composable content that will be applied marquee effect.
 */
@Composable
fun Marquee(
    modifier: Modifier = Modifier,
    params: MarqueeParams = defaultMarqueeParams(),
    content: @Composable (Modifier) -> Unit
) {
    var xOffset by remember { mutableStateOf(0) }
    val layoutInfoState = remember { mutableStateOf<MarqueeLayoutInfo?>(null) }

    LaunchedEffect(layoutInfoState.value) {
        val ltr = params.direction == LayoutDirection.Ltr

        val layoutInfo = layoutInfoState.value ?: return@LaunchedEffect
        if (layoutInfo.width <= layoutInfo.containerWidth) return@LaunchedEffect

        val duration = params.period * layoutInfo.width / layoutInfo.containerWidth

        val animation = TargetBasedAnimation(
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = duration, easing = params.easing
                ), repeatMode = RepeatMode.Restart
            ),
            typeConverter = Int.VectorConverter,
            initialValue = if (ltr) 0 else -layoutInfo.width,
            targetValue = if (ltr) -layoutInfo.width else 0
        )

        val startTime = withFrameNanos { it }

        do {
            val playTime = withFrameNanos { it } - startTime
            xOffset = animation.getValueFromNanos(playTime)
        } while (isActive)
    }

    SubcomposeLayout(
        modifier = modifier.clipToBounds()
    ) { constraints ->
        val infiniteWidthConstraints = constraints.copy(maxWidth = Int.MAX_VALUE)

        var main = subcompose(MarqueeLayers.Main) {
            content(Modifier)
        }.first().measure(infiniteWidthConstraints)

        var gradient: Placeable? = null
        var secondPlaceableWithOffset: Pair<Placeable, Int>? = null

        if (main.width <= constraints.maxWidth) {
            main = subcompose(MarqueeLayers.Secondary) {
                content(Modifier.fillMaxWidth())
            }.first().measure(constraints)
            layoutInfoState.value = null
        } else {
            val spacing = constraints.maxWidth * 2 / 3
            layoutInfoState.value = MarqueeLayoutInfo(
                width = main.width + spacing, containerWidth = constraints.maxWidth
            )
            val secondTextOffset = main.width + xOffset + spacing
            val secondTextSpace = constraints.maxWidth - secondTextOffset

            if (secondTextSpace > 0) {
                secondPlaceableWithOffset = subcompose(MarqueeLayers.Secondary) {
                    content(Modifier)
                }.first().measure(infiniteWidthConstraints) to secondTextOffset
            }
            gradient = if (params.gradientEnabled) subcompose(MarqueeLayers.EdgesGradient) {
                Row {
                    GradientEdge(
                        startColor = params.gradientEdgeColor,
                        endColor = Color.Transparent
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    GradientEdge(
                        startColor = Color.Transparent,
                        endColor = params.gradientEdgeColor
                    )
                }
            }.first().measure(constraints = constraints.copy(maxHeight = main.height)) else null
        }

        layout(
            width = constraints.maxWidth, height = main.height
        ) {
            main.place(xOffset, 0)
            secondPlaceableWithOffset?.let {
                it.first.place(it.second, 0)
            }
            gradient?.place(0, 0)
        }
    }
}

/**
 * Data class which represents Marquee layout params
 *
 * @param period Period of marquee effect, the shorter the period, the faster the effect
 * @param gradientEnabled If gradient edges will be shown or not
 * @param gradientEdgeColor Color of gradient edges
 * @param direction Direction of marquee effect reproducing
 * @param easing Easing of marquee effect
 */
data class MarqueeParams(
    @androidx.annotation.IntRange(from = 1, to = Long.MAX_VALUE) val period: Int = 7500,
    val gradientEnabled: Boolean,
    val gradientEdgeColor: Color,
    val direction: LayoutDirection,
    val easing: Easing
)

/**
 * Function which represents default Marquee layout params
 */
@Composable
fun defaultMarqueeParams(): MarqueeParams = MarqueeParams(
    period = 7500,
    gradientEnabled = true,
    gradientEdgeColor = Color.White,
    direction = LocalLayoutDirection.current,
    easing = LinearEasing
)

@Composable
private fun GradientEdge(
    startColor: Color, endColor: Color
) {
    Box(
        modifier = Modifier
            .width(10.dp)
            .fillMaxHeight()
            .background(
                brush = Brush.horizontalGradient(
                    0f to startColor, 1f to endColor
                )
            )
    )
}

private enum class MarqueeLayers {
    Main,
    Secondary,
    EdgesGradient
}

private data class MarqueeLayoutInfo(
    val width: Int,
    val containerWidth: Int
)
