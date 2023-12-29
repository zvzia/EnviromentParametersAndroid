package stud.pw.enviromentparametersapp.models;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SurroundingConditions {
    int id;
    float temperature;
    int humidity;
    int sensorId;
    Timestamp date;
    int batteryLevel;

    public SurroundingConditions(int id, float temperature, int humidity, int sensorId, Timestamp date, int batteryLevel) {
        this.id = id;
        this.temperature = temperature;
        this.humidity = humidity;
        this.sensorId = sensorId;
        this.date = date;
        this.batteryLevel = batteryLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public Date getDate() {
        return date;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getDateString(boolean oneLine){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String strDate = dateFormat.format(date);

        if(oneLine == true){
            return strDate;
        }

        String time = strDate.substring(0,5);
        String date = strDate.substring(6,16);

        String result = date + "\n" + time;

        return result;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
