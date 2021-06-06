buildscript {
    repositories {
        mavenCentral()
        google()
        maven { setUrl("https://maven.aliyun.com/repository/jcenter") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${property("ANDROID_BUILD_GRADLE")}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${property("KOTLIN_VERSION")}")
        classpath("io.github.panpf.maven-publish:maven-publish-gradle-plugin:${property("MAVEN_PUBLISH")}")  // from mavenLocal()
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven { setUrl("https://maven.aliyun.com/repository/jcenter") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}