plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    id("com.gradle.develocity") version "3.18"
}

val isCI = !System.getenv("envIsCi").isNullOrEmpty()

develocity {
    buildScan {
        uploadInBackground.set(!isCI)
        publishing.onlyIf { it.buildResult.failures.isNotEmpty() && isCI }
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
    }
}
rootProject.name = "swing-extra-locales"
