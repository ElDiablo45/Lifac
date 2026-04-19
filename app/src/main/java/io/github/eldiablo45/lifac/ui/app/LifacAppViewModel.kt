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
import io.github.eldiablo45.lifac.data.concept.ConceptRepository
import io.github.eldiablo45.lifac.data.concept.RoomConceptRepository
import io.github.eldiablo45.lifac.data.concept.StoredConcept
import io.github.eldiablo45.lifac.data.draft.DraftRepository
import io.github.eldiablo45.lifac.data.draft.RoomDraftRepository
import io.github.eldiablo45.lifac.data.draft.StoredDraftBundle
import io.github.eldiablo45.lifac.data.draft.StoredDraftLine
import io.github.eldiablo45.lifac.data.draft.StoredInvoiceDraft
import java.util.Locale
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
    private val conceptRepository: ConceptRepository,
    private val draftRepository: DraftRepository,
) : ViewModel() {
    private val currentSection = MutableStateFlow(LifacSection.HOME)
    private val clientForm = MutableStateFlow(ClientFormUiState())
    private val conceptForm = MutableStateFlow(ConceptFormUiState())
    private val draftForm = MutableStateFlow(defaultDraftForm())
    private val pickingClientForDraft = MutableStateFlow(false)
    private val pickingConceptForDraft = MutableStateFlow(false)
    private val events = MutableSharedFlow<String>()

    val uiEvents = events.asSharedFlow()

    init {
        viewModelScope.launch {
            draftRepository.getActiveDraft()?.let { storedDraft ->
                draftForm.value = storedDraft.toDraftFormState()
            }
        }
    }

    private val uiBaseState = combine(
        currentSection,
        clientRepository.observeClients(),
        conceptRepository.observeConcepts(),
        clientForm,
        conceptForm,
    ) { section, storedClients, storedConcepts, currentClientForm, currentConceptForm ->
        UiBaseState(
            section = section,
            storedClients = storedClients,
            storedConcepts = storedConcepts,
            currentClientForm = currentClientForm,
            currentConceptForm = currentConceptForm,
        )
    }

    val uiState: StateFlow<LifacAppUiState> = combine(
        uiBaseState,
        draftForm,
        pickingClientForDraft,
        pickingConceptForDraft,
    ) { baseState, draft, isPickingClient, isPickingConcept ->
        val section = baseState.section
        val storedClients = baseState.storedClients
        val storedConcepts = baseState.storedConcepts
        val currentClientForm = baseState.currentClientForm
        val currentConceptForm = baseState.currentConceptForm
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
        val mappedConcepts = storedConcepts.map { storedConcept ->
            ConceptListItemUiState(
                id = storedConcept.id,
                name = storedConcept.name,
                description = storedConcept.description.ifBlank { "Descripcion pendiente" },
                price = formatConceptPrice(storedConcept.unitPrice),
                taxLabel = storedConcept.taxMode,
            )
        }
        val selectedClient = mappedClients.firstOrNull { it.id == draft.selectedClientId }
        val draftLines = draft.lines
            .sortedBy { it.sortOrder }
            .map { line -> line.toUiState() }
        val totals = calculateDraftTotals(draft.lines)

        LifacAppUiState(
            currentSection = section,
            emitter = EmitterProfileUiState(),
            invoices = sampleInvoices,
            clients = mappedClients,
            concepts = mappedConcepts,
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
                concepts = draftLines,
                lineEditor = DraftLineEditorUiState(
                    isEditing = draft.lineEditor.editingLineId != null,
                    description = draft.lineEditor.description,
                    quantity = draft.lineEditor.quantity,
                    unitPrice = draft.lineEditor.unitPrice,
                    taxMode = draft.lineEditor.taxMode,
                    actionLabel = if (draft.lineEditor.editingLineId == null) {
                        "Anadir linea"
                    } else {
                        "Guardar cambios"
                    },
                ),
                subtotal = formatCurrency(totals.subtotal),
                taxTotal = formatCurrency(totals.taxTotal),
                grandTotal = formatCurrency(totals.grandTotal),
                hasPersistedDraft = draft.hasPersistedDraft,
            ),
            clientForm = currentClientForm,
            conceptForm = currentConceptForm,
            isPickingClientForDraft = isPickingClient,
            isPickingConceptForDraft = isPickingConcept,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LifacAppUiState(
            emitter = EmitterProfileUiState(),
            invoices = sampleInvoices,
            concepts = emptyList(),
            series = sampleSeries,
            draft = InvoiceDraftUiState(
                concepts = emptyList(),
                lineEditor = DraftLineEditorUiState(),
                subtotal = formatCurrency(0.0),
                taxTotal = formatCurrency(0.0),
                grandTotal = formatCurrency(0.0),
                hasPersistedDraft = false,
            ),
            clientForm = ClientFormUiState(),
            conceptForm = ConceptFormUiState(),
        ),
    )

    fun navigateTo(section: LifacSection) {
        currentSection.value = section
        if (section != LifacSection.CLIENTS) {
            pickingClientForDraft.value = false
        }
        if (section != LifacSection.CONCEPTS) {
            pickingConceptForDraft.value = false
        }
    }

    fun openNewInvoice() {
        pickingClientForDraft.value = false
        pickingConceptForDraft.value = false
        navigateTo(LifacSection.INVOICE_EDITOR)
    }

    fun leaveEditor() {
        pickingClientForDraft.value = false
        pickingConceptForDraft.value = false
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

    fun openConceptsForDraft() {
        pickingConceptForDraft.value = true
        navigateTo(LifacSection.CONCEPTS)
        viewModelScope.launch {
            events.emit("Selecciona un concepto guardado para anadirlo al borrador.")
        }
    }

    fun returnToDraftEditor() {
        pickingClientForDraft.value = false
        pickingConceptForDraft.value = false
        navigateTo(LifacSection.INVOICE_EDITOR)
    }

    fun updateClientKind(kind: ClientType) {
        clientForm.update { current -> current.copy(kind = kind) }
    }

    fun updateConceptName(value: String) {
        conceptForm.update { current -> current.copy(name = value) }
    }

    fun updateConceptDescription(value: String) {
        conceptForm.update { current -> current.copy(description = value) }
    }

    fun updateConceptUnitPrice(value: String) {
        conceptForm.update { current -> current.copy(unitPrice = value) }
    }

    fun updateConceptTaxMode(value: String) {
        conceptForm.update { current -> current.copy(taxMode = value) }
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

    fun updateDraftTaxMode(value: String) {
        draftForm.update { current ->
            current.copy(
                taxMode = value,
                lineEditor = current.lineEditor.copy(taxMode = value),
            )
        }
    }

    fun updateDraftLineDescription(value: String) {
        draftForm.update { current ->
            current.copy(lineEditor = current.lineEditor.copy(description = value))
        }
    }

    fun updateDraftLineQuantity(value: String) {
        draftForm.update { current ->
            current.copy(lineEditor = current.lineEditor.copy(quantity = value))
        }
    }

    fun updateDraftLineUnitPrice(value: String) {
        draftForm.update { current ->
            current.copy(lineEditor = current.lineEditor.copy(unitPrice = value))
        }
    }

    fun resetDraftLineEditor() {
        draftForm.update { current ->
            current.copy(lineEditor = defaultDraftLineEditor(current.taxMode))
        }
    }

    fun editDraftLine(lineId: String) {
        draftForm.update { current ->
            val selectedLine = current.lines.firstOrNull { it.id == lineId } ?: return@update current
            current.copy(
                taxMode = selectedLine.taxMode,
                lineEditor = DraftLineEditorFormState(
                    editingLineId = selectedLine.id,
                    description = selectedLine.description,
                    quantity = selectedLine.quantity,
                    unitPrice = selectedLine.unitPrice,
                    taxMode = selectedLine.taxMode,
                ),
            )
        }
    }

    fun removeDraftLine(lineId: String) {
        draftForm.update { current ->
            val remainingLines = current.lines
                .filterNot { it.id == lineId }
                .reindexed()
            val shouldResetEditor = current.lineEditor.editingLineId == lineId
            current.copy(
                lines = remainingLines,
                lineEditor = if (shouldResetEditor) {
                    defaultDraftLineEditor(current.taxMode)
                } else {
                    current.lineEditor
                },
            )
        }
        viewModelScope.launch {
            events.emit("Linea eliminada del borrador.")
        }
    }

    fun useConceptInDraft(conceptId: String) {
        val selectedConcept = uiState.value.concepts.firstOrNull { it.id == conceptId }
        if (selectedConcept == null) {
            viewModelScope.launch {
                events.emit("No se encontro el concepto seleccionado.")
            }
            return
        }

        draftForm.update { current ->
            val newLine = DraftLineFormState(
                id = UUID.randomUUID().toString(),
                sortOrder = 0,
                description = selectedConcept.description.ifBlank { selectedConcept.name },
                quantity = "1",
                unitPrice = selectedConcept.price.removeSuffix(" EUR"),
                taxMode = selectedConcept.taxLabel,
            )
            current.copy(
                taxMode = selectedConcept.taxLabel,
                lines = (current.lines + newLine).reindexed(),
                lineEditor = defaultDraftLineEditor(selectedConcept.taxLabel),
            )
        }
        pickingConceptForDraft.value = false
        navigateTo(LifacSection.INVOICE_EDITOR)
        viewModelScope.launch {
            events.emit("Concepto anadido al borrador.")
        }
    }

    fun saveDraftLine() {
        val normalizedState = draftForm.value.normalized()
        val lineValidationError = validateDraftLineEditor(normalizedState.lineEditor)

        if (lineValidationError != null) {
            viewModelScope.launch {
                events.emit(lineValidationError)
            }
            return
        }

        draftForm.value = run {
            val lineEditor = normalizedState.lineEditor
            val draftLine = DraftLineFormState(
                id = lineEditor.editingLineId ?: UUID.randomUUID().toString(),
                sortOrder = 0,
                description = lineEditor.description,
                quantity = lineEditor.quantity,
                unitPrice = lineEditor.unitPrice,
                taxMode = lineEditor.taxMode,
            )
            val mergedLines = if (lineEditor.editingLineId == null) {
                normalizedState.lines + draftLine
            } else {
                normalizedState.lines.map { existingLine ->
                    if (existingLine.id == lineEditor.editingLineId) draftLine else existingLine
                }
            }.reindexed()

            normalizedState.copy(
                taxMode = lineEditor.taxMode,
                lines = mergedLines,
                lineEditor = defaultDraftLineEditor(lineEditor.taxMode),
            )
        }

        viewModelScope.launch {
            events.emit("Linea guardada en el borrador.")
        }
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

    fun resetConceptForm() {
        conceptForm.value = ConceptFormUiState(taxMode = conceptForm.value.taxMode)
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

    fun saveConcept() {
        val normalizedForm = conceptForm.value.normalized()
        val validationError = validateConceptForm(normalizedForm)

        if (validationError != null) {
            viewModelScope.launch {
                events.emit(validationError)
            }
            return
        }

        viewModelScope.launch {
            conceptRepository.upsertConcept(
                StoredConcept(
                    id = UUID.randomUUID().toString(),
                    name = normalizedForm.name,
                    description = normalizedForm.description,
                    unitPrice = normalizedForm.unitPrice,
                    taxMode = normalizedForm.taxMode,
                ),
            )
            conceptForm.value = ConceptFormUiState(taxMode = normalizedForm.taxMode)
            events.emit("Concepto guardado localmente.")
        }
    }

    fun saveDraft() {
        val draftToSave = draftForm.value.normalized()

        viewModelScope.launch {
            draftRepository.upsertActiveDraft(
                draft = draftToSave.toStoredDraft(),
                lines = draftToSave.toStoredDraftLines(),
            )
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
                        conceptRepository = RoomConceptRepository.from(appContext),
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

        private val sampleSeries = listOf(
            SeriesListItemUiState(code = "2026", nextNumber = "0008"),
            SeriesListItemUiState(code = "OBRA", nextNumber = "0012"),
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
    val lines: List<DraftLineFormState> = emptyList(),
    val lineEditor: DraftLineEditorFormState = defaultDraftLineEditor(),
    val hasPersistedDraft: Boolean = false,
)

internal data class DraftLineFormState(
    val id: String,
    val sortOrder: Int,
    val description: String,
    val quantity: String,
    val unitPrice: String,
    val taxMode: String,
)

internal data class DraftLineEditorFormState(
    val editingLineId: String? = null,
    val description: String = "",
    val quantity: String = "1",
    val unitPrice: String = "",
    val taxMode: String = "IVA 21%",
)

private data class DraftTotals(
    val subtotal: Double,
    val taxTotal: Double,
    val grandTotal: Double,
)

private data class UiBaseState(
    val section: LifacSection,
    val storedClients: List<StoredClient>,
    val storedConcepts: List<StoredConcept>,
    val currentClientForm: ClientFormUiState,
    val currentConceptForm: ConceptFormUiState,
)

private fun defaultDraftForm(): InvoiceDraftFormState = InvoiceDraftFormState()

private fun defaultDraftLineEditor(taxMode: String = "IVA 21%"): DraftLineEditorFormState {
    return DraftLineEditorFormState(taxMode = taxMode)
}

internal fun validateClientForm(form: ClientFormUiState): String? {
    return if (form.displayName.isBlank()) {
        "El nombre del cliente es obligatorio."
    } else {
        null
    }
}

internal fun validateConceptForm(form: ConceptFormUiState): String? {
    return when {
        form.name.isBlank() -> "El nombre del concepto es obligatorio."
        form.description.isBlank() -> "La descripcion del concepto es obligatoria."
        else -> null
    }
}

internal fun validateDraftLineEditor(editor: DraftLineEditorFormState): String? {
    return when {
        editor.description.isBlank() -> "La descripcion de la linea es obligatoria."
        else -> null
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

private fun ConceptFormUiState.normalized(): ConceptFormUiState {
    return copy(
        name = name.trim(),
        description = description.trim(),
        unitPrice = unitPrice.trim(),
    )
}

private fun InvoiceDraftFormState.normalized(): InvoiceDraftFormState {
    return copy(
        issueDate = issueDate.trim(),
        operationDate = operationDate.trim(),
        projectLabel = projectLabel.trim(),
        notes = notes.trim(),
        lines = lines
            .map { line ->
                line.copy(
                    description = line.description.trim(),
                    quantity = line.quantity.trim(),
                    unitPrice = line.unitPrice.trim(),
                )
            }
            .reindexed(),
        lineEditor = lineEditor.copy(
            description = lineEditor.description.trim(),
            quantity = lineEditor.quantity.trim(),
            unitPrice = lineEditor.unitPrice.trim(),
            taxMode = lineEditor.taxMode.trim(),
        ),
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

private fun InvoiceDraftFormState.toStoredDraftLines(): List<StoredDraftLine> {
    return lines
        .reindexed()
        .map { line ->
            StoredDraftLine(
                id = line.id,
                sortOrder = line.sortOrder,
                description = line.description,
                quantity = line.quantity,
                unitPrice = line.unitPrice,
                taxMode = line.taxMode,
            )
        }
}

internal fun StoredDraftBundle.toDraftFormState(): InvoiceDraftFormState {
    val normalizedLines = lines
        .sortedBy { it.sortOrder }
        .map { line ->
            DraftLineFormState(
                id = line.id,
                sortOrder = line.sortOrder,
                description = line.description,
                quantity = line.quantity,
                unitPrice = line.unitPrice,
                taxMode = line.taxMode,
            )
        }
        .reindexed()
    val lastTaxMode = normalizedLines.lastOrNull()?.taxMode ?: draft.taxMode

    return InvoiceDraftFormState(
        selectedClientId = draft.selectedClientId,
        selectedSeries = draft.selectedSeries,
        nextNumberPreview = draft.nextNumberPreview,
        issueDate = draft.issueDate,
        operationDate = draft.operationDate,
        projectLabel = draft.projectLabel,
        notes = draft.notes,
        taxMode = draft.taxMode,
        lines = normalizedLines,
        lineEditor = defaultDraftLineEditor(lastTaxMode),
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

private fun DraftLineFormState.toUiState(): DraftConceptUiState {
    val totals = calculateLineTotals(this)
    return DraftConceptUiState(
        id = id,
        description = description,
        quantity = quantity,
        unitPrice = unitPrice.ifBlank { "0,00" },
        taxLabel = taxMode,
        total = formatCurrency(totals.lineTotal),
    )
}

private fun calculateDraftTotals(lines: List<DraftLineFormState>): DraftTotals {
    val lineTotals = lines.map(::calculateLineTotals)
    return DraftTotals(
        subtotal = lineTotals.sumOf { it.baseAmount },
        taxTotal = lineTotals.sumOf { it.taxAmount },
        grandTotal = lineTotals.sumOf { it.lineTotal },
    )
}

private data class DraftLineTotals(
    val baseAmount: Double,
    val taxAmount: Double,
    val lineTotal: Double,
)

private fun calculateLineTotals(line: DraftLineFormState): DraftLineTotals {
    val quantity = parseDecimal(line.quantity).takeIf { it > 0 } ?: 0.0
    val unitPrice = parseDecimal(line.unitPrice)
    val baseAmount = quantity * unitPrice
    val taxAmount = baseAmount * taxRateFor(line.taxMode)
    return DraftLineTotals(
        baseAmount = baseAmount,
        taxAmount = taxAmount,
        lineTotal = baseAmount + taxAmount,
    )
}

private fun parseDecimal(rawValue: String): Double {
    val trimmed = rawValue.trim()
    val normalized = if (trimmed.contains(',')) {
        trimmed
            .replace(".", "")
            .replace(',', '.')
    } else {
        trimmed
    }
    return normalized.toDoubleOrNull() ?: 0.0
}

private fun taxRateFor(taxMode: String): Double {
    return when (taxMode) {
        "IVA 10%" -> 0.10
        "IVA 21%" -> 0.21
        else -> 0.0
    }
}

private fun formatCurrency(value: Double): String {
    return String.format(Locale("es", "ES"), "%.2f EUR", value).replace('.', ',')
}

private fun formatConceptPrice(rawValue: String): String {
    val trimmed = rawValue.trim()
    return if (trimmed.isBlank()) {
        "0,00 EUR"
    } else if (trimmed.endsWith("EUR")) {
        trimmed
    } else {
        "$trimmed EUR"
    }
}

private fun List<DraftLineFormState>.reindexed(): List<DraftLineFormState> {
    return mapIndexed { index, line -> line.copy(sortOrder = index) }
}

private fun clientKindLabel(kind: ClientKind): String = when (kind) {
    ClientKind.BUSINESS -> "Empresa"
    ClientKind.INDIVIDUAL -> "Particular"
}
