# MapViewer

[![Build Status](https://travis-ci.org/keenan-v1/MapViewer.svg?branch=master)](https://travis-ci.org/keenan-v1/MapViewer)
[![GitHub issues](https://img.shields.io/github/issues/keenan-v1/MapViewer.svg)](https://github.com/keenan-v1/MapViewer/issues)
[![GitHub version](https://badge.fury.io/gh/keenan-v1%2FMapViewer.svg)](https://github.com/keenan-v1/MapViewer/releases/latest)

Opens maps created by WurmAPI

# Latest Update (1.4.0)
- CLI via new flags
- Localization (no translations yet)

## Requirements
### Running MapViewer
- [Java 8 (64-bit)](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)

### Developing MapViewer
- [Java 8 (64-bit) JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven](http://maven.apache.org/install.html)

## To Use
- Click [here](https://github.com/xorith/MapViewer/releases/latest) for the latest release and download the MapViewer jar.
- For most users, double-clicking the jar will run the program if Java is installed correctly on your system.

## To Develop
- Install Java 8 JDK and Maven. Recommended using an IDE such as [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) or [Eclipse](https://www.eclipse.org/downloads/)
- `git clone https://github.com/xorith/MapViewer.git`
- Make sure to use `mvn validate` before `mvn install`

__You must run `mvn validate` prior to `mvn install` in order to install `./lib/common.jar` as it is not hosted in a repository yet!__

