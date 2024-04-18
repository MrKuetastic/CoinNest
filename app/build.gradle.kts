//plugins {
//    id("com.android.application")
//}
//
//android {
//    namespace = "com.example.coinnest"
//    compileSdk = 34
//
//    defaultConfig {
//        applicationId = "com.example.coinnest"
//        minSdk = 24
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"
//
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//
//    buildFeatures {
//        viewBinding = true
//    }
//}
//
//dependencies {
//    implementation("com.google.code.gson:gson:2.8.6")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.9.0")
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    androidTestImplementation("org.mockito:mockito-android:3.3.3")
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    implementation("com.android.volley:volley:1.2.0")
//    testImplementation("org.robolectric:robolectric:4.12.1")
//    testImplementation("org.mockito:mockito-core:3.12.4")
//    androidTestImplementation("androidx.test:runner:1.4.0")
//    androidTestImplementation("androidx.test:rules:1.4.0")
//    testImplementation("androidx.arch.core:core-testing:2.1.0")
//    testImplementation("androidx.test:core:1.4.0")
//    androidTestImplementation ("androidx.fragment:fragment-testing:1.4.0")
//}


plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.coinnest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.coinnest"
        minSdk = 26
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    androidTestImplementation("org.mockito:mockito-android:3.3.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.android.volley:volley:1.2.0")
    testImplementation("org.robolectric:robolectric:4.12.1")
    testImplementation("org.mockito:mockito-core:3.12.4")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.test:core:1.4.0")
    androidTestImplementation ("androidx.fragment:fragment-testing:1.4.0")
}