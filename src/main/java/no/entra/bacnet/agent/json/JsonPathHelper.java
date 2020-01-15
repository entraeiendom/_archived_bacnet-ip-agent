package no.entra.bacnet.agent.json;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.slf4j.Logger;

import java.util.HashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Attribution: https://github.com/Cantara/Whydah-TypeLib
 */
public class JsonPathHelper {
    private static final Logger log = getLogger(no.entra.bacnet.rec.json.JsonPathHelper.class);

    public static String getStringFailsafeNull(String jsonString, String expression) {
        String value = null;
        try {
            value = getStringFromJsonpathExpression(jsonString, expression);
        } catch (PathNotFoundException e) {
            //expected do nothing
        }
        return value;
    }
    public static String getStringFromJsonpathExpression(String jsonString, String expression) throws PathNotFoundException {
        String value = "";
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
        Object result = JsonPath.read(document, expression);
        if (result != null) {
            value = result.toString();
        }

        return value;
    }

    public static HashMap getMapFromJsonpathExpression(String jsonString, String expression) {
        HashMap value = null;
        try {
            Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
            value = JsonPath.read(document, expression);
        } catch (PathNotFoundException e) {
            log.trace("No value for {} in {}", expression, jsonString);
        }
        return value;
    }

    public static boolean hasElement(String jsonString, String expression) {

        boolean elementIsPresent = false;
        try {
            Object observation = JsonPath.read(jsonString,expression);
            if (observation != null) {
                elementIsPresent = true;
            }
        } catch (PathNotFoundException e) {
            log.trace("{} is missing from {}. This might be as expected.", expression, jsonString);
        }
        return elementIsPresent;
    }
}
