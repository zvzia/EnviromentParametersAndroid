package stud.pw.enviromentparametersapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import java.util.ArrayList;

import stud.pw.enviromentparametersapp.models.Sensor;
import stud.pw.enviromentparametersapp.models.SurroundingConditions;

public class LastUpdateListAdapter extends ArrayAdapter<Sensor> {
    ArrayList<SurroundingConditions> records;
    Context mContext;

    public LastUpdateListAdapter(@NonNull Context context, ArrayList<Sensor> sensors, ArrayList<SurroundingConditions> records) {
        super(context, R.layout.home_list_element, sensors);
        this.records = records;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Sensor sensor = getItem(position);
        SurroundingConditions surroundingConditions = records.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_list_element, parent, false);
        }

        // Lookup view for data population
        TextView sensorName = (TextView) convertView.findViewById(R.id.sensorName);
        TextView temperature = (TextView) convertView.findViewById(R.id.temperature);
        TextView humidity = (TextView) convertView.findViewById(R.id.humidity);
        TextView batteryLvl = (TextView) convertView.findViewById(R.id.batteryLvl);

        // Populate the data into the template view using the data object
        sensorName.setText(sensor.getName());
        if (surroundingConditions != null) {
            temperature.setText(String.valueOf(surroundingConditions.getTemperature()) + "°C");
            humidity.setText(String.valueOf(surroundingConditions.getHumidity()) + "%");
            batteryLvl.setText(String.valueOf(surroundingConditions.getBatteryLevel()) + "%");
        }
        // Return the completed view to render on screen
        return convertView;
    }


}
