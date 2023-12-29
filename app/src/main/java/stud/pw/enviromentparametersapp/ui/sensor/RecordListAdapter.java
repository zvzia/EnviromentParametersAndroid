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
import stud.pw.enviromentparametersapp.models.SurroundingConditions;

public class RecordListAdapter extends ArrayAdapter<SurroundingConditions> {
    ArrayList<SurroundingConditions> records;
    Context mContext;

    public RecordListAdapter(@NonNull Context context, ArrayList<SurroundingConditions> records) {
        super(context, R.layout.record_list_element, records);
        this.records = records;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SurroundingConditions record = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.record_list_element, parent, false);
        }

        // Lookup view for data population
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView temperature = (TextView) convertView.findViewById(R.id.temperature);
        TextView humidity = (TextView) convertView.findViewById(R.id.humidity);

        // Populate the data into the template view using the data object
        date.setText(record.getDateString(false));
        temperature.setText(record.getTemperature() + "°C");
        humidity.setText(record.getHumidity() + "%");

        // Return the completed view to render on screen
        return convertView;
    }
}
