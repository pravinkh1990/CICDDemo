plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.sonarqube")
}
android {
    namespace = "com.example.cicddemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cicddemo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            enableAndroidTestCoverage =true
            enableUnitTestCoverage =true
        }
        release {
            isMinifyEnabled = false
            enableAndroidTestCoverage =false
            enableUnitTestCoverage =false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("org.mockito:mockito-core:3.12.4")
// Optional if you need to detect intents.
    androidTestImplementation ("com.android.support.test.espresso:espresso-intents:2.2.1")
}
