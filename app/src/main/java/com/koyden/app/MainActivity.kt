package com.koyden.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.koyden.app.navigation.KoydenNavHost
import com.koyden.designsystem.theme.KoydenTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Tek-Activity mimarisi: tüm ekranlar Compose Navigation ile yönetilir.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            KoydenApp()
        }
    }
}

@Composable
private fun KoydenApp() {
    KoydenTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            KoydenNavHost(modifier = Modifier.padding(innerPadding))
        }
    }
}
