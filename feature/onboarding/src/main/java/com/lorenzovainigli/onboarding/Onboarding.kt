package com.lorenzovainigli.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Onboarding(
    pageCount: Int,
    onComplete: () -> Unit,
    onSkip: (Int) -> Unit,
    onPageViewed: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    nextLabel: String,
    backLabel: String,
    skipLabel: String,
    completeLabel: String,
    pageContent: @Composable (Int) -> Unit
) {
    require(pageCount > 0)

    val pagerState = rememberPagerState(
        pageCount = { pageCount }
    )
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .collect { page ->
                onPageViewed(page)
            }
    }
    val scope = rememberCoroutineScope()

    val currentPage = pagerState.currentPage
    val isFirstPage = currentPage == 0
    val isLastPage = currentPage == pageCount - 1

    Column(
        modifier = modifier.fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 600.dp)
                            .padding(24.dp)
                    ) {
                        pageContent(page)
                    }
                }
            }
        }

        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pageCount) { index ->
                val selected = index == currentPage

                val width by animateDpAsState(
                    targetValue = if (selected) 24.dp else 8.dp,
                    label = "IndicatorWidth"
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .width(width)
                        .height(8.dp)
                        .background(
                            color = if (selected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outlineVariant,
                            shape = CircleShape
                        )
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
//            if (isFirstPage) {
                TextButton(onClick = {
                    onSkip(pagerState.settledPage)
                }) {
                    Text(skipLabel)
                }
//            } else {
//                TextButton(
//                    onClick = {
//                        scope.launch {
//                            pagerState.animateScrollToPage(
//                                currentPage - 1
//                            )
//                        }
//                    }
//                ) {
//                    Text(backLabel)
//                }
//            }

            Button(
                onClick = {
                    if (isLastPage) {
                        onComplete()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                currentPage + 1
                            )
                        }
                    }
                }
            ) {
                Text(
                    if (isLastPage) completeLabel
                    else nextLabel
                )
            }
        }
    }
}
