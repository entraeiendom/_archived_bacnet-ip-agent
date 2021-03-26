package no.entra.bacnet.agent.importer;

import org.slf4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class ScheduledDeviceImporter {
    private static final Logger log = getLogger(ScheduledDeviceImporter.class);
    private static final int WAIT_DEVICES_TO_REPORT_IN_SEC = 15;

    private final DeviceImporter deviceImporter;
    private final int DEFAULT_SECONDS_BETWEEN_SCHEDULED_IMPORT_RUNS = 60*10;
    public static final String IMPORT_SCHEDULE_MINUTES_KEY = "import_schedule_minutes";
    private static boolean scheduled_import_started = false;
    private static boolean scheduled_import_running = false;
    private final int SECONDS_BETWEEN_SCHEDULED_IMPORT_RUNS;


    public ScheduledDeviceImporter(DeviceImporter deviceImporter) {
        this.deviceImporter = deviceImporter;
        Integer scheduleMinutes = findScheduledMinutes();
        if (scheduleMinutes != null) {
            SECONDS_BETWEEN_SCHEDULED_IMPORT_RUNS = scheduleMinutes * 60;
        } else {
            SECONDS_BETWEEN_SCHEDULED_IMPORT_RUNS = DEFAULT_SECONDS_BETWEEN_SCHEDULED_IMPORT_RUNS;
        }
    }

    private Integer findScheduledMinutes() {
        Integer scheduleMinutes = null;
        String scheduleMinutesValue = "10"; //TODO getProperty(IMPORT_SCHEDULE_MINUTES_KEY);
        if (scheduleMinutesValue != null) {
            try {
                scheduleMinutes = Integer.valueOf(scheduleMinutesValue);
            } catch (NumberFormatException nfe) {
                log.debug("Failed to create scheduledMinutes from [{}]", scheduleMinutesValue);
            }
        }
        return scheduleMinutes;
    }

    public void startScheduledImport() {
        if (!scheduled_import_started) {
            log.info("Starting ScheduledImportManager");

            scheduled_import_started = true;
            ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

            Runnable task1 = () -> {
                if (scheduled_import_running == false) {
                    log.info("Running an new import round.");

                    try {
                        scheduled_import_running = true;
                        deviceImporter.findDevices();
                        log.info("Running...find devices.");
                        log.trace("Wait {} seconds for devices to report in.", WAIT_DEVICES_TO_REPORT_IN_SEC);
                        Thread.sleep(WAIT_DEVICES_TO_REPORT_IN_SEC * 1000);
                        log.info("Find Sensors and Properties each Device Supports. ");
                        deviceImporter.findSensorsAndPropertiesTheDevicesSupports();
                        log.info("Find information about each Sensor and Property configuration. ");
                        deviceImporter.findSensorAndPropertiesConfiguration();
                        log.info("Find Present Value for each sensor.");
                        deviceImporter.findPresentValueForSensors();
                        log.info("Flushed. Now waiting {} seconds for next import run.", SECONDS_BETWEEN_SCHEDULED_IMPORT_RUNS);
                        scheduled_import_running = false;
                    } catch (Exception e) {
                        log.info("Exception trying to run scheduled imports of trendIds. Reason: {}", e.getMessage(), e);
                        scheduled_import_running = false;
                    }
                } else {
                    log.info("Last round of imports has not finished yet. ");
                }
            };

            // init Delay = 5, repeat the task every 60 second
            ScheduledFuture<?> scheduledFuture = ses.scheduleAtFixedRate(task1, 5, SECONDS_BETWEEN_SCHEDULED_IMPORT_RUNS, TimeUnit.SECONDS);
        } else {
            log.info("ScheduledImportManager is is already started");
        }
    }
}
