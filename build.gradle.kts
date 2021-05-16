// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        jcenter()
        maven { setUrl("https://dl.google.com/dl/android/maven2/") }
        maven { setUrl("https://mirrors.huaweicloud.com/repository/maven/") }
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${property("ANDROID_BUILD_GRADLE")}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${property("KOTLIN_VERSION")}")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.15.1")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        maven { setUrl("https://mirrors.huaweicloud.com/repository/maven/") } // Huawei Maven mirrors
        jcenter()
        maven { setUrl("https://dl.google.com/dl/android/maven2/") }
        maven { setUrl("https://dl.bintray.com/panpf/maven") }  // sticky-recycler-item-decoration
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}