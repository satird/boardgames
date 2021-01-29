#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/mediaContainer-1.0.0.jar \
    satird@192.168.0.111:/home/satird

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa satird@192.168.0.111 << EOF
pgrep java | xargs kill -9
nohup java -jar mediaContainer-1.0.0.jar > log.txt &
EOF

echo 'Bye'
