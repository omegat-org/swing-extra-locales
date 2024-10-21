import java.io.ByteArrayOutputStream
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
                url.set("https://github.com/omegat-org/swing-extra-locales")
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
                    connection.set("scm:git:git://github.com/omegat-org/swing-extra-locales.git")
                    developerConnection.set("scm:git:git://github.com/omegat-org/swing-extra-locales.git")
                    url.set("https://github.com/omegat-org/swing-extra-locales")
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

fun startX(display: String): String {
    val outputStream = ByteArrayOutputStream()
    exec {
        commandLine("sh", "-c", "Xvfb " + display + " -screen 0 1280x1024x24 >>/dev/null 2>&1 & echo $!")
        standardOutput = outputStream
    }
    return outputStream.toString().trim()
}

fun stopX(pid: String) {
    val outputStream = ByteArrayOutputStream()
    exec {
        commandLine("sh", "-c", "kill $pid &")
        standardOutput = outputStream
        errorOutput = outputStream
    }
}

// Function to detect if the OS is Linux
fun isLinux(): Boolean = System.getProperty("os.name").lowercase().contains("linux")

// Function to check if Xvfb is installed
fun isCommandAvailable(command: String): Boolean {
    val outputStream = ByteArrayOutputStream()
    return exec {
        commandLine("sh", "-c", "command -v " + command)
        isIgnoreExitValue = true // Don't fail if command is not found
        standardOutput = outputStream
        errorOutput = outputStream
    }.exitValue.equals(0)
}

fun makeTestTask(taskName: String, lang: String, country: String, display: String) {
    tasks.register<Test>(taskName) {
        systemProperty("user.language", lang)
        systemProperty("user.country", country)
        group = "verification"
        onlyIf {
            isLinux() && isCommandAvailable("Xvfb")
        }
        doFirst {
            val xvfbPid = startX(display)
            extensions.extraProperties["xvfbPid"] = xvfbPid
            environment["DISPLAY"] = display
            println("Virtual X server is started with DISPLAY $display and PID: $xvfbPid")
        }
        doLast {
            val pid = extensions.extraProperties["xvfbPid"] as String
            println("Stopping virtual X server for " + taskName + "...")
            stopX(pid)
        }
    }
    tasks.check {dependsOn(taskName)}
}
makeTestTask("testAr", "ar", "SA", ":99")
makeTestTask("testCa", "ca", "ES", ":100")
makeTestTask("testRu", "ru", "RU", ":101")
makeTestTask("testUk", "uk", "UK", ":102")

val ossrhUsername: String? by project
val ossrhPassword: String? by project

nexusPublishing.repositories {
    sonatype {
        stagingProfileId = "15818299f2c2bb"
        nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
        snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        if (ossrhUsername != null && ossrhPassword != null) {
            username.set(ossrhUsername)
            password.set(ossrhPassword)
        } else {
            username.set(System.getenv("SONATYPE_USER"))
            password.set(System.getenv("SONATYPE_PASSWORD"))
        }
    }
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
