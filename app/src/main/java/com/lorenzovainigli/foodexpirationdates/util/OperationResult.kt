package com.lorenzovainigli.foodexpirationdates.util

class OperationResult(
    val state: State = State.NOT_PERFORMED,
    val message: String = ""
) {

    enum class State {
        NOT_PERFORMED, SUCCESS, FAILURE
    }

}