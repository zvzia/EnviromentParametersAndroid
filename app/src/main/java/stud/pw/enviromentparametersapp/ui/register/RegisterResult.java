package stud.pw.enviromentparametersapp.ui.register;

import androidx.annotation.Nullable;

public class RegisterResult {

    @Nullable
    private boolean result;

    public RegisterResult(boolean result) {
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }
}
