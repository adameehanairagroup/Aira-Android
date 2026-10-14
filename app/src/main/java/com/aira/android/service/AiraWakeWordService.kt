package com.aira.android.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.aira.android.accessibility.AiraAccessibilityService
import com.aira.android.biometrics.AcousticBiometricGate
import com.aira.android.ui.overlay.AiraAssistantPopup
import kotlinx.coroutines.*

/**
 * AiraWakeWordService:
 * Ultra-Low-Power (< 1% battery) background listener.
 * Keeps dormant until Boss says "Aira!", triggers the floating
 * popup, checks voice biometrics, and executes device actions.
 */
class AiraWakeWordService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())
    private lateinit var popup: AiraAssistantPopup
    private val biometricGate = AcousticBiometricGate()

    override fun onCreate() {
        super.onCreate()
        popup = AiraAssistantPopup(this)
        startForegroundServiceWithNotification()
        startLowPowerWakeWordListener()
    }

    private fun startForegroundServiceWithNotification() {
        val channelId = "aira_sentinel_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Aira Sentinel Guardian",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitors wake-word and system guard state"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("⚡ Aira Active")
            .setContentText("Listening for wake-word \"Aira!\" · Voice Shield ON")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(1001, notification)
    }

    private fun startLowPowerWakeWordListener() {
        serviceScope.launch {
            // Simulated micro-wake-word loop (In production: Porcupine/OpenWakeWord)
            // When "Aira!" is detected:
            // onWakeWordDetected()
        }
    }

    /**
     * Called when the wake-up word "Aira!" is spoken.
     * Shows the floating popup and processes the command.
     */
    fun onWakeWordDetected(commandAudio: FloatArray) {
        CoroutineScope(Dispatchers.Main).launch {
            // 1. Instantly show the Assistant Popup() over other apps
            popup.show(statusText = "⚡ Aira listening...")

            // 2. Validate Speaker Biometrics (Er. Adam Eehan only)
            val isBoss = biometricGate.isAuthorizedSpeaker(commandAudio)
            if (!isBoss) {
                popup.updateText("Access Denied", "Unauthorized voice detected!")
                delay(2000)
                popup.dismiss()
                return@launch
            }

            // 3. Process Command (e.g. Flashlight / Screen Lock)
            popup.updateText("Executing...", "Flashlight toggled!")
            AiraAccessibilityService.instance?.toggleTorch()

            // 4. Dismiss popup after completion
            delay(1500)
            popup.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
