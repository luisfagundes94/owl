// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlinter) apply false
}

subprojects {
    plugins.apply("org.jmailen.kotlinter")

    plugins.withId("org.jmailen.kotlinter") {
        configure<org.jmailen.gradle.kotlinter.KotlinterExtension> {
            ignoreFormatFailures = true
            ignoreLintFailures = false
            reporters = arrayOf("checkstyle")
        }
    }
}