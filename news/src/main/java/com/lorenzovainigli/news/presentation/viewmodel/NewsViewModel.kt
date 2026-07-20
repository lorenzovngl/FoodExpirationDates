package com.lorenzovainigli.news.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovainigli.news.domain.usecase.GetLatestNewsUseCase
import com.lorenzovainigli.news.domain.usecase.MarkAllNewsAsReadUseCase
import com.lorenzovainigli.news.domain.usecase.MarkNewsAsReadUseCase
import com.lorenzovainigli.news.domain.usecase.MarkNewsAsUnreadUseCase
import com.lorenzovainigli.news.domain.usecase.RefreshNewsUseCase
import com.lorenzovainigli.news.presentation.state.NewsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getLatestNewsUseCase: GetLatestNewsUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase,
    private val markNewsAsReadUseCase: MarkNewsAsReadUseCase,
    private val markNewsAsUnreadUseCase: MarkNewsAsUnreadUseCase,
    private val markAllNewsAsReadUseCase: MarkAllNewsAsReadUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUiState(isLoading = true))
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val result = refreshNewsUseCase()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                news = getLatestNewsUseCase(),
                errorMessage = if (result.isFailure) {
                    "Impossibile aggiornare le novità."
                } else {
                    null
                }
            )
        }
    }

    fun markAsRead(newsId: String) {
        viewModelScope.launch {
            markNewsAsReadUseCase(newsId)
            _uiState.value = _uiState.value.copy(
                news = getLatestNewsUseCase()
            )
        }
    }

    fun markAsUnread(newsId: String) {
        viewModelScope.launch {
            markNewsAsUnreadUseCase(newsId)
            _uiState.value = _uiState.value.copy(
                news = getLatestNewsUseCase()
            )
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            markAllNewsAsReadUseCase()
            _uiState.value = _uiState.value.copy(
                news = getLatestNewsUseCase()
            )
        }
    }
}