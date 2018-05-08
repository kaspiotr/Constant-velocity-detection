package pl.edu.agh.vconstapp.data;

import java.util.Date;
import java.util.Map;

public class DataPoint {
    private Date mTimestamp;
    private String mSensor;

    private Map<String, String> mParams;

    public DataPoint(Date timestamp, String sensor, Map<String, String> params) {
        this.mTimestamp = timestamp;
        this.mSensor = sensor;
        this.mParams = params;
    }

    public Date getTimestamp(){
        return this.mTimestamp;
    }
    public String getSensor(){
        return this.mSensor;
    }
    public String getValue(String label){
        if (mParams.containsKey(label)) {
            return mParams.get(label);
        }

        return null;
    }

}
