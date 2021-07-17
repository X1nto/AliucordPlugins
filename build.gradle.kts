import com.android.build.gradle.BaseExtension

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.2")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

fun Project.android(configuration: BaseExtension.() -> Unit) = extensions.getByName<BaseExtension>("android").configuration()

subprojects {
    if (name != "Aliucord" && name != "DiscordStubs") {
        apply(plugin = "com.android.library")

        android {
            compileSdkVersion(30)

            defaultConfig {
                minSdkVersion(24)
                targetSdkVersion(30)
                versionCode = 1
                versionName = "1.0"
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }

        dependencies {
            val implementation by configurations

            implementation(project(":Aliucord"))

            implementation("androidx.appcompat:appcompat:1.3.0")
            implementation("com.google.android.material:material:1.4.0")
        }
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}