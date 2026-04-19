package io.github.eldiablo45.lifac.ui.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LifacAppViewModelTest {
    @Test
    fun `initial state exposes invoice workspace sample data`() {
        val viewModel = LifacAppViewModel()
        val state = viewModel.uiState.value

        assertEquals(LifacSection.HOME, state.currentSection)
        assertEquals("Construcciones Lifac", state.emitter.businessName)
        assertTrue(state.invoices.isNotEmpty())
        assertTrue(state.clients.isNotEmpty())
        assertTrue(state.concepts.isNotEmpty())
        assertEquals("2026", state.draft.selectedSeries)
    }
}
