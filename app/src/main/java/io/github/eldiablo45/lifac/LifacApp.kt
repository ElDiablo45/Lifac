package io.github.eldiablo45.lifac

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.github.eldiablo45.lifac.ui.app.LifacAppScreen
import io.github.eldiablo45.lifac.ui.app.LifacAppViewModel
import io.github.eldiablo45.lifac.ui.theme.LifacTheme
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun LifacApp(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val factory = remember(context) {
        LifacAppViewModel.provideFactory(context)
    }
    val viewModel: LifacAppViewModel = viewModel(
        factory = factory,
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    LifacTheme {
        LifacAppScreen(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onNavigate = viewModel::navigateTo,
            onOpenNewInvoice = viewModel::openNewInvoice,
            onBackFromEditor = viewModel::leaveEditor,
            onClientKindSelected = viewModel::updateClientKind,
            onClientDisplayNameChanged = viewModel::updateClientDisplayName,
            onClientTaxIdChanged = viewModel::updateClientTaxId,
            onClientCityChanged = viewModel::updateClientCity,
            onClientAddressChanged = viewModel::updateClientAddress,
            onClientNotesChanged = viewModel::updateClientNotes,
            onSaveClient = viewModel::saveClient,
            onResetClientForm = viewModel::resetClientForm,
            onDraftClientSelected = viewModel::selectDraftClient,
            onOpenClientsForDraft = viewModel::openClientsForDraft,
            onPlaceholderAction = { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            },
            modifier = modifier,
        )
    }
}
