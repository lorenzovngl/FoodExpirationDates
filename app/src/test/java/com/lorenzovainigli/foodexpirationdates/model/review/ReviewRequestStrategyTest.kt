package com.lorenzovainigli.foodexpirationdates.model.review

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class ReviewRequestStrategyTest {

    @Test
    fun `review is not requested before 50 foods`() {
        val preferences = FakeReviewPreferences(initialCount = 0)
        val strategy = ReviewRequestStrategy(preferences)

        repeat(49) {
            val result = strategy.onFoodAdded()
            assertFalse(result.shouldRequestReview)
        }
    }

    @Test
    fun `review is requested at 50 foods`() {
        val preferences = FakeReviewPreferences(initialCount = 49)
        val strategy = ReviewRequestStrategy(preferences)

        val result = strategy.onFoodAdded()

        assertTrue(result.shouldRequestReview)
        assertEquals(50, result.foodCount)
    }

    @Test
    fun `review is not requested at 51 foods`() {
        val preferences = FakeReviewPreferences(initialCount = 50)
        val strategy = ReviewRequestStrategy(preferences)

        val result = strategy.onFoodAdded()

        assertFalse(result.shouldRequestReview)
        assertEquals(51, result.foodCount)
    }

    @Test
    fun `review is requested again at 100 foods`() {
        val preferences = FakeReviewPreferences(initialCount = 99)
        val strategy = ReviewRequestStrategy(preferences)

        val result = strategy.onFoodAdded()

        assertTrue(result.shouldRequestReview)
        assertEquals(100, result.foodCount)
    }

}