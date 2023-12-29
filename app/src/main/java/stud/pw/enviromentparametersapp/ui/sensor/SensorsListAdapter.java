package stud.pw.enviromentparametersapp.ui.sensor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import stud.pw.enviromentparametersapp.R;
import stud.pw.enviromentparametersapp.models.Sensor;

public class SensorsListAdapter extends ArrayAdapter<Sensor> {
    ArrayList<Sensor> sensors;
    Context mContext;

    public SensorsListAdapter(@NonNull Context context, ArrayList<Sensor> sensors) {
        super(context, R.layout.sensors_list_element, sensors);
        this.sensors = sensors;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Sensor sensor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sensors_list_element, parent, false);
        }

        // Lookup view for data population
        TextView sensorName = (TextView) convertView.findViewById(R.id.sensorName);

        // Populate the data into the template view using the data object
        sensorName.setText(sensor.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}
