buildscript {
    repositories {
        maven { setUrl("https://mirrors.huaweicloud.com/repository/maven/") }
        google()
        mavenCentral()
        mavenLocal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${property("ANDROID_BUILD_GRADLE")}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${property("KOTLIN_VERSION")}")
//        classpath("com.vanniktech:gradle-maven-publish-plugin:0.15.1")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.16.0-SNAPSHOT")  // from mavenLocal()
    }
}

allprojects {
    repositories {
        maven { setUrl("https://mirrors.huaweicloud.com/repository/maven/") }
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}