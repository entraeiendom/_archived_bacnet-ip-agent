#!/usr/bin/env bash
docker run -p 0.0.0.0:47808:47808 -p 0.0.0.0:47808:47808/udp -it --name=bacnet-ip-agent-java <your docker user>/bacnet-ip-agent-java /bin/bash
