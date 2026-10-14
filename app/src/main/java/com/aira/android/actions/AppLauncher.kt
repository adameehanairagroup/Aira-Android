package com.aira.android.actions

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * AppLauncher:
 * Opens any installed application on Boss's Android phone instantly.
 */
class AppLauncher(private val context: Context) {

    // Package names of popular apps
    private val appMap = mapOf(
        "whatsapp" to "com.whatsapp",
        "youtube" to "com.google.android.youtube",
        "instagram" to "com.instagram.android",
        "chrome" to "com.android.chrome",
        "camera" to "com.android.camera",
        "settings" to "com.android.settings",
        "telegram" to "org.telegram.messenger",
        "spotify" to "com.spotify.music",
        "termux" to "com.termux"
    )

    /**
     * Launches an app by name or exact package name.
     */
    fun openApp(appName: String): Boolean {
        val cleanName = appName.trim().lowercase()
        val packageName = appMap[cleanName] ?: cleanName

        return try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                true
            } else {
                // If package not found, try generic action
                if (cleanName.contains("camera")) {
                    val camIntent = Intent("android.media.action.IMAGE_CAPTURE").apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(camIntent)
                    true
                } else {
                    false
                }
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Opens Google Maps navigation to a destination.
     */
    fun openNavigation(query: String): Boolean {
        return try {
            val uri = Uri.parse("geo:0,0?q=" + Uri.encode(query))
            val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(mapIntent)
            true
        } catch (e: Exception) {
            false
        }
    }
}
