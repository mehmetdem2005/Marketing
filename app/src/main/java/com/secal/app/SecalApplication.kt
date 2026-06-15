package com.secal.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Uygulama giriş noktası. Hilt bağımlılık grafiğinin kökü.
 */
@HiltAndroidApp
class SecalApplication : Application()
