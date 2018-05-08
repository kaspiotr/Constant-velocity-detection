package pl.edu.agh.vconstapp.data;

import java.util.Date;
import java.util.Map;

public class Maneuver {
    private String mDeviceId;
    private Date mBeginning;
    private Date mEnd;
    private String mType;

    private Map<String, String> mParams;

    public Maneuver(String deviceId, Date beginning, Date end, String type, Map<String, String> params) {
        this.mDeviceId = deviceId;
        this.mBeginning = beginning;
        this.mEnd = end;
        this.mType = type;
        this.mParams = params;
    }
}
