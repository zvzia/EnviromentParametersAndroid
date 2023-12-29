package stud.pw.enviromentparametersapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import stud.pw.enviromentparametersapp.data.model.LoggedInUser;
import stud.pw.enviromentparametersapp.envParamClient.EnvParamClient;
import stud.pw.enviromentparametersapp.models.UserResponse;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public void login(SharedPreferences sharedPreferences, String username, String password, Context context, LoginCallback callback){
        EnvParamClient envParamClient = new EnvParamClient(context);
        final LoggedInUser loggedUser = new LoggedInUser();

        try {
            envParamClient.login(username, password, new EnvParamClient.VolleyCallbackUserResponse() {
                @Override
                public void onSuccess(UserResponse result) {
                    loggedUser.setUserId(result.getId());
                    loggedUser.setEmail(result.getEmail());
                    loggedUser.setUsername(result.getUsername());
                    loggedUser.setPassword(result.getPassword());

                    if (sharedPreferences != null) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("user-id", loggedUser.getUserId());
                        editor.putString("username", loggedUser.getUsername());
                        editor.putString("email", loggedUser.getEmail());
                        editor.putString("password", loggedUser.getPassword());
                        editor.apply();
                    }

                    callback.onLoginSuccess(loggedUser);
                }

            });

        } catch (Exception e) {
            callback.onLoginError(new IOException("Error logging in", e));
        }
    }

    public interface LoginCallback {
        void onLoginSuccess(LoggedInUser loggedInUser);
        void onLoginError(Exception e);
    }

    public void logout(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user-id");
        editor.remove("username");
        editor.remove("email");
        editor.remove("password");
        editor.commit();
    }
}