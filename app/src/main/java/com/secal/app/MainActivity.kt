package com.secal.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.secal.app.navigation.SecalNavHost
import com.secal.designsystem.theme.SecalTheme
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
            SecalApp()
        }
    }
}

@Composable
private fun SecalApp() {
    SecalTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SecalNavHost(modifier = Modifier.padding(innerPadding))
        }
    }
}
