package io.github.eldiablo45.lifac.ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.eldiablo45.lifac.data.client.ClientType
import io.github.eldiablo45.lifac.ui.theme.LifacTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifacAppScreen(
    uiState: LifacAppUiState,
    snackbarHostState: SnackbarHostState,
    onNavigate: (LifacSection) -> Unit,
    onOpenNewInvoice: () -> Unit,
    onOpenSavedInvoice: (String) -> Unit,
    onBackFromEditor: () -> Unit,
    onClientKindSelected: (ClientType) -> Unit,
    onClientDisplayNameChanged: (String) -> Unit,
    onClientTaxIdChanged: (String) -> Unit,
    onClientCityChanged: (String) -> Unit,
    onClientAddressChanged: (String) -> Unit,
    onClientNotesChanged: (String) -> Unit,
    onSaveClient: () -> Unit,
    onResetClientForm: () -> Unit,
    onConceptNameChanged: (String) -> Unit,
    onConceptDescriptionChanged: (String) -> Unit,
    onConceptUnitPriceChanged: (String) -> Unit,
    onConceptTaxModeChanged: (String) -> Unit,
    onSaveConcept: () -> Unit,
    onResetConceptForm: () -> Unit,
    onDraftClientSelected: (String) -> Unit,
    onPickClientForDraft: (String) -> Unit,
    onOpenClientsForDraft: () -> Unit,
    onUseConceptForDraft: (String) -> Unit,
    onOpenConceptsForDraft: () -> Unit,
    onReturnToDraftEditor: () -> Unit,
    onDraftIssueDateChanged: (String) -> Unit,
    onDraftOperationDateChanged: (String) -> Unit,
    onDraftProjectLabelChanged: (String) -> Unit,
    onDraftTaxModeChanged: (String) -> Unit,
    onDraftNotesChanged: (String) -> Unit,
    onDraftLineDescriptionChanged: (String) -> Unit,
    onDraftLineQuantityChanged: (String) -> Unit,
    onDraftLineUnitPriceChanged: (String) -> Unit,
    onSaveDraftLine: () -> Unit,
    onEditDraftLine: (String) -> Unit,
    onRemoveDraftLine: (String) -> Unit,
    onResetDraftLineEditor: () -> Unit,
    onSaveInvoice: () -> Unit,
    onSaveDraft: () -> Unit,
    onPlaceholderAction: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.currentSection.title,
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    if (uiState.currentSection == LifacSection.INVOICE_EDITOR) {
                        Text(
                            text = "Atras",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .clickable(onClick = onBackFromEditor),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            )
        },
        bottomBar = {
            if (uiState.currentSection.visibleInBottomBar) {
                NavigationBar {
                    LifacSection.entries
                        .filter { it.visibleInBottomBar }
                        .forEach { section ->
                            NavigationBarItem(
                                selected = section == uiState.currentSection,
                                onClick = { onNavigate(section) },
                                icon = {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(MaterialTheme.shapes.small)
                                            .background(
                                                if (section == uiState.currentSection) {
                                                    MaterialTheme.colorScheme.primary
                                                } else {
                                                    MaterialTheme.colorScheme.outlineVariant
                                                },
                                            ),
                                    )
                                },
                                label = {
                                    Text(text = section.title)
                                },
                            )
                        }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
        ) {
            when (uiState.currentSection) {
                LifacSection.HOME -> HomeScreen(
                    uiState = uiState,
                    onOpenNewInvoice = onOpenNewInvoice,
                    onNavigate = onNavigate,
                    modifier = Modifier.fillMaxSize(),
                )

                LifacSection.INVOICES -> InvoicesScreen(
                    invoices = uiState.invoices,
                    onOpenNewInvoice = onOpenNewInvoice,
                    onOpenSavedInvoice = onOpenSavedInvoice,
                    onPlaceholderAction = onPlaceholderAction,
                    modifier = Modifier.fillMaxSize(),
                )

                LifacSection.CLIENTS -> ClientsScreen(
                    clients = uiState.clients,
                    form = uiState.clientForm,
                    onClientKindSelected = onClientKindSelected,
                    onClientDisplayNameChanged = onClientDisplayNameChanged,
                    onClientTaxIdChanged = onClientTaxIdChanged,
                    onClientCityChanged = onClientCityChanged,
                    onClientAddressChanged = onClientAddressChanged,
                    onClientNotesChanged = onClientNotesChanged,
                    onSaveClient = onSaveClient,
                    onResetClientForm = onResetClientForm,
                    isPickingClientForDraft = uiState.isPickingClientForDraft,
                    onPickClientForDraft = onPickClientForDraft,
                    onReturnToDraftEditor = onReturnToDraftEditor,
                    onPlaceholderAction = onPlaceholderAction,
                    modifier = Modifier.fillMaxSize(),
                )

                LifacSection.CONCEPTS -> ConceptsScreen(
                    concepts = uiState.concepts,
                    form = uiState.conceptForm,
                    isPickingConceptForDraft = uiState.isPickingConceptForDraft,
                    onConceptNameChanged = onConceptNameChanged,
                    onConceptDescriptionChanged = onConceptDescriptionChanged,
                    onConceptUnitPriceChanged = onConceptUnitPriceChanged,
                    onConceptTaxModeChanged = onConceptTaxModeChanged,
                    onSaveConcept = onSaveConcept,
                    onResetConceptForm = onResetConceptForm,
                    onUseConceptForDraft = onUseConceptForDraft,
                    onReturnToDraftEditor = onReturnToDraftEditor,
                    onPlaceholderAction = onPlaceholderAction,
                    modifier = Modifier.fillMaxSize(),
                )

                LifacSection.SETTINGS -> SettingsScreen(
                    emitter = uiState.emitter,
                    series = uiState.series,
                    onPlaceholderAction = onPlaceholderAction,
                    modifier = Modifier.fillMaxSize(),
                )

                LifacSection.INVOICE_EDITOR -> InvoiceEditorScreen(
                    draft = uiState.draft,
                    clients = uiState.clients,
                    onDraftClientSelected = onDraftClientSelected,
                    onOpenClientsForDraft = onOpenClientsForDraft,
                    onOpenConceptsForDraft = onOpenConceptsForDraft,
                    onDraftIssueDateChanged = onDraftIssueDateChanged,
                    onDraftOperationDateChanged = onDraftOperationDateChanged,
                    onDraftProjectLabelChanged = onDraftProjectLabelChanged,
                    onDraftTaxModeChanged = onDraftTaxModeChanged,
                    onDraftNotesChanged = onDraftNotesChanged,
                    onDraftLineDescriptionChanged = onDraftLineDescriptionChanged,
                    onDraftLineQuantityChanged = onDraftLineQuantityChanged,
                    onDraftLineUnitPriceChanged = onDraftLineUnitPriceChanged,
                    onSaveDraftLine = onSaveDraftLine,
                    onEditDraftLine = onEditDraftLine,
                    onRemoveDraftLine = onRemoveDraftLine,
                    onResetDraftLineEditor = onResetDraftLineEditor,
                    onSaveInvoice = onSaveInvoice,
                    onSaveDraft = onSaveDraft,
                    onPlaceholderAction = onPlaceholderAction,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(
    uiState: LifacAppUiState,
    onOpenNewInvoice: () -> Unit,
    onNavigate: (LifacSection) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Facturacion simple, local y lista para PDF",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Este primer corte ya deja ver el flujo principal: cliente, datos de factura, conceptos, impuestos y PDF.",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FilledTonalButton(onClick = onOpenNewInvoice) {
                            Text("Nueva factura")
                        }
                        OutlinedButton(onClick = { onNavigate(LifacSection.INVOICES) }) {
                            Text("Ver facturas")
                        }
                    }
                }
            }
        }

        item {
            SectionTitle("Accesos rapidos")
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuickAccessChip("Clientes") { onNavigate(LifacSection.CLIENTS) }
                    QuickAccessChip("Conceptos") { onNavigate(LifacSection.CONCEPTS) }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuickAccessChip("Ajustes") { onNavigate(LifacSection.SETTINGS) }
                    QuickAccessChip("Series") { onNavigate(LifacSection.SETTINGS) }
                }
            }
        }

        item {
            SectionTitle("Facturas recientes")
        }

        items(uiState.invoices.take(3), key = { it.id }) { invoice ->
            InvoiceRowCard(
                invoice = invoice,
                onClick = { onNavigate(LifacSection.INVOICES) },
            )
        }

        item {
            ElevatedCard {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = "Siguiente iteracion sugerida",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Abrir facturas guardadas desde historial o generar PDF real sobre el modelo persistido actual.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun InvoicesScreen(
    invoices: List<InvoiceListItemUiState>,
    onOpenNewInvoice: () -> Unit,
    onOpenSavedInvoice: (String) -> Unit,
    onPlaceholderAction: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ElevatedCard {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Historial local",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Este listado ya se alimenta de facturas guardadas localmente desde el borrador.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FilledTonalButton(onClick = onOpenNewInvoice) {
                            Text("Nueva factura")
                        }
                        OutlinedButton(
                            onClick = {
                                onPlaceholderAction("Busqueda y filtros detallados quedan para la siguiente iteracion.")
                            },
                        ) {
                            Text("Filtrar")
                        }
                    }
                }
            }
        }

        if (invoices.isEmpty()) {
            item {
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "Aun no hay facturas guardadas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "Guarda la primera desde Nueva factura para empezar a construir historial real.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }

        items(invoices, key = { it.id }) { invoice ->
            InvoiceRowCard(
                invoice = invoice,
                onClick = { onOpenSavedInvoice(invoice.id) },
            )
        }
    }
}

