package com.lorenzovainigli.foodexpirationdates.model.repository

import kotlinx.coroutines.flow.Flow

interface ConnectivityRepository {
    val isConnected: Flow<Boolean>
}