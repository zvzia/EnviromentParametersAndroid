package stud.pw.enviromentparametersapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import stud.pw.enviromentparametersapp.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public LoggedInUser getLoggedInUser() {
        return user;
    }

    public void logout(SharedPreferences sharedPreferences) {
        user = null;
        dataSource.logout(sharedPreferences);
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public void login(SharedPreferences sharedPreferences, String username, String password, Context context, LoginCallback callback) {
        // handle login
        dataSource.login(sharedPreferences, username, password, context, new LoginDataSource.LoginCallback() {
            @Override
            public void onLoginSuccess(LoggedInUser loggedInUser) {
                setLoggedInUser(loggedInUser);
                callback.onResponse(new Result.Success<>(loggedInUser));
            }

            @Override
            public void onLoginError(Exception e) {
                callback.onResponse(new Result.Error(e));
            }
        });


        //Result<LoggedInUser> result = dataSource.login(sharedPreferences, username, password, context);


        /*if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;*/
    }

    public interface LoginCallback {
        void onResponse(Result<LoggedInUser> loggedInUser);
    }
}