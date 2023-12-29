package com.plcoding.testingcourse.part11.presentation

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import com.plcoding.testingcourse.ui.theme.TestingCourseTheme
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {

    // seems to work with this and
    //debugImplementation("androidx.compose.ui:ui-test-manifest:1.3.0-rc01")
    // this rule creates a basic activity for compose, blanc and we fill it with our own composable
    // is used more for isolated UI test meaning we only test this composable and the the interaction
    // between other composables and other components like viewModels etc.
    // for example just this ProfileScreen and nothing else
    @get:Rule
    val composeRule = createComposeRule()

    // this rule creates our own composable with our own activity we provide, could be our mainActivity like in this example
    // is used more for integrated UI test meaning we test this composable and the the interaction
    // between other composables and other components like viewModels etc.
    // for example navigation with other composables, react to state provided by viewModels etc
    //@get:Rule
    //val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testProfileScreenUi_profileLoaded() {
        composeRule.setContent {
            TestingCourseTheme {
                ProfileScreen(
                    state = previewProfileState()
                )
            }
        }

        // we can concatenate matchers like in here will search for a node with that text and that
        // also is clickable
       composeRule.onNode(hasText("test").and(hasClickAction()))
    }
}