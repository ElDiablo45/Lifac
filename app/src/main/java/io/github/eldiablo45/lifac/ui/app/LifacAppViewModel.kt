package io.github.eldiablo45.lifac.ui.app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LifacAppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        LifacAppUiState(
            emitter = EmitterProfileUiState(),
            invoices = sampleInvoices,
            clients = sampleClients,
            concepts = sampleConcepts,
            series = sampleSeries,
            draft = InvoiceDraftUiState(concepts = sampleDraftConcepts),
        ),
    )
    val uiState: StateFlow<LifacAppUiState> = _uiState.asStateFlow()

    fun navigateTo(section: LifacSection) {
        _uiState.update { current -> current.copy(currentSection = section) }
    }

    fun openNewInvoice() {
        navigateTo(LifacSection.INVOICE_EDITOR)
    }

    fun leaveEditor() {
        navigateTo(LifacSection.HOME)
    }

    companion object {
        private val sampleInvoices = listOf(
            InvoiceListItemUiState(
                id = "invoice-1",
                number = "2026-0007",
                clientName = "Promociones Sierra Norte",
                status = "Emitida",
                total = "1.452,00 EUR",
                date = "18/04/2026",
            ),
            InvoiceListItemUiState(
                id = "invoice-2",
                number = "2026-0006",
                clientName = "Marta Ruiz",
                status = "Borrador",
                total = "484,00 EUR",
                date = "17/04/2026",
            ),
            InvoiceListItemUiState(
                id = "invoice-3",
                number = "2026-0005",
                clientName = "Reformas Bellavista SL",
                status = "Emitida",
                total = "2.904,00 EUR",
                date = "15/04/2026",
            ),
        )

        private val sampleClients = listOf(
            ClientListItemUiState(
                id = "client-1",
                displayName = "Promociones Sierra Norte",
                kind = ClientKind.BUSINESS,
                taxId = "B80909090",
                city = "Madrid",
            ),
            ClientListItemUiState(
                id = "client-2",
                displayName = "Marta Ruiz",
                kind = ClientKind.INDIVIDUAL,
                taxId = "Placeholder NIF",
                city = "Alcobendas",
            ),
            ClientListItemUiState(
                id = "client-3",
                displayName = "Reformas Bellavista SL",
                kind = ClientKind.BUSINESS,
                taxId = "B70010020",
                city = "Getafe",
            ),
        )

        private val sampleConcepts = listOf(
            ConceptListItemUiState(
                id = "concept-1",
                name = "Jornada de obra",
                price = "220,00 EUR",
                taxLabel = "IVA 21%",
            ),
            ConceptListItemUiState(
                id = "concept-2",
                name = "Material auxiliar",
                price = "45,00 EUR",
                taxLabel = "IVA 21%",
            ),
            ConceptListItemUiState(
                id = "concept-3",
                name = "Reforma vivienda",
                price = "Placeholder",
                taxLabel = "IVA 10%",
            ),
        )

        private val sampleSeries = listOf(
            SeriesListItemUiState(code = "2026", nextNumber = "0008"),
            SeriesListItemUiState(code = "OBRA", nextNumber = "0012"),
        )

        private val sampleDraftConcepts = listOf(
            DraftConceptUiState(
                description = "Demolicion y retirada de escombros",
                quantity = "1",
                unitPrice = "600,00 EUR",
                taxLabel = "IVA 21%",
                total = "726,00 EUR",
            ),
            DraftConceptUiState(
                description = "Suministro de material de agarre",
                quantity = "8",
                unitPrice = "22,50 EUR",
                taxLabel = "IVA 21%",
                total = "217,80 EUR",
            ),
        )
    }
}
