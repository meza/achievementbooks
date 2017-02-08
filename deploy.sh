#!/usr/bin/env bash

export RELEASE=true
./gradlew curseforge -Pcurseforge_key=${curseforge_key} -Prelease_type="$1" -Ptag_name="$2" -Pbuild_number=$3
