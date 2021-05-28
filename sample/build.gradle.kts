plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(property("COMPILE_SDK_VERSION").toString().toInt())

    defaultConfig {
        applicationId = "me.panpf.adapter.sample"
        minSdkVersion(property("MIN_SDK_VERSION").toString().toInt())
        targetSdkVersion(property("TARGET_SDK_VERSION").toString().toInt())
        versionCode = property("VERSION_CODE").toString().toInt()
        versionName = property("VERSION_NAME").toString()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${property("KOTLIN_VERSION")}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${property("KOTLINX_COROUTINES_ANDROID")}")

    implementation("androidx.core:core-ktx:${property("ANDROIDX_CORE_KTX")}")
    implementation("androidx.appcompat:appcompat:${property("ANDROIDX_APPCOMPAT")}")
    implementation("androidx.fragment:fragment:${property("ANDROIDX_FRAGMENT")}")
    implementation("androidx.constraintlayout:constraintlayout:${property("ANDROIDX_CONSTRAINTLAYOUT")}")
    implementation("com.google.android.material:material:${property("MATERIAL")}")

    implementation("me.panpf:pager-indicator:${property("PAGER_INDICATOR")}")
    implementation("me.panpf:sketch:${property("SKETCH_VERSION")}")
    implementation("me.panpf:sticky-recycler-item-decoration:${property("STICKY_RECYCLER_ITEM_DECORATION")}")

    implementation(project(":assembly-adapter"))
    implementation(project(":assembly-adapter-ktx"))
    implementation(project(":assembly-adapter-paging"))
}
