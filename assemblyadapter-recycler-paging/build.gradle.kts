plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = property("COMPILE_SDK_VERSION").toString().toInt()

    defaultConfig {
        minSdk = property("MIN_SDK_VERSION").toString().toInt()
        targetSdk = property("TARGET_SDK_VERSION").toString().toInt()

        consumerProguardFiles("proguard-rules.pro")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${property("KOTLIN_VERSION")}")
    api("androidx.paging:paging-runtime:${property("ANDROIDX_PAGING")}")
    api("androidx.paging:paging-common:${property("ANDROIDX_PAGING")}")
    api(project(":assemblyadapter-common-recycler"))
    api(project(":assemblyadapter-recycler"))

    testImplementation("junit:junit:${property("JUNIT")}")
    testImplementation("io.github.panpf.tools4j:tools4j-test-ktx:${property("TOOLS4J")}")
    androidTestImplementation("io.github.panpf.tools4j:tools4j-test-ktx:${property("TOOLS4J")}")
    androidTestImplementation("androidx.test:runner:${property("ANDROIDX_TEST_RUNNER")}")
    androidTestImplementation("androidx.test:rules:${property("ANDROIDX_TEST_RULES")}")
}

/**
 * publish config
 */
if (hasProperty("signing.keyId")    // configured in the ~/.gradle/gradle.properties file
    && hasProperty("signing.password")    // configured in the ~/.gradle/gradle.properties file
    && hasProperty("signing.secretKeyRingFile")    // configured in the ~/.gradle/gradle.properties file
    && hasProperty("mavenCentralUsername")    // configured in the ~/.gradle/gradle.properties file
    && hasProperty("mavenCentralPassword")    // configured in the ~/.gradle/gradle.properties file
    && hasProperty("GROUP")    // configured in the rootProject/gradle.properties file
    && hasProperty("POM_ARTIFACT_ID")    // configured in the project/gradle.properties file
) {
    apply { plugin("com.github.panpf.maven.publish") }

    configure<com.github.panpf.maven.publish.MavenPublishPluginExtension> {
        sonatypeHost = com.github.panpf.maven.publish.SonatypeHost.S01
        disableAndroidJavaDocsAddReferencesLinks = true
    }
}