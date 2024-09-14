import java.io.FileInputStream
import java.util.Properties

plugins {
    java
    signing
    `maven-publish`
    alias(libs.plugins.spotbugs)
    alias(libs.plugins.spotless)
    alias(libs.plugins.git.version)
    alias(libs.plugins.nexus.publish)
}

val dotgit = project.file(".git")
if (dotgit.exists()) {
    apply(plugin = libs.plugins.git.version.get().pluginId)
    val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra
    val details = versionDetails()
    val baseVersion = details.lastTag.substring(1)
    version = when {
        details.isCleanTag -> baseVersion
        else -> baseVersion + "-" + details.commitDistance + "-" + details.gitHash + "-SNAPSHOT"
    }
} else {
    val gitArchival = project.file(".git-archival.properties")
    val props = Properties()
    props.load(FileInputStream(gitArchival))
    val versionDescribe = props.getProperty("describe")
    val regex = "^v\\d+\\.\\d+\\.\\d+$".toRegex()
    version = when {
        regex.matches(versionDescribe) -> versionDescribe.substring(1)
        else -> versionDescribe.substring(1) + "-SNAPSHOT"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.mnemonics)
    testImplementation(libs.assertj.swing.junit) {
        exclude("junit")
    }
    testImplementation(libs.junit)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
    withSourcesJar()
    withJavadocJar()
}

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "org.omegat.swing.extra.locales")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "org.omegat"
            artifactId = "swing-extra-locales"
            pom {
                name.set("swing-extra-locales")
                description.set("Extra locales for Java Standard UI components")
                url.set("https://codeberg.org/miurahr/java-swing-extra-locales")
                licenses {
                    license {
                        name.set("The GNU General Public License, Version 2 with the Classpath Exception")
                        url.set("https://openjdk.org/legal/gplv2+ce.html")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("miurahr")
                        name.set("Hiroshi Miura")
                        email.set("miurahr@linux.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://codeberg.org/miurahr/java-swing-extra-locales.git")
                    developerConnection.set("scm:git:git://codeberg.org/miurahr/java-swing-extra-locales.git")
                    url.set("https://codeberg.org/miurahr/java-swing-extra-locales")
                }
            }
        }
    }
}

val signKey = listOf("signingKey", "signing.keyId", "signing.gnupg.keyName").find { project.hasProperty(it) }
tasks.withType<Sign> {
    onlyIf { signKey != null && !rootProject.version.toString().endsWith("-SNAPSHOT") }
}

signing {
    when (signKey) {
        "signingKey" -> {
            val signingKey: String? by project
            val signingPassword: String? by project
            useInMemoryPgpKeys(signingKey, signingPassword)
        }
        "signing.keyId" -> { /* do nothing */
        }
        "signing.gnupg.keyName" -> {
            useGpgCmd()
        }
    }
    sign(publishing.publications["mavenJava"])
}

tasks.withType<Javadoc>() {
    setFailOnError(false)
    options {
        jFlags("-Duser.language=en")
    }
}

tasks.register<Test>("testAr") {
    systemProperty("user.language", "ar")
    systemProperty("user.country", "SA")
    group = "verification"
}
tasks.register<Test>("testCa") {
    systemProperty("user.language", "ca")
    systemProperty("user.country", "ES")
    group = "verification"
}
tasks.register<Test>("testRu") {
    systemProperty("user.language", "ru")
    systemProperty("user.country", "RU")
    group = "verification"
}
tasks.register<Test>("testUk") {
    systemProperty("user.language", "uk")
    systemProperty("user.country", "UK")
    group = "verification"
}

tasks.check {
    dependsOn("testAr", "testCa", "testRu", "testUk")
}

nexusPublishing.repositories.sonatype {
    val sonatypeUsername: String? by project
    val sonatypePassword: String? by project
    nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
    username.set(sonatypeUsername)
    password.set(sonatypePassword)
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.WARN
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

spotless {
    format("misc") {
        target(listOf("*.gradle", ".gitignore"))
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    java {
        target(listOf("src/*/java/**/*.java"))
        palantirJavaFormat()
        importOrder()
        removeUnusedImports()
        formatAnnotations()
    }
}