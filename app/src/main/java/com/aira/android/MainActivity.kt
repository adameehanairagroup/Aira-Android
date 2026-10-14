package com.aira.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aira.android.service.AiraWakeWordService

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AiraDashboard()
        }
    }

    @Composable
    fun AiraDashboard() {
        var isOverlayGranted by remember { mutableStateOf(Settings.canDrawOverlays(this)) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF080C10))
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚡ AIRA FOR ANDROID",
                color = Color(0xFF00F5FF),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sovereign Mobile AI Companion by Er. Adam Eehan",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Permission 1: Overlay Permission for popup()
            Button(
                onClick = {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isOverlayGranted) Color(0xFF00FF88) else Color(0xFF1F2937)
                )
            ) {
                Text(
                    text = if (isOverlayGranted) "✓ Overlay Granted (popup active)" else "1. Grant Overlay Permission (popup())",
                    color = if (isOverlayGranted) Color.Black else Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Permission 2: Accessibility Service
            Button(
                onClick = {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F2937))
            ) {
                Text("2. Enable Accessibility Service (God-Mode)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Start Sentinel Service
            Button(
                onClick = {
                    val intent = Intent(this@MainActivity, AiraWakeWordService::class.java)
                    startForegroundService(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00F5FF))
            ) {
                Text("🚀 Start Aira Wake-Word Sentinel", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}
