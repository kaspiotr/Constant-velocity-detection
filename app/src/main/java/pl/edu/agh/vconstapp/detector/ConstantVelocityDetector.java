package pl.edu.agh.vconstapp.detector;

import pl.edu.agh.vconstapp.data.DataPoint;
import pl.edu.agh.vconstapp.data.Maneuver;
import pl.edu.agh.vconstapp.util.ManeuverRepository;
import pl.edu.agh.vconstapp.util.MultiStreamDataProvider;
import pl.edu.agh.vconstapp.util.StreamDataProvider;

public class ConstantVelocityDetector implements ManeuverDetector {

    private static final String CONSTANT_VELOCITY_MANEUVER_TAG = "CONSTANT_VELOCITY";

    private static final String FUSED_LOCATION_TAG = "FUSED_LOCATION";
    private static final String GPS_LOCATION_TAG = "GPS_LOCATION";
    private static final String ACCELEROMETER_TAG = "ACCELEROMETER";

    @Override
    public void update(MultiStreamDataProvider<DataPoint> dataPointsMultiStream,
                       MultiStreamDataProvider<Maneuver> maneuversMultiStream,
                       ManeuverRepository repository) {

        Maneuver detectedManeuver = null;

        if (dataPointsMultiStream.containsStreamTag(FUSED_LOCATION_TAG)) {
            detectedManeuver = getManeuverFromLocation(dataPointsMultiStream.getStream(FUSED_LOCATION_TAG));
        } else if (dataPointsMultiStream.containsStreamTag(GPS_LOCATION_TAG)) {
            detectedManeuver = getManeuverFromLocation(dataPointsMultiStream.getStream(GPS_LOCATION_TAG));
        } else if (dataPointsMultiStream.containsStreamTag(ACCELEROMETER_TAG)) {
            detectedManeuver = getManeuverFromAccelerometer(dataPointsMultiStream.getStream(ACCELEROMETER_TAG));
        }

        if (detectedManeuver != null) {
            repository.addManeuver(detectedManeuver);
        }
    }

    private Maneuver getManeuverFromLocation(StreamDataProvider<DataPoint> locationStream) {
        return null;
    }

    private Maneuver getManeuverFromAccelerometer(StreamDataProvider<DataPoint> accelerometerStream) {
        return null;
    }
}
