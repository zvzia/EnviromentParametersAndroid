package stud.pw.enviromentparametersapp.ui.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;

import java.io.IOException;

import stud.pw.enviromentparametersapp.R;
import stud.pw.enviromentparametersapp.envParamClient.EnvParamClient;
import stud.pw.enviromentparametersapp.models.StringResponse;
import stud.pw.enviromentparametersapp.models.UserResponse;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    //private LoginRepository loginRepository;

    RegisterViewModel() {}

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(String name, String email, String password, Context context) {
        // can be launched in a separate asynchronous job
        EnvParamClient envParamClient = new EnvParamClient(context);

        try {
            envParamClient.register(name,email , password, new EnvParamClient.VolleyCallbackStringResponse() {
                @Override
                public void onSuccess(StringResponse result) {
                    if(result.getResponse().equals("OK")){
                        registerResult.setValue(new RegisterResult(true));
                    }else{
                        registerResult.setValue(new RegisterResult(false));
                    }
                }
            });

        } catch (Exception e) {
            registerResult.setValue(new RegisterResult(false));
        }
    }

    public void registerDataChanged(String name, String email, String password, String passwordRetyped) {
        if (!isNameValid(name)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null, null, null));
        } else if (!isEmailValid(email)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_email, null, null));
        }else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_password, null));
        }else if (!isPasswordRetypedValid(password, passwordRetyped)) {
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_password_retyped));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    private boolean isNameValid(String name) {
        if (name == null) {
            return false;
        }
        return !name.trim().isEmpty();
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return false;
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isPasswordRetypedValid(String password, String passwordRetyped) {
        if(!password.trim().equals(passwordRetyped.trim())){
            return false;
        }
        return true;
    }
}
