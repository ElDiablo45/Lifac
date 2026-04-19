package io.github.eldiablo45.lifac

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun displaysHomeWorkspace() {
        composeRule.onNodeWithText("Nueva factura").assertIsDisplayed()
        composeRule.onNodeWithText("Facturas recientes").assertIsDisplayed()
    }
}
