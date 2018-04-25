package pl.edu.agh.vconstapp;

public interface ManeuverDetector {
    void update(MultiStreamDataProvider<DataPoint> dataPointsMultiStream,
                MultiStreamDataProvider<Maneuver> maneuversMultiStream,
                ManeuverRepository repository);
}
