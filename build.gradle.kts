plugins {
    java
    alias(libs.plugins.protobuf)
    alias(libs.plugins.shadow)
    alias(libs.plugins.versioning)
}

group = "io.github.boolivar.comdb2"

val src by extra("comdb2/cdb2jdbc/src")
val protobufSrc by extra("comdb2/protobuf")

scmVersion {
    tag.prefix = ""
    useHighestVersion = true
    versionIncrementer("incrementMinorIfNotOnRelease", mapOf("releaseBranchPattern" to "release/.+"))
    branchVersionCreator.putAll(mapOf("release/.*" to "simple"))
    rootProject.version = version
}

repositories {
    mavenCentral()
}

tasks {
    compileJava {
        options.release = 8
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
