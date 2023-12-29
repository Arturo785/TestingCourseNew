package com.plcoding.testingcourse.part12.presentation

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.app.NotificationCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.plcoding.testingcourse.MainActivity
import com.plcoding.testingcourse.part12.data.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountdownNotificationTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    // we hardcode the permission to be granted right away for test purposes
    @get:Rule
    val grantPermissionRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)

    @Test
    fun testNotificationCountdown() = runTest {
        runCurrent()
        // we get our notificationManager
        val context = ApplicationProvider.getApplicationContext<Context>()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        // this is like this because on the mainActivity the countdown is set from 5
        for(i in 5 downTo 1) {
            // we wait until the first one is populated
            composeRule.waitUntil {
                notificationManager.activeNotifications.isNotEmpty()
            }
            // we check the id
            val notification = notificationManager.activeNotifications.first {
                it.id == 1
            }.notification

            // we check that is counting down as expected
            val contentText = notification.extras.getString(NotificationCompat.EXTRA_TEXT)
            assertThat(contentText).isEqualTo("Counting down: $i")

            // and advance one second
            advanceTimeBy(1000L)
            runCurrent()
        }
        notificationManager.cancel(1)
    }
}