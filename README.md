# cpcl-sdk-java
[![Maven Central](https://img.shields.io/maven-central/v/com.lingmoyun/cpcl-sdk-java.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.lingmoyun%20AND%20a:cpcl-sdk-java)
[![GitHub release](https://img.shields.io/github/release/lingmoyun/cpcl-sdk-java.svg)](https://github.com/lingmoyun/cpcl-sdk-java/releases)
[![License](https://img.shields.io/badge/license-MIT-4EB1BA.svg)](https://www.opensource.org/licenses/mit-license.php)

CPCL指令SDK Java版



## Download

- [maven][1]
- [the latest JAR][2]

[1]: https://repo1.maven.org/maven2/com/lingmoyun/cpcl-sdk-java/
[2]: https://search.maven.org/remote_content?g=com.lingmoyun&a=cpcl-sdk-java&v=LATEST

## Maven

```xml
<dependency>
    <groupId>com.lingmoyun</groupId>
    <artifactId>cpcl-sdk-java</artifactId>
    <version>0.2.0</version>
</dependency>
```

## Quick Start

```java
// A4系列机器
byte[] cpcl = CpclBuilder.createArea(0, 203, 1680, 2374, 1) // 203DPI
        //.taskId("1") // 任务ID，这里传什么，打印结果就会携带什么，如果不需要打印结果注释这一行即可
        //.imageGG(0, 0, "/path/to/test.jpg")
        .imageGG(0, 0, ImageIO.read(Files.newInputStream(Paths.get("/path/to/test.jpg"))))
        .formPrint()
        .cut(0) // 切刀指令，切纸，无切刀机器不受影响
        .build();
```

```java
// 面单、票据系列机器
byte[] cpcl = CpclBuilder.createArea(0, 203, 1680, 2374, 1) // 203DPI
        .text(0, 0, 500, 100, "Hello World!")
        .line(100, 100, 300, 100, 1)
        .barCode(1, 1, 100, 100, 400, "A43009200005")
        .qrCode(100, 600, "https://github.com/lingmoyun/cpcl-sdk-java")
        .imageEG(100, 800, "/path/to/test.jpg")
        .imageCG(400, 800, ImageIO.read(Files.newInputStream(Paths.get("/path/to/test.jpg"))))
        .formPrint()
        .build();
```

## More

更多用法，见 [Example](https://github.com/lingmoyun/cpcl-sdk-java/blob/main/src/test/java/com/lingmoyun/example/CpclExample.java) 。

