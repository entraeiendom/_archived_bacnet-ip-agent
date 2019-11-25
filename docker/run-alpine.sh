#!/usr/bin/env bash
#docker run -it --name=bacnet-ip-agent-java <your docker user>/bacnet-ip-agent-java:alpine /bin/bash
DEVICE_CONNECTION_STRING=$0
DOCKER_USER=${1:-baardl}
NUMBER_REQUESTS=10
docker run --name=bacnet-ip-agent-java -e DEVICE_CONNECTION_STRING=$DEVICE_CONNECTION_STRING -e NUMBER_REQUESTS=$NUMBER_REQUESTS $DOCKER_USER/bacnet-ip-agent-java:alpine
