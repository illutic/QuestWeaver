plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "g.sig.questweaver.common.data"
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
    api(libs.kotlinx.serialization.protobuf)
    api(libs.kotlinx.coroutines.core)
}

fun Provider<String>.toInt(): Int = get().toInt()
