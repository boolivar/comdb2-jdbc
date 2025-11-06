plugins {
    java
    alias(libs.plugins.protobuf)
}

val src by extra("comdb2/cdb2jdbc/src")
val protobufSrc by extra("comdb2/protobuf")

repositories {
    mavenCentral()
}

tasks {
    compileJava {
        options.release = 8
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
