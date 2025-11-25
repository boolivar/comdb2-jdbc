# comdb2-jdbc
[![CI](https://github.com/boolivar/comdb2-jdbc/workflows/CI/badge.svg)](https://github.com/boolivar/comdb2-jdbc/actions/workflows/ci.yml)
[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.boolivar.comdb2/comdb2-jdbc)](https://central.sonatype.com/artifact/io.github.boolivar.comdb2/comdb2-jdbc)
[![release](https://img.shields.io/github/v/release/boolivar/comdb2-jdbc)](https://github.com/boolivar/comdb2-jdbc/releases/latest)

This repository provides a Java JDBC driver build for [comdb2](https://github.com/bloomberg/comdb2?tab=readme-ov-file#overview).

## Maven Central coordinates
<!-- x-release-please-start-version -->
maven:
```xml
<dependency>
    <groupId>io.github.boolivar.comdb2</groupId>
    <artifactId>comdb2-jdbc</artifactId>
    <version>8.0.0</version>
</dependency>
```

gradle:
```gradle
implementation("io.github.boolivar.comdb2:comdb2-jdbc:8.0.0")
```
<!-- x-release-please-end -->
## Standalone distribution
For database managers such as DBeaver that expect a single JAR file, use a standalone (fat/uber) JAR that bundles this driver and any required dependencies.
Download from [releases page](https://github.com/boolivar/comdb2-jdbc/releases) or from [Maven Central](https://repo1.maven.org/maven2/io/github/boolivar/comdb2/comdb2-jdbc/) by `-standalone` classifier.

### Using the driver in DBeaver example
> [!NOTE]
> DBeaver supports maven dependencies with transitives. This is just a configuration example.  
> Use `Driver Manager` -> `Libraries` -> `Add Artifacts` to specify driver coordinates.

Download the standalone JAR.  
Open DBeaver -> `Database` -> `Driver Manager`.  
Click `New` to create a driver entry.  
Give it a name (e.g., comdb2).  
Add the standalone JAR file to the driver libraries.  
Driver class: click `Find Class` and select `com.bloomberg.comdb2.jdbc.Driver`.  
URL template (example pattern): `jdbc:comdb2://{host}[:{port}]/[{database}]`  
Create new database connection using comdb2 driver.

## License
Distribution and code in this repository licensed under the MIT License: [LICENSE](LICENSE)  
Original sources licensed under the Apache License, Version 2.0: [LICENSE](https://github.com/bloomberg/comdb2/blob/main/LICENSE)  
Standalone distribution includes protobuf-java binaries licensed under 3-Clause BSD License: [LICENSE](https://github.com/protocolbuffers/protobuf/blob/main/LICENSE) 

## Unofficial build notice
This is an unofficial build of Bloomberg comdb2 JDBC driver. It is not produced, maintained, endorsed, or supported by Bloomberg L.P. or any of their affiliates.
Use of this repository and any resulting binaries is at your own risk. You must ensure that you comply with any and all applicable third-party terms, licenses, and export controls that apply to the original sources or any bundled components.
