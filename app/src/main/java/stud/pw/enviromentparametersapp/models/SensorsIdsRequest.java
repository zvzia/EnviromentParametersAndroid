package stud.pw.enviromentparametersapp.models;

import java.util.Arrays;

public class SensorsIdsRequest {
    int[] sensorIds;

    public int[] getSensorIds() {
        return sensorIds;
    }

    public void setSensorIds(int[] sensorIds) {
        this.sensorIds = sensorIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorsIdsRequest that = (SensorsIdsRequest) o;
        return Arrays.equals(sensorIds, that.sensorIds);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(sensorIds);
    }
}
