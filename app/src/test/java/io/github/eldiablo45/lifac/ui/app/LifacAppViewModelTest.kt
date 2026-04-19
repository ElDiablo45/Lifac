package io.github.eldiablo45.lifac.ui.app

import io.github.eldiablo45.lifac.data.client.ClientType
import io.github.eldiablo45.lifac.data.draft.StoredDraftBundle
import io.github.eldiablo45.lifac.data.draft.StoredDraftLine
import io.github.eldiablo45.lifac.data.draft.StoredInvoiceDraft
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

    @Test
    fun `stored draft maps back into editable draft state`() {
        val storedDraft = StoredDraftBundle(
            draft = StoredInvoiceDraft(
                selectedClientId = "client-1",
                selectedSeries = "2026",
                nextNumberPreview = "2026-0008",
                issueDate = "20/04/2026",
                operationDate = "21/04/2026",
                projectLabel = "Obra Centro",
                notes = "Nota de prueba",
                taxMode = "IVA 21%",
            ),
            lines = listOf(
                StoredDraftLine(
                    id = "line-1",
                    sortOrder = 0,
                    description = "Demolicion interior",
                    quantity = "2",
                    unitPrice = "150,00",
                    taxMode = "IVA 21%",
                ),
            ),
        )

        val draftState = storedDraft.toDraftFormState()

        assertEquals("client-1", draftState.selectedClientId)
        assertEquals("20/04/2026", draftState.issueDate)
        assertEquals("Obra Centro", draftState.projectLabel)
        assertEquals("Nota de prueba", draftState.notes)
        assertEquals("line-1", draftState.lines.single().id)
        assertEquals("Demolicion interior", draftState.lines.single().description)
        assertEquals(true, draftState.hasPersistedDraft)
    }

    @Test
    fun `validateDraftLineEditor rejects blank description`() {
        val editor = DraftLineEditorFormState(description = " ")

        assertEquals(
            "La descripcion de la linea es obligatoria.",
            validateDraftLineEditor(editor),
        )
    }

    @Test
    fun `validateConceptForm rejects blank name`() {
        val form = ConceptFormUiState(name = " ", description = "Material de agarre")

        assertEquals(
            "El nombre del concepto es obligatorio.",
            validateConceptForm(form),
        )
    }
}
