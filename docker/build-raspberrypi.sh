#!/bin/sh
DOCKER_USER=${1:-.}
TAG=${2:rasberypi4}
docker run --rm --privileged multiarch/qemu-user-static:register --reset
echo Building $DOCKER_USER/bacnet-ip-agent-java:$TAG
docker build -t $DOCKER_USER/bacnet-ip-agent-java:$TAG -f DockerfileRaspberryPi4 .