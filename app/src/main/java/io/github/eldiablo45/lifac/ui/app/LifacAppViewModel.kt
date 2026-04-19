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
) : ViewModel() {
    private val currentSection = MutableStateFlow(LifacSection.HOME)
    private val clientForm = MutableStateFlow(ClientFormUiState())
    private val events = MutableSharedFlow<String>()

    val uiEvents = events.asSharedFlow()

    val uiState: StateFlow<LifacAppUiState> = combine(
        currentSection,
        clientRepository.observeClients(),
        clientForm,
    ) { section, storedClients, form ->
        LifacAppUiState(
            currentSection = section,
            emitter = EmitterProfileUiState(),
            invoices = sampleInvoices,
            clients = storedClients.map { storedClient ->
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
            },
            concepts = sampleConcepts,
            series = sampleSeries,
            draft = InvoiceDraftUiState(
                selectedClientLabel = if (storedClients.isEmpty()) {
                    "Seleccionar cliente o crear uno nuevo"
                } else {
                    "Selecciona un cliente guardado (${storedClients.size} disponibles)"
                },
                concepts = sampleDraftConcepts,
            ),
            clientForm = form,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LifacAppUiState(
            emitter = EmitterProfileUiState(),
            invoices = sampleInvoices,
            concepts = sampleConcepts,
            series = sampleSeries,
            draft = InvoiceDraftUiState(concepts = sampleDraftConcepts),
            clientForm = ClientFormUiState(),
        ),
    )

    fun navigateTo(section: LifacSection) {
        currentSection.value = section
    }

    fun openNewInvoice() {
        navigateTo(LifacSection.INVOICE_EDITOR)
    }

    fun leaveEditor() {
        navigateTo(LifacSection.HOME)
    }

    fun updateClientKind(kind: ClientType) {
        clientForm.update { current -> current.copy(kind = kind) }
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
            clientRepository.upsertClient(
                StoredClient(
                    id = UUID.randomUUID().toString(),
                    kind = normalizedForm.kind,
                    displayName = normalizedForm.displayName,
                    taxId = normalizedForm.taxId,
                    city = normalizedForm.city,
                    address = normalizedForm.address,
                    notes = normalizedForm.notes,
                ),
            )
            clientForm.value = ClientFormUiState(kind = normalizedForm.kind)
            events.emit("Cliente guardado localmente.")
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            val appContext = context.applicationContext
            return viewModelFactory {
                initializer {
                    LifacAppViewModel(
                        clientRepository = RoomClientRepository.from(appContext),
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
