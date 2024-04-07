plugins {
    alias(libs.plugins.androidApplication)
}

android {
    packagingOptions {
        exclude ("META-INF/DEPENDENCIES")
    }
    namespace = "com.example.tp3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tp3"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    implementation("com.itextpdf:itextpdf:5.5.13.2")
    androidTestImplementation(libs.espresso.core)
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("org.apache.pdfbox:pdfbox:2.0.24")
    implementation ("com.itextpdf:itext7-core:7.2.0")
}