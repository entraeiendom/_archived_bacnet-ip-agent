package no.entra.bacnet.agent.sensors;

import org.junit.Test;

public class BacnetJsonSensorIdParserTest {

    @Test
    public void parse() {
        String bacnetJson = "{\"sender\":\"unknown\",\"service\":\"GetAlarmSummary\",\"observation\":{\"unit\":\"DegreesCelcius\",\"observedAt\":\"2020-01-10T12:46:35.072037\",\"name\":\"Oslo1-NAE4/FCB.434_101-1OU001.RT001\",\"description\":\"Room and section \",\"source\":{\"deviceId\":\"TODO\",\"objectId\":\"AnalogInput 3000047\"},\"value\":22.170061}}";

    }
}