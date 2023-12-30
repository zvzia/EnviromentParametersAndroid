package stud.pw.enviromentparametersapp.ui.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import stud.pw.enviromentparametersapp.MainActivity;
import stud.pw.enviromentparametersapp.R;
import stud.pw.enviromentparametersapp.databinding.ActivityAddSensorBinding;
import stud.pw.enviromentparametersapp.databinding.ActivityEditProfileBinding;
import stud.pw.enviromentparametersapp.ui.login.LoginActivity;

public class AddSensorActivity extends AppCompatActivity {
    public enum State {
        HOLD_BUTTON, CONNECT_WIFI, ENTER_CREDENTIALS, ENTER_NAME, WAITING, CONFIGURED;
    }

    State state;
    private ActivityAddSensorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        state = State.HOLD_BUTTON;

        super.onCreate(savedInstanceState);
        binding = ActivityAddSensorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setContentView(R.layout.activity_edit_profile);

        TextView instructionText = binding.instructionText;
        TextView instructionTextWifi = binding.instructionTextWifi;
        Button continueBtn = binding.continueBtn;
        EditText wifiName = binding.wifiNameTxt;
        EditText wifiPassword = binding.wifiPasswordTxt;
        EditText sensorName = binding.sensorNameTxt;

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (state) {
                    case HOLD_BUTTON:
                        state = State.CONNECT_WIFI;
                        instructionText.setText(R.string.add_sensor_connect_wifi);
                        break;
                    case CONNECT_WIFI:
                        state = State.ENTER_CREDENTIALS;
                        instructionText.setText(R.string.add_sensor_enter_wifi);

                        /*ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                                ConstraintLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(20, 0, 20, 100);
                        instructionText.setLayoutParams(params);*/
                        instructionTextWifi.setVisibility(View.VISIBLE);
                        instructionText.setVisibility(View.INVISIBLE);
                        wifiName.setVisibility(View.VISIBLE);
                        wifiPassword.setVisibility(View.VISIBLE);
                        break;
                    case ENTER_CREDENTIALS:
                        state = State.ENTER_NAME;

                        instructionTextWifi.setText("Enter sensor name");
                        wifiName.setVisibility(View.INVISIBLE);
                        wifiPassword.setVisibility(View.INVISIBLE);
                        sensorName.setVisibility(View.VISIBLE);

                        break;
                    case ENTER_NAME:
                        state = State.CONFIGURED;

                        instructionTextWifi.setVisibility(View.INVISIBLE);
                        instructionText.setVisibility(View.VISIBLE);
                        sensorName.setVisibility(View.INVISIBLE);
                        instructionText.setText(R.string.add_sensor_configured);

                    case CONFIGURED:
                        Intent myIntent = new Intent(AddSensorActivity.this, MainActivity.class);
                        startActivity(myIntent);
                        finish();
                        break;
                }
            }
        });
    }
}