plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = property("COMPILE_SDK_VERSION").toString().toInt()

    defaultConfig {
        applicationId = "com.github.panpf.assemblyadapter.sample"
        minSdk = property("MIN_SDK_VERSION").toString().toInt()
        targetSdk = property("TARGET_SDK_VERSION").toString().toInt()
        versionCode = property("VERSION_CODE").toString().toInt()
        versionName = property("VERSION_NAME").toString()
    }

    buildTypes {
        getByName("debug") {
            multiDexEnabled = true
        }
        getByName("release") {
            multiDexEnabled = true
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${property("KOTLIN")}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${property("KOTLINX_COROUTINES_ANDROID")}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${property("KOTLINX_SERIALIZATION_JSON")}")
    implementation("androidx.multidex:multidex:${property("ANDROIDX_MULTIDEX")}")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${property("LEAK_CANARY")}")

    implementation("androidx.core:core-ktx:${property("ANDROIDX_CORE")}")
    implementation("androidx.appcompat:appcompat:${property("ANDROIDX_APPCOMPAT")}")
    implementation("androidx.fragment:fragment-ktx:${property("ANDROIDX_FRAGMENT")}")
    implementation("androidx.constraintlayout:constraintlayout:${property("ANDROIDX_CONSTRAINTLAYOUT")}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${property("ANDROIDX_LIFECYCLE")}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${property("ANDROIDX_LIFECYCLE")}")
    implementation("androidx.paging:paging-common:${property("ANDROIDX_PAGING")}")
    implementation("androidx.paging:paging-runtime:${property("ANDROIDX_PAGING")}")
    implementation("androidx.navigation:navigation-fragment-ktx:${property("ANDROIDX_NAVIGATION")}")
    implementation("androidx.navigation:navigation-ui-ktx:${property("ANDROIDX_NAVIGATION")}")

    implementation("com.google.android.material:material:${property("GOOGLE_MATERIAL")}")
    implementation("io.github.panpf.pagerindicator:pagerindicator:${property("PAGER_INDICATOR")}")
    implementation("io.github.panpf.sketch:sketch:${property("SKETCH_VERSION")}")
    implementation("io.github.panpf.tools4a:tools4a-dimen-ktx:${property("TOOLS4A")}")
    implementation("io.github.panpf.tools4a:tools4a-display-ktx:${property("TOOLS4A")}")
    implementation("io.github.panpf.tools4k:tools4k:${property("TOOLS4K")}")
    implementation("io.github.panpf.liveevent:liveevent:${property("LIVEEVENT")}")
    implementation("com.github.promeg:tinypinyin:${property("TINYPINYIN")}")

    implementation(project(":assemblyadapter"))
}