package io.github.eldiablo45.lifac.ui.app

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.eldiablo45.lifac.data.client.ClientRepository
import io.github.eldiablo45.lifac.data.client.ClientType
import io.github.eldiablo45.lifac.data.client.RoomClientRepository
import io.github.eldiablo45.lifac.data.client.StoredClient
import io.github.eldiablo45.lifac.data.draft.DraftRepository
import io.github.eldiablo45.lifac.data.draft.RoomDraftRepository
import io.github.eldiablo45.lifac.data.draft.StoredInvoiceDraft
import java.util.UUID
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LifacAppViewModel(
    private val clientRepository: ClientRepository,
    private val draftRepository: DraftRepository,
) : ViewModel() {
    private val currentSection = MutableStateFlow(LifacSection.HOME)
    private val clientForm = MutableStateFlow(ClientFormUiState())
    private val draftForm = MutableStateFlow(defaultDraftForm())
    private val pickingClientForDraft = MutableStateFlow(false)
    private val events = MutableSharedFlow<String>()

    val uiEvents = events.asSharedFlow()

    init {
        viewModelScope.launch {
            draftRepository.getActiveDraft()?.let { storedDraft ->
                draftForm.value = storedDraft.toDraftFormState()
            }
        }
    }

    val uiState: StateFlow<LifacAppUiState> = combine(
        currentSection,
        clientRepository.observeClients(),
        clientForm,
        draftForm,
        pickingClientForDraft,
    ) { section, storedClients, form, draft, isPickingClientForDraft ->
        val mappedClients = storedClients.map { storedClient ->
            ClientListItemUiState(
                id = storedClient.id,
                displayName = storedClient.displayName,
                kind = when (storedClient.kind) {
                    ClientType.BUSINESS -> ClientKind.BUSINESS
                    ClientType.INDIVIDUAL -> ClientKind.INDIVIDUAL
                },
                taxId = storedClient.taxId.ifBlank { "Placeholder fiscal pendiente" },
                city = storedClient.city.ifBlank { "Ciudad pendiente" },
                address = storedClient.address,
                notes = storedClient.notes,
            )
        }
        val selectedClient = mappedClients.firstOrNull { it.id == draft.selectedClientId }

        LifacAppUiState(
            currentSection = section,
            emitter = EmitterProfileUiState(),
            invoices = sampleInvoices,
            clients = mappedClients,
            concepts = sampleConcepts,
            series = sampleSeries,
            draft = InvoiceDraftUiState(
                selectedClientId = selectedClient?.id,
                selectedClientLabel = buildDraftClientLabel(
                    selectedClient = selectedClient,
                    availableClientCount = storedClients.size,
                ),
                selectedClientMeta = buildDraftClientMeta(selectedClient),
                selectedSeries = draft.selectedSeries,
                nextNumberPreview = draft.nextNumberPreview,
                issueDate = draft.issueDate,
                operationDate = draft.operationDate,
                projectLabel = draft.projectLabel,
                notes = draft.notes,
                taxMode = draft.taxMode,
                concepts = sampleDraftConcepts,
                hasPersistedDraft = draft.hasPersistedDraft,
            ),
            clientForm = form,
            isPickingClientForDraft = isPickingClientForDraft,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LifacAppUiState(
            emitter = EmitterProfileUiState(),
            invoices = sampleInvoices,
            concepts = sampleConcepts,
            series = sampleSeries,
            draft = InvoiceDraftUiState(
                concepts = sampleDraftConcepts,
                hasPersistedDraft = false,
            ),
            clientForm = ClientFormUiState(),
        ),
    )

    fun navigateTo(section: LifacSection) {
        currentSection.value = section
        if (section != LifacSection.CLIENTS) {
            pickingClientForDraft.value = false
        }
    }

    fun openNewInvoice() {
        pickingClientForDraft.value = false
        navigateTo(LifacSection.INVOICE_EDITOR)
    }

    fun leaveEditor() {
        pickingClientForDraft.value = false
        navigateTo(LifacSection.HOME)
    }

    fun selectDraftClient(clientId: String) {
        draftForm.update { current -> current.copy(selectedClientId = clientId) }
    }

    fun pickClientAndReturnToDraft(clientId: String) {
        draftForm.update { current -> current.copy(selectedClientId = clientId) }
        pickingClientForDraft.value = false
        navigateTo(LifacSection.INVOICE_EDITOR)
    }

    fun openClientsForDraft() {
        pickingClientForDraft.value = true
        navigateTo(LifacSection.CLIENTS)
        viewModelScope.launch {
            events.emit("Crea o revisa clientes y luego vuelve a Nueva factura para seleccionarlo.")
        }
    }

    fun returnToDraftEditor() {
        pickingClientForDraft.value = false
        navigateTo(LifacSection.INVOICE_EDITOR)
    }

    fun updateClientKind(kind: ClientType) {
        clientForm.update { current -> current.copy(kind = kind) }
    }

    fun updateDraftIssueDate(value: String) {
        draftForm.update { current -> current.copy(issueDate = value) }
    }

    fun updateDraftOperationDate(value: String) {
        draftForm.update { current -> current.copy(operationDate = value) }
    }

    fun updateDraftProjectLabel(value: String) {
        draftForm.update { current -> current.copy(projectLabel = value) }
    }

    fun updateDraftNotes(value: String) {
        draftForm.update { current -> current.copy(notes = value) }
    }

    fun updateClientDisplayName(value: String) {
        clientForm.update { current -> current.copy(displayName = value) }
    }

    fun updateClientTaxId(value: String) {
        clientForm.update { current -> current.copy(taxId = value) }
    }

    fun updateClientCity(value: String) {
        clientForm.update { current -> current.copy(city = value) }
    }

    fun updateClientAddress(value: String) {
        clientForm.update { current -> current.copy(address = value) }
    }

    fun updateClientNotes(value: String) {
        clientForm.update { current -> current.copy(notes = value) }
    }

    fun resetClientForm() {
        clientForm.value = ClientFormUiState(kind = clientForm.value.kind)
    }

    fun saveClient() {
        val normalizedForm = clientForm.value.normalized()
        val validationError = validateClientForm(normalizedForm)

        if (validationError != null) {
            viewModelScope.launch {
                events.emit(validationError)
            }
            return
        }

        viewModelScope.launch {
            val newClientId = UUID.randomUUID().toString()
            clientRepository.upsertClient(
                StoredClient(
                    id = newClientId,
                    kind = normalizedForm.kind,
                    displayName = normalizedForm.displayName,
                    taxId = normalizedForm.taxId,
                    city = normalizedForm.city,
                    address = normalizedForm.address,
                    notes = normalizedForm.notes,
                ),
            )
            if (pickingClientForDraft.value) {
                draftForm.update { current -> current.copy(selectedClientId = newClientId) }
                pickingClientForDraft.value = false
                currentSection.value = LifacSection.INVOICE_EDITOR
            }
            clientForm.value = ClientFormUiState(kind = normalizedForm.kind)
            events.emit(
                if (currentSection.value == LifacSection.INVOICE_EDITOR) {
                    "Cliente guardado y seleccionado en el borrador."
                } else {
                    "Cliente guardado localmente."
                },
            )
        }
    }

    fun saveDraft() {
        val draftToSave = draftForm.value.normalized()

        viewModelScope.launch {
            draftRepository.upsertActiveDraft(draftToSave.toStoredDraft())
            draftForm.value = draftToSave.copy(hasPersistedDraft = true)
            events.emit("Borrador guardado localmente.")
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            val appContext = context.applicationContext
            return viewModelFactory {
                initializer {
                    LifacAppViewModel(
                        clientRepository = RoomClientRepository.from(appContext),
                        draftRepository = RoomDraftRepository.from(appContext),
                    )
                }
            }
        }

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

internal data class InvoiceDraftFormState(
    val selectedClientId: String? = null,
    val selectedSeries: String = "2026",
    val nextNumberPreview: String = "2026-0008",
    val issueDate: String = "19/04/2026",
    val operationDate: String = "",
    val projectLabel: String = "",
    val notes: String = "",
    val taxMode: String = "IVA 21%",
    val hasPersistedDraft: Boolean = false,
)

private fun defaultDraftForm(): InvoiceDraftFormState = InvoiceDraftFormState()

internal fun validateClientForm(form: ClientFormUiState): String? {
    return if (form.displayName.isBlank()) {
        "El nombre del cliente es obligatorio."
    } else {
        null
    }
}

private fun ClientFormUiState.normalized(): ClientFormUiState {
    return copy(
        displayName = displayName.trim(),
        taxId = taxId.trim(),
        city = city.trim(),
        address = address.trim(),
        notes = notes.trim(),
    )
}

private fun InvoiceDraftFormState.normalized(): InvoiceDraftFormState {
    return copy(
        issueDate = issueDate.trim(),
        operationDate = operationDate.trim(),
        projectLabel = projectLabel.trim(),
        notes = notes.trim(),
    )
}

private fun InvoiceDraftFormState.toStoredDraft(): StoredInvoiceDraft {
    return StoredInvoiceDraft(
        selectedClientId = selectedClientId,
        selectedSeries = selectedSeries,
        nextNumberPreview = nextNumberPreview,
        issueDate = issueDate,
        operationDate = operationDate,
        projectLabel = projectLabel,
        notes = notes,
        taxMode = taxMode,
    )
}

internal fun StoredInvoiceDraft.toDraftFormState(): InvoiceDraftFormState {
    return InvoiceDraftFormState(
        selectedClientId = selectedClientId,
        selectedSeries = selectedSeries,
        nextNumberPreview = nextNumberPreview,
        issueDate = issueDate,
        operationDate = operationDate,
        projectLabel = projectLabel,
        notes = notes,
        taxMode = taxMode,
        hasPersistedDraft = true,
    )
}

internal fun buildDraftClientLabel(
    selectedClient: ClientListItemUiState?,
    availableClientCount: Int,
): String {
    return when {
        selectedClient != null -> selectedClient.displayName
        availableClientCount == 0 -> "Seleccionar cliente o crear uno nuevo"
        else -> "Selecciona un cliente guardado ($availableClientCount disponibles)"
    }
}

internal fun buildDraftClientMeta(
    selectedClient: ClientListItemUiState?,
): String {
    return if (selectedClient == null) {
        "Placeholder: elige un cliente real o crea uno nuevo"
    } else {
        buildString {
            append(clientKindLabel(selectedClient.kind))
            append(" · ")
            append(selectedClient.taxId)
            append(" · ")
            append(selectedClient.city)
        }
    }
}

private fun clientKindLabel(kind: ClientKind): String = when (kind) {
    ClientKind.BUSINESS -> "Empresa"
    ClientKind.INDIVIDUAL -> "Particular"
}
