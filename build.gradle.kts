// Kök build dosyası — alt projeler kendi plugin'lerini version catalog'dan uygular.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.detekt) apply false
}

// --- detekt: tüm modüller için ortak kalite/a11y/güvenlik kapısı ---
subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        allRules = false
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        parallel = true
    }

    dependencies {
        add(
            "detektPlugins",
            "io.gitlab.arturbosch.detekt:detekt-formatting:${libs.versions.detekt.get()}",
        )
    }
}
