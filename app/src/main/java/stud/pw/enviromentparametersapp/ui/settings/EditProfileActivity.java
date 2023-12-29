package stud.pw.enviromentparametersapp.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import stud.pw.enviromentparametersapp.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        EditText name = binding.name;
        EditText email = binding.username;
        EditText password = binding.password;
        EditText passwordRetyped = binding.retypedPassword;
        Button nameBtn = binding.nameBtn;
        Button emailBtn = binding.emailBtn;
        Button passwordBtn = binding.passwordBtn;

        //TODO get user info
        name.setText("imie");
        email.setText("email");
        password.setText("haslo");

        nameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameBtn.getText().toString().equals("Edit")){
                    name.setEnabled(true);
                    nameBtn.setText("Save");
                }else{
                    name.setEnabled(false);
                    nameBtn.setText("Edit");
                    //TODO send request
                }
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailBtn.getText().toString().equals("Edit")){
                    email.setEnabled(true);
                    emailBtn.setText("Save");
                }else{
                    email.setEnabled(false);
                    emailBtn.setText("Edit");
                    //TODO send request
                }
            }
        });

        passwordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordBtn.getText().toString().equals("Edit")){
                    password.setEnabled(true);
                    passwordRetyped.setEnabled(true);
                    passwordRetyped.setVisibility(View.VISIBLE);
                    passwordBtn.setText("Save");
                }else{
                    password.setEnabled(false);
                    passwordRetyped.setEnabled(true);
                    passwordRetyped.setVisibility(View.INVISIBLE);
                    passwordBtn.setText("Edit");
                    //TODO send request
                }
            }
        });
    }

}