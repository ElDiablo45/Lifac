package io.github.eldiablo45.lifac.ui.app

import io.github.eldiablo45.lifac.data.client.ClientType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LifacAppViewModelTest {
    @Test
    fun `validateClientForm accepts non empty display name`() {
        val form = ClientFormUiState(
            kind = ClientType.BUSINESS,
            displayName = "Construcciones Norte",
        )

        assertNull(validateClientForm(form))
    }

    @Test
    fun `validateClientForm rejects blank display name`() {
        val form = ClientFormUiState(displayName = " ")

        assertEquals(
            "El nombre del cliente es obligatorio.",
            validateClientForm(form),
        )
    }
}
