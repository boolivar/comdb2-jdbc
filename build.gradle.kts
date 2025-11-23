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
val comdb2Version = file(".gitmodules").readLines().find { it.contains("branch") }!!.split('=')[1].trim()

scmVersion {
    tag.prefix = ""
    useHighestVersion = true
    versionIncrementer("incrementMinorIfNotOnRelease", mapOf("releaseBranchPattern" to "release/.+"))
    branchVersionCreator.put("release/.*","simple")
    rootProject.version = version
}

sonatypePublish {
    autoPublish = properties["mavenCentralAutoPublish"]?.toString().toBoolean()
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
        into("META-INF") {
            from(resources.text.fromUri("https://raw.githubusercontent.com/protocolbuffers/protobuf/v${libs.protobuf.get().version}/LICENSE")) {
                rename { "LICENSE" }
                into("licenses/protobuf")
            }
        }
    }
    withType<Jar>() {
        into("META-INF") {
            from("LICENSE")
            from("comdb2/LICENSE") {
                into("licenses/comdb2")
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
                        url = "https://raw.githubusercontent.com/bloomberg/comdb2/$comdb2Version/LICENSE"
                        comments = "Original sources licensed under the Apache License, Version 2.0"
                    }
                    license {
                        name = "BSD-3-Clause"
                        url = "https://raw.githubusercontent.com/protocolbuffers/protobuf/v${libs.protobuf.get().version}/LICENSE"
                        comments = "Standalone distribution includes protobuf-java binaries licensed under 3-Clause BSD License"
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
