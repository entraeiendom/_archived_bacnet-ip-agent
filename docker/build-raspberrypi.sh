#!/bin/sh
docker run --rm --privileged multiarch/qemu-user-static:register --reset
docker build -t <your docker user>/bacnet-ip-agent-java:rasberypi4 -f DockerfileRaspberryPi .