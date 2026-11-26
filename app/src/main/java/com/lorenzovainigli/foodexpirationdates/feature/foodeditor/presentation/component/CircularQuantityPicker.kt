package com.lorenzovainigli.foodexpirationdates.feature.foodeditor.presentation.component

import android.os.SystemClock
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun CircularQuantityPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    step: Int = 10,
    valueRange: IntRange = 1..10000,
    unit: String = "g",
) {
    require(step > 0)
    require(!valueRange.isEmpty())

    val latestValue by rememberUpdatedState(value)
    val latestOnValueChange by rememberUpdatedState(onValueChange)

    val hapticFeedback = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.06f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "Quantity wheel scale",
    )
    val hapticIntervalMillis = 200L
    val primary = MaterialTheme.colorScheme.primary
    val track = MaterialTheme.colorScheme.primaryContainer
    val surface = MaterialTheme.colorScheme.surface

    Box(
        modifier = modifier.size(200.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(step, valueRange) {
                    val center = Offset(
                        size.width / 2f,
                        size.height / 2f,
                    )

                    var lastHapticTime = 0L

                    fun performThrottledHapticFeedback() {
                        val now = SystemClock.uptimeMillis()

                        if (now - lastHapticTime >= hapticIntervalMillis) {
                            hapticFeedback.performHapticFeedback(
                                HapticFeedbackType.LongPress
                            )
                            lastHapticTime = now
                        }
                    }

                    fun angle(position: Offset): Float =
                        atan2(
                            position.y - center.y,
                            position.x - center.x,
                        )

                    awaitEachGesture {
                        val down = awaitFirstDown(
                            requireUnconsumed = false
                        )

                        val dx = down.position.x - center.x
                        val dy = down.position.y - center.y
                        val radius = hypot(dx, dy)
                        val diameter = size.width.toFloat()

                        val isOnWheel = radius in
                                (diameter * 0.28f)..(diameter * 0.5f)

                        if (isOnWheel) {
                            down.consume()

                            isPressed = true
                            performThrottledHapticFeedback()

                            var previousAngle = angle(down.position)
                            var accumulatedAngle = 0f
                            var gestureValue = latestValue

                            try {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    val change = event.changes.firstOrNull {
                                        it.id == down.id
                                    } ?: break

                                    if (!change.pressed) break

                                    val nextAngle = angle(change.position)
                                    var delta = nextAngle - previousAngle

                                    if (delta > PI) {
                                        delta -= (2 * PI).toFloat()
                                    } else if (delta < -PI) {
                                        delta += (2 * PI).toFloat()
                                    }

                                    previousAngle = nextAngle

                                    accumulatedAngle += Math.toDegrees(
                                        delta.toDouble()
                                    ).toFloat()

                                    while (accumulatedAngle >= 20f) {
                                        val newValue = (
                                                gestureValue.toLong() + step
                                                ).coerceAtMost(
                                                valueRange.last.toLong()
                                            ).toInt()

                                        accumulatedAngle -= 20f

                                        if (newValue != gestureValue) {
                                            gestureValue = newValue
                                            performThrottledHapticFeedback()
                                            latestOnValueChange(gestureValue)
                                        }
                                    }

                                    while (accumulatedAngle <= -20f) {
                                        val newValue = (
                                                gestureValue.toLong() - step
                                                ).coerceAtLeast(
                                                valueRange.first.toLong()
                                            ).toInt()

                                        accumulatedAngle += 20f

                                        if (newValue != gestureValue) {
                                            gestureValue = newValue
                                            performThrottledHapticFeedback()
                                            latestOnValueChange(gestureValue)
                                        }
                                    }

                                    change.consume()
                                }
                            } finally {
                                isPressed = false
                            }
                        } else {
                            waitForUpOrCancellation()
                        }
                    }
                }
        ) {
            val center = Offset(
                size.width / 2f,
                size.height / 2f,
            )

            val radius = size.minDimension * 0.43f

            drawCircle(
                color = track,
                radius = radius,
                center = center,
            )

            drawCircle(
                color = primary.copy(alpha = 0.35f),
                radius = radius,
                center = center,
                style = Stroke(width = 2.dp.toPx()),
            )

            for (index in 0 until 36) {
                val angle = Math.toRadians(
                    (index * 10 - 90).toDouble()
                )

                val innerRadius = radius * 0.81f
                val outerRadius = radius * 0.91f

                drawLine(
                    color = primary,
                    start = Offset(
                        center.x + innerRadius * cos(angle).toFloat(),
                        center.y + innerRadius * sin(angle).toFloat(),
                    ),
                    end = Offset(
                        center.x + outerRadius * cos(angle).toFloat(),
                        center.y + outerRadius * sin(angle).toFloat(),
                    ),
                    strokeWidth = if (index % 3 == 0) {
                        2.5.dp.toPx()
                    } else {
                        1.dp.toPx()
                    },
                    cap = StrokeCap.Round,
                )
            }

            drawCircle(
                color = surface,
                radius = radius * 0.65f,
                center = center,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value.toString(),
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold,
                color = primary,
            )

            Text(
                text = unit,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
