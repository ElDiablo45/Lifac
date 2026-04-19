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
    fun displaysHelloWorldMessage() {
        composeRule.onNodeWithText("Hello World").assertIsDisplayed()
        composeRule
            .onNodeWithText("Base Android 16 con Kotlin, Compose y Material 3.")
            .assertIsDisplayed()
    }
}
