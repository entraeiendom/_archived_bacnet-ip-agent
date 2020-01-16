package no.entra.bacnet.agent.devices;

import org.junit.Before;
import org.junit.Test;

import static no.entra.bacnet.json.utils.StringUtils.hasValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class DeviceIdServiceTest {

    private DeviceIdService deviceIdService;

    @Before
    public void setUp() throws Exception {
        DeviceIdRepository deviceIdRepository = mock(DeviceIdRepository.class);
        deviceIdService = new DeviceIdService(deviceIdRepository);
    }

    @Test
    public void createDeviceId() {
        DeviceId deviceId = deviceIdService.createDeviceId(null, null, null);
        assertNull(deviceId);
        deviceId = deviceIdService.createDeviceId(1, null, null);
        assertTrue(hasValue(deviceId.getId()));
        assertEquals(Integer.valueOf(1), deviceId.getInstanceNumber());
        deviceId = deviceIdService.createDeviceId(null, "127.0.0.1", null);
        assertTrue(hasValue(deviceId.getId()));
        assertEquals("127.0.0.1", deviceId.getIpAddress());
        deviceId = deviceIdService.createDeviceId(null, null, "tfm1234");
        assertTrue(hasValue(deviceId.getId()));
        assertEquals("tfm1234", deviceId.getTfmTag());
    }
}