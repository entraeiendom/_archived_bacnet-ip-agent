# backnetIp-agent
Listen to UDP packages, and send UDP packages.

## Installation

Deviation from convention :-(
Checkout https://github.com/entraeiendom/bacnet-2-json
mvn clean install on that project
then

`mvn clean install`

Sorry

## How to Run

```
mvn clean install
java -jar target/bacnet-ip-agent-*.jar
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
