package stud.pw.enviromentparametersapp.models;

public class SensorConfigRequest {
    int sensorId;
    String sensorName;
    Integer measurementFreq;
    Float temperatureMax;
    Float temperatureMin;
    Integer humidityMax;
    Integer humidityMin;

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public Integer getMeasurementFreq() {
        return measurementFreq;
    }

    public void setMeasurementFreq(Integer measurementFreq) {
        this.measurementFreq = measurementFreq;
    }

    public Float getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Float temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public Float getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Float temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public Integer getHumidityMax() {
        return humidityMax;
    }

    public void setHumidityMax(Integer humidityMax) {
        this.humidityMax = humidityMax;
    }

    public Integer getHumidityMin() {
        return humidityMin;
    }

    public void setHumidityMin(Integer humidityMin) {
        this.humidityMin = humidityMin;
    }
}
