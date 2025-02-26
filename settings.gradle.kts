plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
    id("com.gradle.develocity") version "3.19"
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
