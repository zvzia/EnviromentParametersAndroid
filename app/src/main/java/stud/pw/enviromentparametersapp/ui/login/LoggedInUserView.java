package stud.pw.enviromentparametersapp.ui.login;

import stud.pw.enviromentparametersapp.data.model.LoggedInUser;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private String displayName;
    private LoggedInUser loggedInUser;

    public LoggedInUserView(String displayName, LoggedInUser loggedInUser) {
        this.displayName = displayName;
        this.loggedInUser = loggedInUser;
    }

    public LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }

    public LoggedInUser getLoggedInUser() {
        return loggedInUser;
    }
}