import com.aliucord.gradle.AliucordExtension
import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.2")
        classpath("com.github.Aliucord:gradle:resources-SNAPSHOT")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

fun Project.aliucord(
    configuration: AliucordExtension.() -> Unit
) = extensions.getByName<AliucordExtension>("aliucord").configuration()

fun Project.android(
    configuration: BaseExtension.() -> Unit
) = extensions.getByName<BaseExtension>("android").configuration()

subprojects {
    apply(plugin = "com.android.library")
    apply(plugin = "com.aliucord.gradle")
    apply(plugin = "kotlin-android")

    aliucord {
        val baseGithubUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins"
        author("Xinto", 423915768191647755L)
        updateUrl.set("$baseGithubUrl/builds/updater.json")
        buildUrl.set("$baseGithubUrl/builds/%s.zip")
    }

    android {
        compileSdkVersion(30)

        defaultConfig {
            minSdk = 24
            targetSdk= 30
            versionCode = 1
            versionName = "1.0"
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    dependencies {
        "discord"("com.discord:discord:aliucord-SNAPSHOT")
        "implementation"("com.github.Aliucord:Aliucord:main-SNAPSHOT")

        "implementation"("androidx.appcompat:appcompat:1.3.1")
        "implementation"("com.google.android.material:material:1.4.0")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}