import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val secrets = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) load(file.inputStream())
}

android {
    namespace = "com.followapp.mytime"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.followapp.mytime"
        minSdk = 25
        targetSdk = 36
        versionCode = 13
        versionName = "1.1.13"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            // Upload keystore path + credentials live in local.properties
            // (git-ignored). When absent — CI, or a dev who only builds debug —
            // release builds are produced unsigned, which is fine for local R8
            // checks. A real release needs all four set.
            val storeFilePath = secrets.getProperty("RELEASE_STORE_FILE", "")
            if (storeFilePath.isNotEmpty()) {
                storeFile = file(storeFilePath)
                storePassword = secrets.getProperty("RELEASE_STORE_PASSWORD", "")
                keyAlias = secrets.getProperty("RELEASE_KEY_ALIAS", "")
                keyPassword = secrets.getProperty("RELEASE_KEY_PASSWORD", "")
            }
        }
    }

    buildTypes {
        release {
            // Enables code shrinking, obfuscation, and optimization.
            isMinifyEnabled = true
            // Enables resource shrinking, performed by the Android Gradle plugin.
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Sign with the upload key when configured; otherwise unsigned.
            if (secrets.getProperty("RELEASE_STORE_FILE", "").isNotEmpty()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}