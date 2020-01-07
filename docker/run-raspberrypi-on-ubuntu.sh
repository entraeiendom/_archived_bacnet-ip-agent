#!/usr/bin/env bash
DOCKER_USER=${1:-.}
TAG=${2:-rasberypi4}
CONNECTION_STRING=${3:-connection-to-eg-azure-iot}
docker run -e DEVICE_CONNECTION_STRING=$CONNECTION_STRING -p 0.0.0.0:47808:47808 -p 0.0.0.0:47808:47808/udp --name=bacnet-ip-agent-java $DOCKER_USER/bacnet-ip-agent-java:$TAG
