package com.aira.android.ui.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.aira.android.R

/**
 * AiraAssistantPopup: Displays the floating assistant overlay
 * when the wake-up word "Aira!" is spoken.
 *
 * Requirements:
 * 1. android.permission.SYSTEM_ALERT_WINDOW in AndroidManifest.xml
 * 2. Settings.canDrawOverlays(context) granted by user ("Display over other apps")
 */
class AiraAssistantPopup(private val context: Context) {

    private val windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var overlayView: View? = null
    private var isShowing = false

    /**
     * Checks if the app has permission to draw the assistant popup over other apps.
     */
    fun hasOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(context)
    }

    /**
     * Shows the floating assistant popup bottom-sheet over whatever app is active.
     */
    fun show(statusText: String = "Listening...", responseText: String? = null) {
        if (!hasOverlayPermission()) return

        if (isShowing && overlayView != null) {
            updateText(statusText, responseText)
            return
        }

        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.BOTTOM
            y = 50 // Floating margin from bottom edge
        }

        // Programmatically generate a sleek dark cyberpunk overlay view
        val layout = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(48, 36, 48, 48)
            background = android.graphics.drawable.GradientDrawable().apply {
                setColor(android.graphics.Color.parseColor("#E60A0E17")) // 90% obsidian dark
                cornerRadius = 60f
                setStroke(2, android.graphics.Color.parseColor("#3300F5FF")) // neon cyan border
            }
        }

        val headerText = TextView(context).apply {
            text = "⚡ AIRA INTELLIGENCE"
            setTextColor(android.graphics.Color.parseColor("#00F5FF"))
            textSize = 12f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        val bodyText = TextView(context).apply {
            id = View.generateViewId()
            text = statusText
            setTextColor(android.graphics.Color.WHITE)
            textSize = 16f
            setPadding(0, 12, 0, 0)
        }

        layout.addView(headerText)
        layout.addView(bodyText)

        overlayView = layout
        windowManager.addView(overlayView, params)
        isShowing = true
    }

    /**
     * Updates the status or spoken reply in the floating popup.
     */
    fun updateText(status: String, reply: String?) {
        val body = (overlayView as? android.widget.LinearLayout)?.getChildAt(1) as? TextView
        body?.text = reply ?: status
    }

    /**
     * Dismisses the popup smoothly when the task completes.
     */
    fun dismiss() {
        if (isShowing && overlayView != null) {
            try {
                windowManager.removeView(overlayView)
            } catch (e: Exception) {
                // Ignore if already detached
            }
            overlayView = null
            isShowing = false
        }
    }
}
