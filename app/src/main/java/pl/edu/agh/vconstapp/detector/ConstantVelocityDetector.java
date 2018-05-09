package pl.edu.agh.vconstapp.detector;

import android.util.Pair;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

    private static final String DEVICE_MOCK_ID = "device_id";

    private static final String VELOCITY_TAG = "s";
    private static final String VELOCITY_ACCURACY_TAG = "s_acc";

    private static final double ACCELERATION_ERROR = 0.5; // guessed number

    private final List<Maneuver> mDetectedManeuvers;

    private Pair<Double, Date> mLastConstantVelocityAndStartTimestamp;
    private double mAcceleration;

    public ConstantVelocityDetector() {
        mDetectedManeuvers = new LinkedList<>();
        resetSavedLastConstantVelocity();
    }

    @Override
    public void update(MultiStreamDataProvider<DataPoint> dataPointsMultiStream,
                       MultiStreamDataProvider<Maneuver> maneuversMultiStream,
                       ManeuverRepository repository) {

        resetDetectedManeuvers();

        if (dataPointsMultiStream.containsStreamTag(FUSED_LOCATION_TAG)) {
            detectManeuversFromLocation(dataPointsMultiStream.getStream(FUSED_LOCATION_TAG));
        } else if (dataPointsMultiStream.containsStreamTag(GPS_LOCATION_TAG)) {
            detectManeuversFromLocation(dataPointsMultiStream.getStream(GPS_LOCATION_TAG));
        } else if (dataPointsMultiStream.containsStreamTag(ACCELEROMETER_TAG)) {
            detectManeuverFromAccelerometer(dataPointsMultiStream.getStream(ACCELEROMETER_TAG));
        }

        if (mDetectedManeuvers.size() > 0) {
            for (Maneuver detectedManeuver : mDetectedManeuvers) {
                repository.addManeuver(detectedManeuver);
            }
        }
    }

    private void detectManeuversFromLocation(StreamDataProvider<DataPoint> locationStream) {
        final int lastSeenIndex = locationStream.lastSeen();
        if (lastSeenIndex > 0) {
            startLocationDataAnalysisFromIndex(locationStream, lastSeenIndex);
        } else {
            resetSavedLastConstantVelocity();
            startLocationDataAnalysisFromIndex(locationStream, 0);
        }
    }

    private void startLocationDataAnalysisFromIndex(StreamDataProvider<DataPoint> locationStream, int index) {
        final int streamSize = locationStream.size();

        DataPoint earlierDataPoint;
        DataPoint laterDataPoint;
        for (int i = index; i < streamSize - 1; i++) {
            earlierDataPoint = locationStream.get(i);
            laterDataPoint = locationStream.get(i + 1);
            checkIfConstantVelocityAndUpdateState(getAccelerationBetweenTwoDataPoints(earlierDataPoint, laterDataPoint),
                                                  earlierDataPoint);
        }
    }

    private void detectManeuverFromAccelerometer(StreamDataProvider<DataPoint> accelerometerStream) {
    }

    private double getAccelerationBetweenTwoDataPoints(DataPoint earlierDataPoint, DataPoint laterDataPoint) {
        final double earlierVelocity = Double.valueOf(earlierDataPoint.getValue(VELOCITY_TAG));
        final double laterVelocity = Double.valueOf(laterDataPoint.getValue(VELOCITY_TAG));
        final long time = getDifferenceBetweenDatesInSeconds(earlierDataPoint.getTimestamp(), laterDataPoint.getTimestamp());

        return calculateAcceleration(earlierVelocity, laterVelocity, time);
    }

    private double calculateAcceleration(double v1, double v2, long time) {
        return (v2 - v1) / time;
    }

    private long getDifferenceBetweenDatesInSeconds(Date earlierDate, Date laterDate) {
        long difference = laterDate.getTime() - earlierDate.getTime();
        return (long) (difference * 0.001);
    }

    private void checkIfConstantVelocityAndUpdateState(double acceleration, DataPoint dataPoint) {
        if (Math.abs(acceleration - this.mAcceleration) <= ACCELERATION_ERROR) {
            updateAccelerationAndLastConstantVelocity(acceleration, dataPoint);
        } else {
            mDetectedManeuvers.add(new Maneuver(DEVICE_MOCK_ID,
                                                mLastConstantVelocityAndStartTimestamp.second,
                                                dataPoint.getTimestamp(),
                                                CONSTANT_VELOCITY_MANEUVER_TAG,
                                                null)); // TODO: add params
        }
    }

    private void resetDetectedManeuvers() {
        this.mDetectedManeuvers.clear();
    }

    private void resetSavedLastConstantVelocity() {
        this.mLastConstantVelocityAndStartTimestamp = null;
    }

    private void updateAccelerationAndLastConstantVelocity(double acceleration, DataPoint dataPoint) {
        this.mAcceleration = acceleration;
        this.mLastConstantVelocityAndStartTimestamp =
                new Pair<>(Double.valueOf(dataPoint.getValue(VELOCITY_TAG)),
                           dataPoint.getTimestamp());
    }
}