@Composable
private fun ClientsScreen(
    clients: List<ClientListItemUiState>,
    form: ClientFormUiState,
    onClientKindSelected: (ClientType) -> Unit,
    onClientDisplayNameChanged: (String) -> Unit,
    onClientTaxIdChanged: (String) -> Unit,
    onClientCityChanged: (String) -> Unit,
    onClientAddressChanged: (String) -> Unit,
    onClientNotesChanged: (String) -> Unit,
    onSaveClient: () -> Unit,
    onResetClientForm: () -> Unit,
    isPickingClientForDraft: Boolean,
    onPickClientForDraft: (String) -> Unit,
    onReturnToDraftEditor: () -> Unit,
    onPlaceholderAction: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ElevatedCard {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Clientes empresa y particular",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Esta ya es la primera parte real de la app: los clientes se guardan localmente en el dispositivo. Los campos dudosos se mantienen opcionales por ahora.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    if (isPickingClientForDraft) {
                        Spacer(modifier = Modifier.height(8.dp))
                        FilledTonalButton(onClick = onReturnToDraftEditor) {
                            Text("Volver a nueva factura")
                        }
                    }
                }
            }
        }

        item {
            ElevatedCard {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Nuevo cliente",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = form.kind == ClientType.BUSINESS,
                            onClick = { onClientKindSelected(ClientType.BUSINESS) },
                            label = { Text("Empresa") },
                        )
                        FilterChip(
                            selected = form.kind == ClientType.INDIVIDUAL,
                            onClick = { onClientKindSelected(ClientType.INDIVIDUAL) },
                            label = { Text("Particular") },
                        )
                    }
                    OutlinedTextField(
                        value = form.displayName,
                        onValueChange = onClientDisplayNameChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                if (form.kind == ClientType.BUSINESS) {
                                    "Nombre fiscal o razon social"
                                } else {
                                    "Nombre del cliente"
                                },
                            )
                        },
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = form.taxId,
                        onValueChange = onClientTaxIdChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("NIF o identificador fiscal") },
                        placeholder = { Text("Placeholder: opcional por ahora") },
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = form.city,
                        onValueChange = onClientCityChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Ciudad") },
                        placeholder = { Text("Placeholder: ayuda para encontrarlo luego") },
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = form.address,
                        onValueChange = onClientAddressChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Direccion") },
                        placeholder = { Text("Placeholder: opcional hasta cerrar el formulario final") },
                    )
                    OutlinedTextField(
                        value = form.notes,
                        onValueChange = onClientNotesChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Notas") },
                        placeholder = { Text("Placeholder: obra, referencia o comentario") },
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FilledTonalButton(onClick = onSaveClient) {
                            Text("Guardar cliente")
                        }
                        OutlinedButton(onClick = onResetClientForm) {
                            Text("Limpiar")
                        }
                    }
                }
            }
        }

        if (clients.isEmpty()) {
            item {
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "Aun no hay clientes guardados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "Usa el formulario superior para crear el primero. Quedara persistido localmente en este dispositivo.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }

        items(clients, key = { it.id }) { client ->
            ElevatedCard(
                onClick = {
                    if (isPickingClientForDraft) {
                        onPickClientForDraft(client.id)
                    } else {
                        onPlaceholderAction("Edicion detallada de cliente pendiente de la siguiente iteracion.")
                    }
                },
            ) {
                ListItem(
                    headlineContent = { Text(client.displayName) },
                    supportingContent = {
                        Text(
                            text = buildString {
                                append(clientKindLabel(client.kind))
                                append(" · ")
                                append(client.taxId)
                                append(" · ")
                                append(client.city)
                                if (client.address.isNotBlank()) {
                                    append(" · ")
                                    append(client.address)
                                }
                            },
                        )
                    },
                    trailingContent = {
                        if (isPickingClientForDraft) {
                            Text(
                                text = "Seleccionar",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        } else {
                            AssistChip(
                                onClick = {},
                                label = { Text(clientKindLabel(client.kind)) },
                            )
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun ConceptsScreen(
    concepts: List<ConceptListItemUiState>,
    form: ConceptFormUiState,
    isPickingConceptForDraft: Boolean,
    onConceptNameChanged: (String) -> Unit,
    onConceptDescriptionChanged: (String) -> Unit,
    onConceptUnitPriceChanged: (String) -> Unit,
    onConceptTaxModeChanged: (String) -> Unit,
    onSaveConcept: () -> Unit,
    onResetConceptForm: () -> Unit,
    onUseConceptForDraft: (String) -> Unit,
    onReturnToDraftEditor: () -> Unit,
    onPlaceholderAction: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ElevatedCard {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Catalogo de conceptos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Los conceptos ya se guardan en local y pueden reutilizarse para acelerar nuevas facturas.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    if (isPickingConceptForDraft) {
                        Spacer(modifier = Modifier.height(8.dp))
                        FilledTonalButton(onClick = onReturnToDraftEditor) {
                            Text("Volver a nueva factura")
                        }
                    }
                }
            }
        }

        item {
            ElevatedCard {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Nuevo concepto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    OutlinedTextField(
                        value = form.name,
                        onValueChange = onConceptNameChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nombre corto") },
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = form.description,
                        onValueChange = onConceptDescriptionChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Descripcion por defecto") },
                        placeholder = { Text("Placeholder: texto que ira a la linea de factura") },
                    )
                    OutlinedTextField(
                        value = form.unitPrice,
                        onValueChange = onConceptUnitPriceChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Precio por defecto") },
                        placeholder = { Text("Ejemplo: 220,00") },
                        singleLine = true,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = form.taxMode == "IVA 21%",
                            onClick = { onConceptTaxModeChanged("IVA 21%") },
                            label = { Text("IVA 21%") },
                        )
                        FilterChip(
                            selected = form.taxMode == "IVA 10%",
                            onClick = { onConceptTaxModeChanged("IVA 10%") },
                            label = { Text("IVA 10%") },
                        )
                        FilterChip(
                            selected = form.taxMode == "ISP",
                            onClick = { onConceptTaxModeChanged("ISP") },
                            label = { Text("ISP") },
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FilledTonalButton(onClick = onSaveConcept) {
                            Text("Guardar concepto")
                        }
                        OutlinedButton(onClick = onResetConceptForm) {
                            Text("Limpiar")
                        }
                    }
                }
            }
        }

        if (concepts.isEmpty()) {
            item {
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "Aun no hay conceptos guardados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "Crea el primero para reutilizarlo luego al generar facturas.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }

        items(concepts, key = { it.id }) { concept ->
            ElevatedCard(
                onClick = {
                    if (isPickingConceptForDraft) {
                        onUseConceptForDraft(concept.id)
                    } else {
                        onPlaceholderAction("La edicion detallada de conceptos llegara en una siguiente iteracion.")
                    }
                },
            ) {
                ListItem(
                    headlineContent = { Text(concept.name) },
                    supportingContent = {
                        Text("${concept.description} · ${concept.price} · ${concept.taxLabel}")
                    },
                    trailingContent = {
                        Text(
                            text = if (isPickingConceptForDraft) "Usar" else "Disponible",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    emitter: EmitterProfileUiState,
    series: List<SeriesListItemUiState>,
    onPlaceholderAction: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ElevatedCard {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Datos del emisor",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    SettingsField("Razon social", emitter.businessName)
                    SettingsField("NIF", emitter.taxId)
                    SettingsField("Direccion fiscal", emitter.fiscalAddress)
                    SettingsField("Municipio", emitter.city)
                    SettingsField("Email", emitter.email)
                }
            }
        }

        item {
            ElevatedCard {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Series",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    series.forEach { seriesItem ->
                        SettingsField(
                            label = "Serie ${seriesItem.code}",
                            value = "Siguiente numero ${seriesItem.nextNumber}",
                        )
                    }
                }
            }
        }

        item {
            ElevatedCard(
                onClick = {
                    onPlaceholderAction("Backup local y Google Drive se definiran cuando exista persistencia real.")
                },
            ) {
                ListItem(
                    headlineContent = { Text("Backup futuro") },
                    supportingContent = {
                        Text("Placeholder para exportacion local y posible copia voluntaria en Google Drive.")
                    },
                )
            }
        }
    }
}

@Composable
private fun InvoiceEditorScreen(
    draft: InvoiceDraftUiState,
    clients: List<ClientListItemUiState>,
    onDraftClientSelected: (String) -> Unit,
    onOpenClientsForDraft: () -> Unit,
    onOpenConceptsForDraft: () -> Unit,
    onDraftIssueDateChanged: (String) -> Unit,
    onDraftOperationDateChanged: (String) -> Unit,
    onDraftProjectLabelChanged: (String) -> Unit,
    onDraftTaxModeChanged: (String) -> Unit,
    onDraftNotesChanged: (String) -> Unit,
    onDraftLineDescriptionChanged: (String) -> Unit,
    onDraftLineQuantityChanged: (String) -> Unit,
    onDraftLineUnitPriceChanged: (String) -> Unit,
    onSaveDraftLine: () -> Unit,
    onEditDraftLine: (String) -> Unit,
    onRemoveDraftLine: (String) -> Unit,
    onResetDraftLineEditor: () -> Unit,
    onSaveInvoice: () -> Unit,
    onSaveDraft: () -> Unit,
    onPlaceholderAction: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = "Editor de factura",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Este flujo ya combina cliente real y borrador local, mientras conceptos, impuestos avanzados y PDF siguen en evolucion.",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    if (draft.hasPersistedDraft) {
                        Text(
                            text = "Borrador local cargado",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }

        item {
            EditorSectionCard(
                title = "1. Cliente",
                subtitle = "Empresa o particular dentro del mismo flujo.",
            ) {
                EditorField("Cliente", draft.selectedClientLabel)
                EditorField("Resumen", draft.selectedClientMeta)
                Spacer(modifier = Modifier.height(8.dp))
                if (clients.isEmpty()) {
                    PlaceholderTag("No hay clientes guardados todavia.")
                    Spacer(modifier = Modifier.height(8.dp))
                    FilledTonalButton(onClick = onOpenClientsForDraft) {
                        Text("Ir a clientes")
                    }
                } else {
                    Text(
                        text = "Seleccion rápida",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        clients.forEach { client ->
                            FilterChip(
                                selected = draft.selectedClientId == client.id,
                                onClick = { onDraftClientSelected(client.id) },
                                label = {
                                    Text(
                                        text = buildString {
                                            append(client.displayName)
                                            append(" · ")
                                            append(clientKindLabel(client.kind))
                                        },
                                    )
                                },
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = onOpenClientsForDraft) {
                        Text("Gestionar clientes")
                    }
                }
            }
        }

        item {
            EditorSectionCard(
                title = "2. Datos de factura",
                subtitle = "Serie y numeracion automatica desde el primer dia.",
            ) {
                EditorField("Serie", draft.selectedSeries)
                EditorField("Numero autogenerado", draft.nextNumberPreview)
                OutlinedTextField(
                    value = draft.issueDate,
                    onValueChange = onDraftIssueDateChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Fecha de emision") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = draft.operationDate,
                    onValueChange = onDraftOperationDateChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Fecha de operacion") },
                    placeholder = { Text("Placeholder: misma fecha por ahora") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = draft.projectLabel,
                    onValueChange = onDraftProjectLabelChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Obra o referencia") },
                    placeholder = { Text("Placeholder: nombre de obra o referencia") },
                )
            }
        }

        item {
            EditorSectionCard(
                title = "3. Conceptos",
                subtitle = "Las lineas ya se editan en local dentro del borrador activo.",
            ) {
                if (draft.concepts.isEmpty()) {
                    PlaceholderTag("Aun no hay lineas en este borrador.")
                } else {
                    draft.concepts.forEachIndexed { index, concept ->
                        if (index > 0) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        }
                        EditorField("Descripcion", concept.description)
                        EditorField("Cantidad", concept.quantity)
                        EditorField("Precio unitario", concept.unitPrice)
                        EditorField("Impuesto", concept.taxLabel)
                        EditorField("Total linea", concept.total)
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FilledTonalButton(onClick = { onEditDraftLine(concept.id) }) {
                                Text("Editar")
                            }
                            OutlinedButton(onClick = { onRemoveDraftLine(concept.id) }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (draft.lineEditor.isEditing) {
                        "Editar linea"
                    } else {
                        "Nueva linea"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                OutlinedTextField(
                    value = draft.lineEditor.description,
                    onValueChange = onDraftLineDescriptionChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Descripcion") },
                    placeholder = { Text("Placeholder: mano de obra, material o servicio") },
                )
                OutlinedTextField(
                    value = draft.lineEditor.quantity,
                    onValueChange = onDraftLineQuantityChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Cantidad") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = draft.lineEditor.unitPrice,
                    onValueChange = onDraftLineUnitPriceChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Precio unitario") },
                    placeholder = { Text("Ejemplo: 220,00") },
                    singleLine = true,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = draft.lineEditor.taxMode == "IVA 21%",
                        onClick = { onDraftTaxModeChanged("IVA 21%") },
                        label = { Text("IVA 21%") },
                    )
                    FilterChip(
                        selected = draft.lineEditor.taxMode == "IVA 10%",
                        onClick = { onDraftTaxModeChanged("IVA 10%") },
                        label = { Text("IVA 10%") },
                    )
                    FilterChip(
                        selected = draft.lineEditor.taxMode == "ISP",
                        onClick = { onDraftTaxModeChanged("ISP") },
                        label = { Text("ISP") },
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilledTonalButton(onClick = onSaveDraftLine) {
                        Text(draft.lineEditor.actionLabel)
                    }
                    OutlinedButton(onClick = onOpenConceptsForDraft) {
                        Text("Usar guardado")
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = onResetDraftLineEditor) {
                        Text("Limpiar linea")
                    }
                }
            }
        }

        item {
            EditorSectionCard(
                title = "4. Impuestos y observaciones",
                subtitle = "Modo fiscal por defecto del borrador y resumen calculado.",
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = draft.taxMode == "IVA 21%",
                        onClick = { onDraftTaxModeChanged("IVA 21%") },
                        label = { Text("IVA 21%") },
                    )
                    FilterChip(
                        selected = draft.taxMode == "IVA 10%",
                        onClick = { onDraftTaxModeChanged("IVA 10%") },
                        label = { Text("IVA 10%") },
                    )
                    FilterChip(
                        selected = draft.taxMode == "ISP",
                        onClick = { onDraftTaxModeChanged("ISP") },
                        label = { Text("ISP") },
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                EditorField("Subtotal", draft.subtotal)
                EditorField("Impuestos", draft.taxTotal)
                EditorField("Total", draft.grandTotal)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = draft.notes,
                    onValueChange = onDraftNotesChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Observaciones") },
                    placeholder = { Text("Placeholder: nota legal o comentario final") },
                )
            }
        }

        item {
            ElevatedCard {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "5. Acciones finales",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FilledTonalButton(onClick = onSaveInvoice) {
                            Text("Guardar factura")
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FilledTonalButton(onClick = onSaveDraft) {
                            Text("Guardar borrador")
                        }
                        OutlinedButton(
                            onClick = {
                                onPlaceholderAction("La generacion real de PDF se apoyara en estas lineas y totales persistidos.")
                            },
                        ) {
                            Text("Generar PDF")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InvoiceRowCard(
    invoice: InvoiceListItemUiState,
    onClick: () -> Unit,
) {
    ElevatedCard(onClick = onClick) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = invoice.number,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = invoice.status,
                    color = if (invoice.status == "Emitida") {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    },
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Text(
                text = invoice.clientName,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "${invoice.date} · ${invoice.total}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun EditorSectionCard(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit,
) {
    ElevatedCard {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            content()
        }
    }
}

@Composable
private fun EditorField(
    label: String,
    value: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun SettingsField(
    label: String,
    value: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun PlaceholderTag(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.secondary,
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun QuickAccessChip(
    label: String,
    onClick: () -> Unit,
) {
    AssistChip(
        onClick = onClick,
        label = { Text(label) },
    )
}

private fun clientKindLabel(kind: ClientKind): String = when (kind) {
    ClientKind.BUSINESS -> "Empresa"
    ClientKind.INDIVIDUAL -> "Particular"
}

@Preview(showBackground = true)
@Composable
private fun LifacAppScreenPreview() {
    LifacTheme {
        LifacAppScreen(
            uiState = LifacAppUiState(
                emitter = EmitterProfileUiState(),
                invoices = listOf(
                    InvoiceListItemUiState(
                        id = "preview",
                        number = "2026-0008",
                        clientName = "Promociones Sierra Norte",
                        status = "Emitida",
                        total = "1.452,00 EUR",
                        date = "19/04/2026",
                    ),
                ),
                clients = listOf(
                    ClientListItemUiState(
                        id = "client",
                        displayName = "Cliente de muestra",
                        kind = ClientKind.BUSINESS,
                        taxId = "B00000000",
                        city = "Madrid",
                        address = "Calle Mayor 1",
                        notes = "",
                    ),
                ),
                concepts = emptyList(),
                series = emptyList(),
                draft = InvoiceDraftUiState(
                    selectedClientId = "client",
                    selectedClientLabel = "Cliente de muestra",
                    selectedClientMeta = "Empresa · B00000000 · Madrid",
                    concepts = listOf(
                        DraftConceptUiState(
                            id = "line-preview",
                            description = "Demolicion interior",
                            quantity = "1",
                            unitPrice = "600,00 EUR",
                            taxLabel = "IVA 21%",
                            total = "726,00 EUR",
                        ),
                    ),
                ),
                clientForm = ClientFormUiState(),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onNavigate = {},
            onOpenNewInvoice = {},
            onOpenSavedInvoice = {},
            onBackFromEditor = {},
            onClientKindSelected = {},
            onClientDisplayNameChanged = {},
            onClientTaxIdChanged = {},
            onClientCityChanged = {},
            onClientAddressChanged = {},
            onClientNotesChanged = {},
            onSaveClient = {},
            onResetClientForm = {},
            onConceptNameChanged = {},
            onConceptDescriptionChanged = {},
            onConceptUnitPriceChanged = {},
            onConceptTaxModeChanged = {},
            onSaveConcept = {},
            onResetConceptForm = {},
            onDraftClientSelected = {},
            onPickClientForDraft = {},
            onOpenClientsForDraft = {},
            onUseConceptForDraft = {},
            onOpenConceptsForDraft = {},
            onReturnToDraftEditor = {},
            onDraftIssueDateChanged = {},
            onDraftOperationDateChanged = {},
            onDraftProjectLabelChanged = {},
            onDraftTaxModeChanged = {},
            onDraftNotesChanged = {},
            onDraftLineDescriptionChanged = {},
            onDraftLineQuantityChanged = {},
            onDraftLineUnitPriceChanged = {},
            onSaveDraftLine = {},
            onEditDraftLine = {},
            onRemoveDraftLine = {},
            onResetDraftLineEditor = {},
            onSaveInvoice = {},
            onSaveDraft = {},
            onPlaceholderAction = {},
        )
    }
}
