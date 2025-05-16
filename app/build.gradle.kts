plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
//    signingConfigs {
//        getByName("debug") {
//            storeFile = file("C:\\keystore\\keystore.jks")
//            storePassword = "Qui23022003@"
//            keyAlias = "Quangqui"
//            keyPassword = "Qui23022003@"
//        }
//        create("myconfig") {
//            storeFile = file("C:\\keystore\\keystore.jks")
//            storePassword = "Qui23022003@"
//            keyAlias = "Quangqui"
//            keyPassword = "Qui23022003@"
//        }
//    }
    namespace = "com.example.dietarysupplementshop"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.dietarysupplementshop"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
//            signingConfig = signingConfigs.getByName("myconfig")
        }
//        getByName("debug") {
//            signingConfig = signingConfigs.getByName("myconfig")
//        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("nl.dionsegijn:konfetti-xml:2.0.4")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.6")
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.6")

    implementation("com.facebook.shimmer:shimmer:0.5.0")

    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.facebook.android:facebook-login:17.0.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.11.0")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("com.airbnb.android:lottie:6.5.2")
    implementation("androidx.room:room-runtime:2.6.1")
//    implementation(fileTree(mapOf(
//        "dir" to "D:\\zalopay",
//        "include" to listOf("*.aar", "*.jar"),
//        "exclude" to listOf("")
//    )))
    implementation("commons-codec:commons-codec:1.17.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}