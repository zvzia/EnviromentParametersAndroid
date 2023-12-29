package stud.pw.enviromentparametersapp.ui.register;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import stud.pw.enviromentparametersapp.databinding.ActivityRegisterBinding;
import stud.pw.enviromentparametersapp.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);

        EditText nameEditText = binding.nameEditText;
        EditText emailEditText = binding.emailEditText;
        EditText passwordEditText = binding.passwordEditText;
        EditText passwordRetypedEditText = binding.passwordRetypedEditText;
        Button registerBtn = binding.registerBtn;
        ProgressBar loadingProgressBar = binding.loading;


        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerBtn.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getNameError() != null) {
                    nameEditText.setError(getString(registerFormState.getNameError()));
                }
                if (registerFormState.getEmailError() != null) {
                    emailEditText.setError(getString(registerFormState.getEmailError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
                if (registerFormState.getPasswordRetypedError() != null) {
                    passwordRetypedEditText.setError(getString(registerFormState.getPasswordRetypedError()));
                }
            }
        });

        registerViewModel.getRegisterResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult registerResult) {
                if (registerResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (!registerResult.getResult()) {
                    showRegisterResult("Registration failed");
                }
                if (registerResult.getResult()) {
                    showRegisterResult("Registration success");
                    Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(myIntent);
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        binding.loginBtnInRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                registerViewModel.register(nameEditText.getText().toString(), emailEditText.getText().toString(),
                        passwordEditText.getText().toString(), view.getContext());
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.registerDataChanged(nameEditText.getText().toString(), emailEditText.getText().toString(),
                        passwordEditText.getText().toString(), passwordRetypedEditText.getText().toString());
            }
        };

        nameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordRetypedEditText.addTextChangedListener(afterTextChangedListener);

        /*setContentView(R.layout.activity_register);
        Button loginButton = findViewById(R.id.loginBtnInRegister);
        loginButton.setOnClickListener(loginPage);*/
    }

    private void showRegisterResult(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}