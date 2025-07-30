import org.gradle.kotlin.dsl.android
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.ksp)
}

android {
    namespace = "merail.otp.auth.impl"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JvmTarget.JVM_17.target
    }

    buildFeatures {
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
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.android.mail)
    implementation(libs.android.activation)

    implementation(project(":core"))
    implementation(project(":auth:api"))
}