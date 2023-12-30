package stud.pw.enviromentparametersapp.models;

import java.util.Objects;

public class BatteryLevelResponse {
    int sensorId;
    Integer batteryLvl;

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public Integer getBatteryLvl() {
        return batteryLvl;
    }

    public void setBatteryLvl(Integer batteryLvl) {
        this.batteryLvl = batteryLvl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatteryLevelResponse that = (BatteryLevelResponse) o;
        return sensorId == that.sensorId && Objects.equals(batteryLvl, that.batteryLvl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorId, batteryLvl);
    }

    @Override
    public String toString() {
        return "BatteryLevelResponse{" +
                "sensorId=" + sensorId +
                ", batteryLvl=" + batteryLvl +
                '}';
    }
}
