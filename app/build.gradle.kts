import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "merail.otp.system"
    compileSdk = 36

    defaultConfig {
        applicationId = "merail.otp.system"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JvmTarget.JVM_17.target
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            pickFirsts += "/META-INF/{NOTICE.md,LICENSE.md}"
        }
    }

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    buildTypes {
        debug {
            buildConfigField(
                type = "String",
                name = "HOST_EMAIL",
                value = properties.getProperty("hostEmail"),
            )
            buildConfigField(
                type = "String",
                name = "HOST_PASSWORD",
                value = properties.getProperty("hostPassword"),
            )
            buildConfigField(
                type = "String",
                name = "ADRESSEE_EMAIL",
                value = properties.getProperty("adresseeEmail"),
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)

    implementation(libs.android.mail)
    implementation(libs.android.activation)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation.compose)
}