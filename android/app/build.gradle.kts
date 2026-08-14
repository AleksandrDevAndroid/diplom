import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt)

}

android {
    namespace = "ru.netology.nmedia"
    compileSdk = 36

    packaging {
        resources {
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/io.netty.versions.properties"
        }
    }

    defaultConfig {
        applicationId = "ru.netology.nmedia"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        if (rootProject.file("local.properties").exists()) {
                rootProject.file("local.properties").inputStream().use { properties.load(it) }
        }
        val API_KEY = properties.getProperty("API_KEY", "")
        buildConfigField("String", "API_KEY", "\"$API_KEY\"")
        val AUTH_API_KEY = properties.getProperty("AUTH_API_KEY", "")
        buildConfigField("String", "AUTH_API_KEY", "\"$AUTH_API_KEY\"")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["usesCleartextTraffic"] = false
            buildConfigField("String", "BASE_URL", "\"https://netomedia.ru\"")
        }
        debug {
            manifestPlaceholders["usesCleartextTraffic"] = true
            buildConfigField("String", "BASE_URL", "\"http://94.228.125.136:8080\"")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}


dependencies {
    implementation("com.airbnb.android:lottie:6.7.1")
    implementation("com.github.LottieFiles:dotlottie-android:0.5.0")
    implementation(libs.androidx.room.ktx)
    implementation(libs.firebase.appdistribution.gradle)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.gson)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.room)
    ksp(libs.androidx.room.compiler)
    implementation(platform(libs.firebase))
    implementation(libs.firebase.messaging)
    implementation(libs.play.services)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.glide)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.imagepicker)
    implementation(libs.ucrop)
    coreLibraryDesugaring(libs.desugaring)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.room.paging)
}
