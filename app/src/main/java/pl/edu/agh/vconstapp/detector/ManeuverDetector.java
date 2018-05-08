package pl.edu.agh.vconstapp.detector;

import pl.edu.agh.vconstapp.data.DataPoint;
import pl.edu.agh.vconstapp.data.Maneuver;
import pl.edu.agh.vconstapp.util.ManeuverRepository;
import pl.edu.agh.vconstapp.util.MultiStreamDataProvider;

public interface ManeuverDetector {
    void update(MultiStreamDataProvider<DataPoint> dataPointsMultiStream,
                MultiStreamDataProvider<Maneuver> maneuversMultiStream,
                ManeuverRepository repository);
}
