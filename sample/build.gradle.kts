plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
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
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${property("KOTLIN_VERSION")}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${property("KOTLINX_COROUTINES_ANDROID")}")

    implementation("androidx.core:core-ktx:${property("ANDROIDX_CORE_KTX")}")
    implementation("androidx.appcompat:appcompat:${property("ANDROIDX_APPCOMPAT")}")
    implementation("androidx.fragment:fragment-ktx:${property("ANDROIDX_FRAGMENT")}")
    implementation("androidx.constraintlayout:constraintlayout:${property("ANDROIDX_CONSTRAINTLAYOUT")}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${property("ANDROIDX_LIFECYCLE")}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${property("ANDROIDX_LIFECYCLE")}")
    implementation("com.google.android.material:material:${property("GOOGLE_MATERIAL")}")
    implementation("androidx.paging:paging-common:${property("ANDROIDX_PAGING")}")
    implementation("androidx.paging:paging-runtime:${property("ANDROIDX_PAGING")}")

    implementation("io.github.panpf.pagerindicator:pagerindicator:${property("PAGER_INDICATOR")}")
    implementation("io.github.panpf.sketch:sketch:${property("SKETCH_VERSION")}")
    implementation("io.github.panpf.tools4a:tools4a-dimen-ktx:${property("TOOLS4A")}")
    implementation("io.github.panpf.liveevent:liveevent:${property("LIVEEVENT")}")
    implementation("com.github.promeg:tinypinyin:${property("TINYPINYIN")}")

    implementation(project(":assemblyadapter"))
}
