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
