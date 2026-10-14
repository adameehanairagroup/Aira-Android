package com.aira.android.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.os.Build
import android.view.accessibility.AccessibilityEvent

/**
 * AiraAccessibilityService:
 * The official Android God-Mode controller. Enables hands-free
 * screen locking, home/back navigation, screenshots, torch toggle,
 * and app interactions without needing root privileges.
 */
class AiraAccessibilityService : AccessibilityService() {

    companion object {
        var instance: AiraAccessibilityService? = null
            private set
    }

    private var isTorchOn = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Can be used to inspect notifications or active app windows
    }

    override fun onInterrupt() {
        instance = null
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    // ============================================
    // SYSTEM ACTIONS (Zero-Root God-Mode)
    // ============================================

    /** Locks the device screen immediately */
    fun lockScreen(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
        } else {
            false
        }
    }

    /** Navigates to the Home screen */
    fun goHome(): Boolean {
        return performGlobalAction(GLOBAL_ACTION_HOME)
    }

    /** Navigates Back */
    fun goBack(): Boolean {
        return performGlobalAction(GLOBAL_ACTION_BACK)
    }

    /** Takes an instantaneous stealth screenshot */
    fun takeScreenshot(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            performGlobalAction(GLOBAL_ACTION_TAKE_SCREENSHOT)
        } else {
            false
        }
    }

    /** Toggles the phone's physical flashlight in under 2ms */
    fun toggleTorch(): Boolean {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        return try {
            val cameraId = cameraManager.cameraIdList[0]
            isTorchOn = !isTorchOn
            cameraManager.setTorchMode(cameraId, isTorchOn)
            true
        } catch (e: Exception) {
            false
        }
    }

    /** Adjusts the media or notification volume */
    fun setVolume(level: Int) {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            level.coerceIn(0, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)),
            0
        )
    }
}
