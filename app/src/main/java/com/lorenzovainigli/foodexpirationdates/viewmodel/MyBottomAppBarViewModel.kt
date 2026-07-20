package com.lorenzovainigli.foodexpirationdates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovainigli.news.domain.usecase.ObserveUnreadNewsCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyBottomAppBarViewModel @Inject constructor(
    observeUnreadNewsCountUseCase: ObserveUnreadNewsCountUseCase
): ViewModel() {

    val unreadNewsCount: StateFlow<Int> =
        observeUnreadNewsCountUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )
}