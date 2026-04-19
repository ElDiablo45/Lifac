package io.github.eldiablo45.lifac.ui.home

import org.junit.Assert.assertEquals
import org.junit.Test

class GreetingViewModelTest {
    @Test
    fun `default state exposes hello world`() {
        val viewModel = GreetingViewModel()

        assertEquals(
            GreetingUiState(),
            viewModel.uiState.value,
        )
    }
}

