plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlinter) apply false
}

subprojects {
    plugins.apply("org.jmailen.kotlinter")

    plugins.withId("org.jmailen.kotlinter") {
        configure<org.jmailen.gradle.kotlinter.KotlinterExtension> {
            ignoreFormatFailures = false
            ignoreLintFailures = false
            reporters = arrayOf("checkstyle")
        }
        dependencies {
            add("ktlint", "io.nlopez.compose.rules:ktlint:0.4.22")
        }
    }
}