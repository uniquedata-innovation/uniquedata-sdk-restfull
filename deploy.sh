#!/bin/bash

echo "maven deploy uniquedata-restfull-sdk started";

mvn clean install -Pgithub-restfull-sdk-profile

mvn clean deploy -Pgithub-restfull-sdk-profile -X

echo "Finish success deploy!";


