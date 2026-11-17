
#!/bin/bash
set -e

echo "Clean..."
./gradlew clean

echo "Generate FullDebug and FullRelease APKs..."
./gradlew assembleFullDebug assembleFullRelease

echo "Generate FossDebug and FossRelease APKs..."
./gradlew -PbuildFoss assembleFossDebug assembleFossRelease

echo "Copying APKs..."
./gradlew copyAPKs

echo "Finish."
