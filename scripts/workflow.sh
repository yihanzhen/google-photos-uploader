#!/bin/bash

# handle arguments
if [ "${#}" -ne 5 ]; then
  echo "Usage: workflow fromdir processeddir destdir time location"
  exit 1
fi
fromdir=$1
processeddir=$2
destdir=$3
time=$4
location=$5

# build
mvn install
jarname=target/google-photos-uploader-1.0-SNAPSHOT-jar-with-dependencies.jar
mainclass=com.hzyi.google.photos.uploader.UploaderMain

# sanitize
java -cp "${jarname}" "${mainclass}" sanitize -r -dir "${fromdir}" -p "${processeddir}" -dest "${destdir}"
if [ "${?}" -ne 0 ]; then
  echo "sanitize failed"
  exit 1
fi

# upload
java -cp "${jarname}" "${mainclass}" upload -a -dir "${destdir}/original/jpeg/" -ab original -t "${time}" -l "${location}"
if [ "${?}" -ne 0 ]; then
  echo "upload original failed"
  exit 1
fi

java -cp "${jarname}" "${mainclass}" upload -a -dir "${destdir}/processed/jpeg/" -ab processed -t "${time}" -l "${location}"
if [ "${?}" -ne 0 ]; then
  echo "upload processed failed"
  exit 1
fi

java -cp "${jarname}" "${mainclass}" upload -a -dir "${destdir}/processed/raw/" -ab raw -t "${time}" -l "${location}"
if [ "${?}" -ne 0 ]; then
  echo "upload raw failed"
  exit 1
fi
