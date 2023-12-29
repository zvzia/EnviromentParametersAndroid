package stud.pw.enviromentparametersapp.data.common;

import android.content.SharedPreferences;

import stud.pw.enviromentparametersapp.data.model.LoggedInUser;

public class UserInfo {

    public static LoggedInUser getLoggedInUser(SharedPreferences sharedPreferences){

        int userId = sharedPreferences.getInt("user-id", 0);
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        return new LoggedInUser(userId, username, email, password);
    }
}
