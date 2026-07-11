plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.kazakago.simswitcher"
    compileSdk = 37
    defaultConfig {
        applicationId = "com.kazakago.simswitcher"
        minSdk = 29
        targetSdk = 37
        versionCode = 1
        versionName = "1.0.0"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
