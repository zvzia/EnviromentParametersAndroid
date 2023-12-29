package stud.pw.enviromentparametersapp.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;

import stud.pw.enviromentparametersapp.R;
import stud.pw.enviromentparametersapp.envParamClient.EnvParamClient;
import stud.pw.enviromentparametersapp.models.ArrayResponse;
import stud.pw.enviromentparametersapp.models.Sensor;

public class EditSensorNamesActivity extends AppCompatActivity {

    ArrayList<Sensor> sensorsList;
    ListView list;
    private static EditSensorNamesListAdapter adapter;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sensor_names);

        EnvParamClient envParamClient = new EnvParamClient(this);
        try {
            envParamClient.getSensorsList(new EnvParamClient.VolleyCallbackArrResponse(){
                @Override
                public void onSuccess(ArrayResponse result) {
                    sensorsList = result.getElements();

                    displayList();
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        setTitle("Configure sensors");
    }


    private void displayList(){
        if(sensorsList != null){
            list = (ListView) findViewById(R.id.editSensorNamesList);
            adapter = new EditSensorNamesListAdapter(this, sensorsList);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Sensor sensor = sensorsList.get(i);

                    Intent mIntent = new Intent(getBaseContext() , ConfigureSensorActivity.class);
                    mIntent.putExtra("sensorId", sensor.getId());
                    startActivityForResult(mIntent, 1); //TODO zmienić to bo deprecated
                }
            });
        }
    }
}