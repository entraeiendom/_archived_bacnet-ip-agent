# backnetIp-agent
Listen to UDP packages, and send UDP packages.

## How to Run

```
mvn clean install
java -DDEVICE_CONNECTION_STRING="Hostname=....<from portal.azure.com>" -jar target/bacnet-ip-agent-*.jar 
``` 

## Docker

### Ubuntu

Update <your docker user> in all docker/*.sh files.

```
mvn clean install
docker/build-alpine-amd64.sh
docker/run-alpine.sh
```
### Raspberry PI


```
//TODO Support for Java 13
```
## Echo example

UDP might be hard to understand. 
Please have a look at [UDPTest] to get a quick intro.
