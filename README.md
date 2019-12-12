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
## Simulator

We provide a simulator for easy coding of the functionality you need. No need for working with a live Bacnet. 
The simulator is found in no.entra.bacnet.agent.simulators.BacnetUdpSimulator.

Running the simulator is easy. Start the main method from your IDE. The simulator will then
put a WhoIs Bacnet message to your local net. 
If you want to run a specific Bacnet message, you may provide a HexString as the first program parameter.
