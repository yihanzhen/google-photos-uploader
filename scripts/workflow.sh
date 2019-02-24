#!/bin/bash

# build
mvn install

# sanitize
java -cp /target/google-photos-uploader-1.0-SNAPSHOT-jar-with-dependencies.jar \
  uploader sanitize -dir fromdir -p processeddir -dest destdir

# upload
java -cp /target/google-photos-uploader-1.0-SNAPSHOT-jar-with-dependencies.jar \
  uploader upload -dir destdir/original/jpeg/ -ab original -t time -l location
java -cp /target/google-photos-uploader-1.0-SNAPSHOT-jar-with-dependencies.jar \
  uploader upload -dir destdir/processed/jpeg/ -ab processed -t time -l location
java -cp /target/google-photos-uploader-1.0-SNAPSHOT-jar-with-dependencies.jar \
  uploader upload -dir destdir/processed/raw/ -ab raw -t time -l location
