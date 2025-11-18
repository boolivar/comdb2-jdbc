plugins {
    java
    `maven-publish`
    alias(libs.plugins.protobuf)
    alias(libs.plugins.shadow)
    alias(libs.plugins.versioning)
    alias(libs.plugins.publish)
}

group = "io.github.boolivar.comdb2"
description = "comdb2 jdbc driver"

val src by extra("comdb2/cdb2jdbc/src")
val protobufSrc by extra("comdb2/protobuf")

scmVersion {
    tag.prefix = ""
    useHighestVersion = true
    versionIncrementer("incrementMinorIfNotOnRelease", mapOf("releaseBranchPattern" to "release/.+"))
    branchVersionCreator.put("release/.*","simple")
    rootProject.version = version
}

repositories {
    mavenCentral()
}

tasks {
    compileJava {
        options.release = 8
    }
    javadoc {
        options {
            this as StandardJavadocDocletOptions
            addBooleanOption("Xdoclint:none", true) 
        }
    }
    shadowJar {
        enableAutoRelocation = true
        relocationPrefix = "com.bloomberg.comdb2.shadow"
        archiveClassifier = "standalone"
    }
    withType<Jar>() {
        into("META-INF") {
            from("LICENSE")
            from("comdb2/LICENSE") {
                into("licenses/comdb2")
            }
            from("comdb2/berkdb/LICENSE") {
                into("licenses/berkdb")
            }
            from("comdb2/crc32c/sb8.h") {
                into("licenses/crc32c")
            }
            from("comdb2/dfp/decNumber/ICU-license.html") {
                into("licenses/decNumber")
            }
            from("comdb2/dfp/dfpal/ICU-license.html") {
                into("licenses/dfpal")
            }
            from("comdb2/lua/lua.h") {
                into("licenses/lua")
            }
        }
    }
}

protobuf {
    protoc {
        artifact = libs.protoc.get().toString()
    }
}

sourceSets {
    listOf("sqlquery.proto", "sqlresponse.proto").forEach { protoFile ->
        create(protoFile.substringBefore(".")) {
            proto {
                srcDir(protobufSrc)
                setIncludes(setOf(protoFile))
            }
        }
    }
    main {
        java {
            srcDir("$src/main/java")
            srcDirs(tasks.withType<com.google.protobuf.gradle.GenerateProtoTask>())
        }
        resources {
            srcDir("$src/main/resources")
        }
    }
}

dependencies {
    implementation(libs.protobuf)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name = "$groupId:$artifactId"
                description = project.description
                url = "https://github.com/boolivar/comdb2-jdbc"
                inceptionYear = "2025"
                licenses {
                    license {
                        name = "MIT"
                        url = "https://raw.githubusercontent.com/boolivar/comdb2-jdbc/$version/LICENSE"
                        comments = "This distribution licensed under MIT License"
                    }
                    license {
                        name = "Apache-2.0"
                        url = "https://raw.githubusercontent.com/bloomberg/comdb2/main/LICENSE"
                        comments = "Original sources licensed under the Apache License, Version 2.0"
                    }
                }
                developers {
                    developer {
                        id = "boolivar"
                        name = "Aleksey Krichevskiy"
                        email = "boolivar@gmail.com"
                        organizationUrl = "https://github.com/boolivar"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/boolivar/comdb2-jdbc.git"
                    developerConnection = "scm:git:ssh://github.com:boolivar/comdb2-jdbc.git"
                    url = "https://github.com/boolivar/comdb2-jdbc"
                }
            }
        }
    }
}
