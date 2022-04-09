// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id ("com.android.application") version "7.1.2" apply false
    id ("com.android.library") version "7.1.2" apply false
    id ("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id ("org.jetbrains.kotlin.plugin.allopen") version "1.6.10" apply false
}

buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.41")
        val nav_version = "2.4.1"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        classpath ("org.jetbrains.kotlin:kotlin-allopen:1.6.10")
        classpath ("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.18.1")
    }
}
tasks.register(name = "type", type = Delete::class) {
    delete(rootProject.buildDir)
}
tasks.register(name ="detekt",type =io.gitlab.arturbosch.detekt.Detekt::class){
    description = "Runs autocorrect enabled detekt build."
    source = fileTree("src/main/java")
    reports {
        html{
            enabled = true
            destination = file("build/reports/detekt-formatted.html")
        }
    }
    parallel = true
    autoCorrect = true
}
//task detektFormating(type: io.gitlab.arturbosch.detekt.Detekt) {
//    description = "Runs autocorrect enabled detekt build."
//    source = files("src/main/java")
//    reports {
//        html{
//            enabled = true
//            destination = file("build/reports/detekt-formatted.html")
//        }
//    }
//    parallel = true
//    autoCorrect = true
//}