package com.plcoding.testingcourse

import android.Manifest
import android.content.ComponentName
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.plcoding.testingcourse.part12.presentation.ProfileActivity
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()


    /**
     * This grants the permission immediately to the test, using this the permission dialog won't
     * appear since acts like if the permission is already automatically granted
     *
     * Works for cases like when we want to test the functionality that needs the permission rather
     * than testing the checks of the permissions
     * */
    //@get:Rule
    //val grantPermissionRule = GrantPermissionRule.grant(Manifest.permission.RECORD_AUDIO)

//    @get:Rule
//    val intentsRule = IntentsRule()
//
//    @Test
//    fun testLaunchingOtherActivity() {
//        composeRule.onNodeWithText("Send intent").performClick()
//
//        Intents.intended(
//            IntentMatchers.hasComponent(
//                ComponentName(composeRule.activity.applicationContext, ProfileActivity::class.java)
//            )
//        )
//        Intents.intended(IntentMatchers.hasExtra("TEST_EXTRA", "top secret"))
//        Intents.intended(IntentMatchers.hasAction("MY_ACTION"))
//    }

    @Test
    fun testRecordAudioPermissionDenial_showsErrorDialog() {
        // will trigger the permission request
        composeRule.onNodeWithText("Record").performClick()

        // I think this part needs this dependencies
        //    androidTestImplementation 'androidx.test.uiautomator:uiautomator:2.3.0-alpha04'
        //    androidTestUtil 'androidx.test:orchestrator:1.4.2'

        // Since the permission dialog is not part of the compose code we need this way
        // to access the button and being able to click it
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val dontAllowButton = device.findObject(UiSelector().textStartsWith("Don"))
        dontAllowButton.click()

        composeRule.onNodeWithText("Record").performClick()
        dontAllowButton.click()

        // after several don't allow the text should appear
        composeRule.onNodeWithText("Can't record without permission").assertIsDisplayed()
    }
}