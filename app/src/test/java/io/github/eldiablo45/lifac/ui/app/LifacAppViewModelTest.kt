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

    @Test
    fun `buildDraftClientLabel uses selected client when available`() {
        val selectedClient = ClientListItemUiState(
            id = "client-1",
            displayName = "Promociones Sierra Norte",
            kind = ClientKind.BUSINESS,
            taxId = "B80909090",
            city = "Madrid",
            address = "",
            notes = "",
        )

        assertEquals(
            "Promociones Sierra Norte",
            buildDraftClientLabel(
                selectedClient = selectedClient,
                availableClientCount = 3,
            ),
        )
        assertEquals(
            "Empresa · B80909090 · Madrid",
            buildDraftClientMeta(selectedClient),
        )
    }
}
