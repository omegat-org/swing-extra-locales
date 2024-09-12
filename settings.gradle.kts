plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    id("com.gradle.develocity") version "3.18"
}
develocity {
    buildScan {
        publishing.onlyIf { "true".equals(System.getProperty("envIsCi")) }
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
    }
}
rootProject.name = "swing-extra-locales"
