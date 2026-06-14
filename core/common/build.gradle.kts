plugins {
    alias(libs.plugins.kotlin.jvm)
}

// core:common saf Kotlin'dir (Android API kullanmaz). kotlin.jvm olarak yayınlanır ki
// hem :core:domain (kotlin.jvm) hem de Android modülleri (data/designsystem/ui/feature/app)
// tüketebilsin. (Önceki android.library hali :core:domain ile variant uyumsuzluğu yaratıyordu.)
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
