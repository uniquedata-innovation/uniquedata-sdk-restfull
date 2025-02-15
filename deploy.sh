#!/bin/bash

echo "maven deploy uniquedata-restfull-sdk started";

mvn clean

sleep 1;

mvn install

sleep 1;

mvn deploy -Pgithub-restfull-sdk-profile

echo "Finish success deploy!";


