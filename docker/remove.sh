#!/usr/bin/env bash
echo stopping bacnet-ip-agent-java
docker stop bacnet-ip-agent-java
echo removing bacnet-ip-agent-java
docker rm bacnet-ip-agent-java
echo list active docker containers
docker ps
