package stud.pw.enviromentparametersapp.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import stud.pw.enviromentparametersapp.R;
import stud.pw.enviromentparametersapp.databinding.ActivityAddSensorBinding;
import stud.pw.enviromentparametersapp.databinding.ActivityConfigureSensorBinding;
import stud.pw.enviromentparametersapp.envParamClient.EnvParamClient;
import stud.pw.enviromentparametersapp.models.ArrayResponse;
import stud.pw.enviromentparametersapp.models.SensorConfigRequest;
import stud.pw.enviromentparametersapp.models.SensorConfigResponse;
import stud.pw.enviromentparametersapp.models.StringResponse;

public class ConfigureSensorActivity extends AppCompatActivity {

    private int sensorId;
    private SharedPreferences sharedPreferences;

    private ActivityConfigureSensorBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        binding = ActivityConfigureSensorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent mIntent = getIntent();
        sensorId = mIntent.getIntExtra("sensorId", 0);

        EditText newNameText = binding.newNameText;
        EditText measurementFreqEditText = binding.measurementFreqEditText;
        EditText maxTempEditText = binding.maxTempEditText;
        EditText minTempEditText = binding.minTempEditText;
        EditText maxHumidEditText = binding.maxHumidEditText;
        EditText minHumidEditText = binding.minHumidEditText;
        Switch maxTempSwitch = binding.maxTempSwitch;
        Switch minTempSwitch = binding.minTempSwitch;
        Switch maxHumidSwitch = binding.maxHumidSwitch;
        Switch minHumidSwitch = binding.minHumidSwitch;

        EnvParamClient envParamClient = new EnvParamClient(this);
        try {
            envParamClient.getSensorConfig(sensorId, new EnvParamClient.VolleyCallbackSensorConfigResponse(){
                @Override
                public void onSuccess(SensorConfigResponse result) {
                    setTitle("Configure "+ result.getSensorName());

                    newNameText.setText(result.getSensorName());
                    measurementFreqEditText.setText(String.valueOf(result.getMeasurementFreq()));

                    if(result.getTemperatureMax() == null){
                        maxTempSwitch.setChecked(false);
                    }else{
                        maxTempSwitch.setChecked(true);
                        maxTempEditText.setText(String.valueOf(result.getTemperatureMax()));
                    }

                    if(result.getTemperatureMin() == null){
                        minTempSwitch.setChecked(false);
                    }else{
                        minTempSwitch.setChecked(true);
                        minTempEditText.setText(String.valueOf(result.getTemperatureMin()));
                    }

                    if(result.getHumidityMax() == null){
                        maxHumidSwitch.setChecked(false);
                    }else{
                        maxHumidSwitch.setChecked(true);
                        maxHumidEditText.setText(String.valueOf(result.getHumidityMax()));
                    }

                    if(result.getHumidityMin() == null){
                        minHumidSwitch.setChecked(false);
                    }else{
                        minHumidSwitch.setChecked(true);
                        minHumidEditText.setText(String.valueOf(result.getHumidityMin()));
                    }
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public void saveSensorConfig(View view) {
        EditText newNameText = binding.newNameText;
        EditText measurementFreqEditText = binding.measurementFreqEditText;
        EditText maxTempEditText = binding.maxTempEditText;
        EditText minTempEditText = binding.minTempEditText;
        EditText maxHumidEditText = binding.maxHumidEditText;
        EditText minHumidEditText = binding.minHumidEditText;
        Switch maxTempSwitch = binding.maxTempSwitch;
        Switch minTempSwitch = binding.minTempSwitch;
        Switch maxHumidSwitch = binding.maxHumidSwitch;
        Switch minHumidSwitch = binding.minHumidSwitch;

        SensorConfigRequest sensorConfigRequest = new SensorConfigRequest();
        sensorConfigRequest.setSensorId(sensorId);
        sensorConfigRequest.setSensorName(newNameText.getText().toString());
        sensorConfigRequest.setMeasurementFreq(Integer.valueOf(measurementFreqEditText.getText().toString()));

        if(maxTempSwitch.isChecked()){
            sensorConfigRequest.setTemperatureMax(Double.valueOf(maxTempEditText.getText().toString()));
        }else{
            sensorConfigRequest.setTemperatureMax(null);
        }

        if(minTempSwitch.isChecked()){
            sensorConfigRequest.setTemperatureMin(Double.valueOf(minTempEditText.getText().toString()));
        }else{
            sensorConfigRequest.setTemperatureMin(null);
        }

        if(maxHumidSwitch.isChecked()){
            sensorConfigRequest.setHumidityMax(Integer.valueOf(maxHumidEditText.getText().toString()));
        }else{
            sensorConfigRequest.setHumidityMax(null);
        }

        if(minHumidSwitch.isChecked()){
            sensorConfigRequest.setHumidityMin(Integer.valueOf(minHumidEditText.getText().toString()));
        }else{
            sensorConfigRequest.setHumidityMin(null);
        }


        EnvParamClient envParamClient = new EnvParamClient(this);
        try {
            envParamClient.setSensorConfig(sensorConfigRequest, new EnvParamClient.VolleyCallbackStringResponse() {
                @Override
                public void onSuccess(StringResponse result) {
                    Toast.makeText(getApplicationContext(), "Changes saved", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

}