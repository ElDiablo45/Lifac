package io.github.eldiablo45.lifac.ui.app

import io.github.eldiablo45.lifac.data.client.ClientType

enum class LifacSection(
    val title: String,
    val visibleInBottomBar: Boolean,
) {
    HOME(title = "Lifac", visibleInBottomBar = true),
    INVOICES(title = "Facturas", visibleInBottomBar = true),
    CLIENTS(title = "Clientes", visibleInBottomBar = true),
    CONCEPTS(title = "Conceptos", visibleInBottomBar = true),
    SETTINGS(title = "Ajustes", visibleInBottomBar = true),
    INVOICE_EDITOR(title = "Nueva factura", visibleInBottomBar = false),
}

enum class ClientKind {
    BUSINESS,
    INDIVIDUAL,
}

data class LifacAppUiState(
    val currentSection: LifacSection = LifacSection.HOME,
    val emitter: EmitterProfileUiState = EmitterProfileUiState(),
    val invoices: List<InvoiceListItemUiState> = emptyList(),
    val clients: List<ClientListItemUiState> = emptyList(),
    val concepts: List<ConceptListItemUiState> = emptyList(),
    val series: List<SeriesListItemUiState> = emptyList(),
    val draft: InvoiceDraftUiState = InvoiceDraftUiState(),
    val clientForm: ClientFormUiState = ClientFormUiState(),
)

data class EmitterProfileUiState(
    val businessName: String = "Construcciones Lifac",
    val taxId: String = "B12345678",
    val fiscalAddress: String = "Av. de la Reforma 18",
    val city: String = "Madrid",
    val email: String = "admin@lifac.es",
)

data class InvoiceListItemUiState(
    val id: String,
    val number: String,
    val clientName: String,
    val status: String,
    val total: String,
    val date: String,
)

data class ClientListItemUiState(
    val id: String,
    val displayName: String,
    val kind: ClientKind,
    val taxId: String,
    val city: String,
    val address: String,
    val notes: String,
)

data class ClientFormUiState(
    val kind: ClientType = ClientType.BUSINESS,
    val displayName: String = "",
    val taxId: String = "",
    val city: String = "",
    val address: String = "",
    val notes: String = "",
)

data class ConceptListItemUiState(
    val id: String,
    val name: String,
    val price: String,
    val taxLabel: String,
)

data class SeriesListItemUiState(
    val code: String,
    val nextNumber: String,
)

data class InvoiceDraftUiState(
    val selectedClientLabel: String = "Seleccionar cliente o crear uno nuevo",
    val selectedSeries: String = "2026",
    val nextNumberPreview: String = "2026-0008",
    val issueDate: String = "19/04/2026",
    val operationDate: String = "Placeholder: misma fecha por ahora",
    val projectLabel: String = "Placeholder: nombre de obra o referencia",
    val notes: String = "Placeholder: nota legal o comentario final",
    val taxMode: String = "IVA 21%",
    val concepts: List<DraftConceptUiState> = emptyList(),
)

data class DraftConceptUiState(
    val description: String,
    val quantity: String,
    val unitPrice: String,
    val taxLabel: String,
    val total: String,
)
