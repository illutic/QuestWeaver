plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "g.sig.questweaver"
    compileSdk = libs.versions.targetSdk.toInt()

    defaultConfig {
        applicationId = "g.sig.questweaver"
        minSdk = libs.versions.minSdk.toInt()
        targetSdk = libs.versions.targetSdk.toInt()
        versionCode = libs.versions.versionCode.toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:nearby"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:navigation"))
    implementation(project(":app:feature:onboarding"))
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)
}

fun Provider<String>.toInt(): Int = get().toInt()
