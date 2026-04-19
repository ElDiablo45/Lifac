package io.github.eldiablo45.lifac

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.eldiablo45.lifac.ui.home.GreetingScreen
import io.github.eldiablo45.lifac.ui.home.GreetingViewModel
import io.github.eldiablo45.lifac.ui.theme.LifacTheme

@Composable
fun LifacApp(
    modifier: Modifier = Modifier,
) {
    val viewModel: GreetingViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifacTheme {
        GreetingScreen(
            uiState = uiState,
            modifier = modifier,
        )
    }
}

