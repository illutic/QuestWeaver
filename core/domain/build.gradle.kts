plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "g.sig.questweaver.domain"
    compileSdk = libs.versions.targetSdk.toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}

fun Provider<String>.toInt(): Int = get().toInt()
