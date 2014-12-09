@echo off
gradlew clean & gradlew setupDecompWorkspace & gradlew eclipse --refresh-dependencies & pause