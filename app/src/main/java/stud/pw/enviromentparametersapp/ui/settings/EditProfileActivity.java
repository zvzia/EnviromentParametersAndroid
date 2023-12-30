package stud.pw.enviromentparametersapp.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import stud.pw.enviromentparametersapp.MainActivity;
import stud.pw.enviromentparametersapp.databinding.ActivityEditProfileBinding;
import stud.pw.enviromentparametersapp.envParamClient.EnvParamClient;
import stud.pw.enviromentparametersapp.models.SensorConfigResponse;
import stud.pw.enviromentparametersapp.models.UserResponse;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private EnvParamClient envParamClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        envParamClient = new EnvParamClient(this);

        EditText name = binding.name;
        EditText email = binding.username;
        EditText password = binding.password;
        EditText passwordRetyped = binding.retypedPassword;
        Button save = binding.save;
        Button cancel = binding.cancel;

        name.setText(sharedPreferences.getString("username", ""));
        email.setText(sharedPreferences.getString("email", ""));
        password.setText(sharedPreferences.getString("password", ""));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    envParamClient.setUserInfo(name.getText().toString(), email.getText().toString(), password.getText().toString(), new EnvParamClient.VolleyCallbackUserResponse() {
                        @Override
                        public void onSuccess(UserResponse result) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", result.getUsername());
                            editor.putString("email", result.getEmail());
                            editor.putString("password", result.getPassword());
                            editor.apply();

                            name.setText(sharedPreferences.getString("username", ""));
                            email.setText(sharedPreferences.getString("email", ""));
                            password.setText(sharedPreferences.getString("password", ""));

                            Intent myIntent = new Intent(EditProfileActivity.this, MainActivity.class);
                            startActivity(myIntent);
                            finish();
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(myIntent);

                finish();
            }
        });

    }
}