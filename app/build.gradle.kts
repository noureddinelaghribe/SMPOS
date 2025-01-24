

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.noureddine.stockmanagment"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.noureddine.stockmanagment"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets {
        getByName("main") {
            assets.srcDirs("src/main/assets")
        }
    }

    packaging {
        resources.excludes += setOf(
            "META-INF/DEPENDENCIES",
            "META-INF/LICENSE",
            "META-INF/LICENSE.txt",
            "META-INF/license.txt",
            "META-INF/NOTICE",
            "META-INF/NOTICE.txt",
            "META-INF/notice.txt",
            "META-INF/ASL2.0",
            "META-INF/*.kotlin_module"
        )
    }

}

dependencies {
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.guava:guava:32.1.2-jre")

    //implementation("com.itextpdf:itext7-core:7.2.3")
    implementation("com.itextpdf:itextpdf:5.5.13.3")
    implementation("com.ibm.icu:icu4j:73.2")
    implementation("com.google.android.gms:play-services-nearby:18.0.0")

    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.jpardogo.googleprogressbar:library:1.2.0")
    implementation(libs.firebase.database)

    val work_version = "2.9.1"
    implementation("androidx.work:work-runtime:$work_version")
    implementation("androidx.work:work-runtime-ktx:$work_version")
    implementation("androidx.work:work-rxjava3:$work_version")
    implementation("androidx.work:work-multiprocess:$work_version")
    androidTestImplementation("androidx.work:work-testing:$work_version")

    implementation("com.google.code.gson:gson:2.11.0")
    implementation("de.raphaelebner:roomdatabasebackup:1.0.1")
    implementation("com.readystatesoftware.sqliteasset:sqliteassethelper:2.0.1")
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
