package no.entra.bacnet.agent.json;

import org.junit.Test;

import java.util.HashMap;

import static no.entra.bacnet.agent.json.JsonPathHelper.*;
import static org.junit.Assert.*;

public class JsonPathHelperTest {

    private static String bacnetJson = "{\n" +
            "  \"configurationRequest\": {\n" +
            "    \"observedAt\": \"2020-01-13T14:25:36.433205\",\n" +
            "    \"id\": \"TODO\",\n" +
            "    \"source\": \"0961\",\n" +
            "    \"properties\": {\n" +
            "      \"Request\": \"IHave\",\n" +
            "      \"NotificationClass\": \"0\",\n" +
            "      \"Device\": \"11\",\n" +
            "      \"ObjectName\": \"tfmtag-example\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"sender\": {\n" +
            "    \"gateway\": {\n" +
            "      \"instanceNumber\": 2401,\n" +
            "      \"deviceId\": 11\n" +
            "    }\n" +
            "  },\n" +
            "  \"service\": \"IHave\"\n" +
            "}";

    @Test
    public void getStringFailsafeNullTest() {
        assertEquals("IHave", getStringFailsafeNull(bacnetJson, "$.service"));
        assertEquals(null, getStringFailsafeNull(bacnetJson, "$.non-existing"));
    }

    @Test
    public void getStringFromJsonpathExpressionTest() {
        assertEquals("IHave", getStringFromJsonpathExpression(bacnetJson, "$.service"));
    }

    @Test
    public void getMapFromJsonpathExpressionTest() {
        HashMap parsed = getMapFromJsonpathExpression(bacnetJson, "$.sender");
        assertNotNull(parsed);
        assertNotNull(parsed.get("gateway"));
    }

    @Test
    public void hasElementTest() {
        assertTrue(hasElement(bacnetJson, "$"));
        assertFalse(hasElement(bacnetJson, "$.non-existing"));
        assertTrue(hasElement(bacnetJson, "$.sender"));
        assertTrue(hasElement(bacnetJson, "$.service"));
    }
}