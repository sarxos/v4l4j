# V4L4J

This is mavenized and customized fork of original [V4L4J](http://code.google.com/p/v4l4j) by Gilles Gigan. It contains only Java source code (no natives) and *.so objects precompiled for several architectures:

* Linux 64-bit,
* Linux ARM (hard float).

In case you need more architectures please compile binaries by following [this](https://code.google.com/p/v4l4j/wiki/SourceInstall) procedure. 

Source revision [r507](http://code.google.com/p/v4l4j/source/detail?r=507).

## Maven

```xml
<repository>
    <id>SarXos Repository</id>
    <url>http://www.sarxos.pl/repo/maven2</url>
</repository>
```

```xml
<dependency>
    <groupId>com.github.sarxos</groupId>
    <artifactId>v4l4j</artifactId>
    <version>0.9.1-r507</version>
</dependency>
```

## License

The code is distributed under the terms of GNU GPL v3.