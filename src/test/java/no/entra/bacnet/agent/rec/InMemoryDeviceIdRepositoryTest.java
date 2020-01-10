package no.entra.bacnet.agent.rec;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InMemoryDeviceIdRepositoryTest {

    private DeviceIdRepository idRepository;
    private DeviceId defaultDeviceId;
    public static final String DEFAULT_PORT = "47808";

    @Before
    public void setUp() throws Exception {
        idRepository = new InMemoryDeviceIdRepository();
        defaultDeviceId = new DeviceId("id1", "127.0.0.1", DEFAULT_PORT, "TFM123/456", 2002 );
        //String id, String ipAddress, String portNumber, String tfmTag, int instanceNumber
        idRepository.add(defaultDeviceId);
    }

    @Test
    public void add() {
        assertEquals(1, idRepository.size());
        idRepository.add(new DeviceId("id2", "127.0.0.1", DEFAULT_PORT, "TFM123/456", 2002));
        assertEquals(2, idRepository.size());
    }

    @Test(expected = IllegalStateException.class)
    public void addConflict() {
        idRepository.add(new DeviceId("id1"));
    }

    @Test
    public void findFromTfm() {
    }

    @Test
    public void findFromInstanceNumber() {
    }

    @Test
    public void findFromIpAddress() {
    }
}