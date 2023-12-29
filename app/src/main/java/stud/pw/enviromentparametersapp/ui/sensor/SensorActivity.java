package stud.pw.enviromentparametersapp.ui.sensor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import stud.pw.enviromentparametersapp.R;
import stud.pw.enviromentparametersapp.envParamClient.EnvParamClient;
import stud.pw.enviromentparametersapp.models.BatteryLevelResponse;
import stud.pw.enviromentparametersapp.models.Sensor;
import stud.pw.enviromentparametersapp.models.SurroundingConditions;

public class SensorActivity extends AppCompatActivity {

    private ArrayList<SurroundingConditions> surroundingConditions;
    private int sensorId;

    Date start;
    Date end;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Intent mIntent = getIntent();
        sensorId = mIntent.getIntExtra("sensorId", 0);

        Calendar cal = Calendar.getInstance();
        //Date end = cal.getTime();
        end = cal.getTime();
        cal.add(Calendar.DATE, -30);
        //Date start = cal.getTime();
        start = cal.getTime();


        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButtonView);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    loadFragment(RecordListFragment.newInstance(sensorId, start, end));
                } else {
                    // The toggle is disabled
                    loadFragment(ChartsFragment.newInstance(sensorId, start, end));
                }
            }
        });

        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefreshSensor);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                pullToRefresh.setRefreshing(false);
            }
        });

        loadData();

    }

    private void loadData(){
        EnvParamClient envParamClient = new EnvParamClient(this);
        try {
            envParamClient.getSensorById(sensorId, new EnvParamClient.VolleyCallbackSensorResponse(){
                @Override
                public void onSuccess(Sensor result) {
                    setTitle(result.getName());
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            envParamClient.getLastBatteryLevel(sensorId, new EnvParamClient.VolleyCallbackBatteryLevelResponse(){
                @Override
                public void onSuccess(BatteryLevelResponse result) {
                    TextView batteryLvl = findViewById(R.id.batteryLvlText);
                    batteryLvl.setText("Battery level: " + result.getBatteryLvl() + "%");
                }

            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButtonView);
        if (toggle.isChecked()){
            loadFragment(RecordListFragment.newInstance(sensorId, start, end));
        } else {
            loadFragment(ChartsFragment.newInstance(sensorId, start, end));
        }
    }

    private void loadFragment(Fragment fragment) {
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    public void showDatePicker(View view) {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                start = new Date(selection.first);
                end = new Date(selection.second);

                start = dayStart(start);
                end = dayEnd(end);

                ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButtonView);
                if (toggle.isChecked()){
                    loadFragment(RecordListFragment.newInstance(sensorId, start, end));
                } else {
                    loadFragment(ChartsFragment.newInstance(sensorId, start, end));
                }

            }
        });

    }

    private static Date dayStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private static Date dayEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}