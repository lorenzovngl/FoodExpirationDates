package com.lorenzovainigli.news.presentation.viewmodel

import com.lorenzovainigli.news.MainDispatcherRule
import com.lorenzovainigli.news.domain.model.NewsItem
import com.lorenzovainigli.news.domain.usecase.GetLatestNewsUseCase
import com.lorenzovainigli.news.domain.usecase.MarkAllNewsAsReadUseCase
import com.lorenzovainigli.news.domain.usecase.MarkNewsAsReadUseCase
import com.lorenzovainigli.news.domain.usecase.MarkNewsAsUnreadUseCase
import com.lorenzovainigli.news.domain.usecase.RefreshNewsUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getLatestNewsUseCase: GetLatestNewsUseCase = mock()
    private val refreshNewsUseCase: RefreshNewsUseCase = mock()
    private val markNewsAsReadUseCase: MarkNewsAsReadUseCase = mock()
    private val markNewsAsUnreadUseCase: MarkNewsAsUnreadUseCase = mock()
    private val markAllNewsAsReadUseCase: MarkAllNewsAsReadUseCase = mock()


    private fun createViewModel() = NewsViewModel(
        getLatestNewsUseCase = getLatestNewsUseCase,
        refreshNewsUseCase = refreshNewsUseCase,
        markNewsAsReadUseCase = markNewsAsReadUseCase,
        markNewsAsUnreadUseCase = markNewsAsUnreadUseCase,
        markAllNewsAsReadUseCase = markAllNewsAsReadUseCase
    )

    @Test
    fun init_loadsNews() = runTest {
        // Given
        val news = listOf(
            NewsItem(
                id = "1",
                title = "Article",
                description = null,
                url = "https://example.com",
                publishedAt = Instant.now(),
                isRead = false
            )
        )

        whenever(getLatestNewsUseCase()).thenReturn(news)

        // When
        val viewModel = createViewModel()

        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(news, state.news)
        assertNull(state.errorMessage)

        verify(getLatestNewsUseCase).invoke()
    }

    @Test
    fun refreshNews_whenSuccessful_updatesNewsAndClearsRefreshingState() = runTest {
        // Given
        val initialNews = listOf(
            NewsItem(
                id = "1",
                title = "Old article",
                description = null,
                url = "https://example.com/old",
                publishedAt = null,
                isRead = false
            )
        )

        val refreshedNews = listOf(
            NewsItem(
                id = "2",
                title = "New article",
                description = null,
                url = "https://example.com/new",
                publishedAt = null,
                isRead = false
            )
        )

        whenever(getLatestNewsUseCase())
            .thenReturn(initialNews)
            .thenReturn(refreshedNews)

        whenever(refreshNewsUseCase())
            .thenReturn(Result.success(Unit))

        val viewModel = createViewModel()

        // Complete the initial load
        advanceUntilIdle()

        // When
        viewModel.refreshNews()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value

        assertFalse(state.isRefreshing)
        assertEquals(refreshedNews, state.news)
        assertNull(state.errorMessage)

        verify(refreshNewsUseCase).invoke()
        verify(getLatestNewsUseCase, times(2)).invoke()
    }

    @Test
    fun refreshNews_whenFailure_showsErrorMessage() = runTest {
        // Given
        val news = listOf(
            NewsItem(
                id = "1",
                title = "Article",
                description = null,
                url = "https://example.com",
                publishedAt = null,
                isRead = false
            )
        )

        whenever(getLatestNewsUseCase())
            .thenReturn(news)
            .thenReturn(news)

        whenever(refreshNewsUseCase())
            .thenReturn(Result.failure(RuntimeException()))

        val viewModel = createViewModel()

        // Complete initial load
        advanceUntilIdle()

        // When
        viewModel.refreshNews()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value

        assertFalse(state.isRefreshing)
        assertEquals(news, state.news)
        assertEquals(
            "Impossibile aggiornare le novità.",
            state.errorMessage
        )

        verify(refreshNewsUseCase).invoke()
        verify(getLatestNewsUseCase, times(2)).invoke()
    }

    @Test
    fun refreshNews_whenAlreadyRefreshing_ignoresSecondRequest() = runTest {
        // Given
        val refreshGate = CompletableDeferred<Unit>()

        whenever(getLatestNewsUseCase()).thenReturn(emptyList())

        doSuspendableAnswer {
            refreshGate.await()
            Result.success(Unit)
        }.whenever(refreshNewsUseCase).invoke()

        val viewModel = createViewModel()
        advanceUntilIdle()

        // When: start the first refresh and leave it suspended
        viewModel.refreshNews()
        runCurrent()

        assertTrue(viewModel.uiState.value.isRefreshing)

        // This second request should be ignored
        viewModel.refreshNews()
        runCurrent()

        // Then
        verify(refreshNewsUseCase, times(1)).invoke()

        // Complete the first refresh
        refreshGate.complete(Unit)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isRefreshing)
        verify(getLatestNewsUseCase, times(2)).invoke()
    }

    @Test
    fun markAsRead_updatesNewsList() = runTest {
        // Given
        val initialNews = listOf(
            NewsItem(
                id = "1",
                title = "Article",
                description = null,
                url = "https://example.com",
                publishedAt = null,
                isRead = false
            )
        )

        val updatedNews = listOf(
            initialNews.first().copy(isRead = true)
        )

        whenever(getLatestNewsUseCase())
            .thenReturn(initialNews)
            .thenReturn(updatedNews)

        val viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.markAsRead("1")
        advanceUntilIdle()

        // Then
        assertEquals(updatedNews, viewModel.uiState.value.news)

        verify(markNewsAsReadUseCase).invoke("1")
        verify(getLatestNewsUseCase, times(2)).invoke()
    }

    @Test
    fun markAsUnread_updatesNewsList() = runTest {
        // Given
        val initialNews = listOf(
            NewsItem(
                id = "1",
                title = "Article",
                description = null,
                url = "https://example.com",
                publishedAt = null,
                isRead = true
            )
        )

        val updatedNews = listOf(
            initialNews.first().copy(isRead = false)
        )

        whenever(getLatestNewsUseCase())
            .thenReturn(initialNews)
            .thenReturn(updatedNews)

        val viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.markAsUnread("1")
        advanceUntilIdle()

        // Then
        assertEquals(updatedNews, viewModel.uiState.value.news)

        verify(markNewsAsUnreadUseCase).invoke("1")
        verify(getLatestNewsUseCase, times(2)).invoke()
    }

    @Test
    fun markAllAsRead_updatesNewsList() = runTest {
        // Given
        val initialNews = listOf(
            NewsItem(
                id = "1",
                title = "First article",
                description = null,
                url = "https://example.com/1",
                publishedAt = null,
                isRead = false
            ),
            NewsItem(
                id = "2",
                title = "Second article",
                description = null,
                url = "https://example.com/2",
                publishedAt = null,
                isRead = false
            )
        )

        val updatedNews = initialNews.map {
            it.copy(isRead = true)
        }

        whenever(getLatestNewsUseCase())
            .thenReturn(initialNews)
            .thenReturn(updatedNews)

        val viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.markAllAsRead()
        advanceUntilIdle()

        // Then
        assertEquals(updatedNews, viewModel.uiState.value.news)

        verify(markAllNewsAsReadUseCase).invoke()
        verify(getLatestNewsUseCase, times(2)).invoke()
    }

}