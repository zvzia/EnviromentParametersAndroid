package stud.pw.enviromentparametersapp.ui.register;

import androidx.annotation.Nullable;

public class RegisterFormState {

    @Nullable
    private Integer nameError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer passwordRetypedError;
    private boolean isDataValid;

    public RegisterFormState(@Nullable Integer nameError, @Nullable Integer emailError, @Nullable Integer passwordError, @Nullable Integer passwordRetypedError) {
        this.nameError = nameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.passwordRetypedError = passwordRetypedError;
        this.isDataValid = false;
    }

    public RegisterFormState(boolean isDataValid) {
        this.nameError = null;
        this.emailError = null;
        this.passwordError = null;
        this.passwordRetypedError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getNameError() {
        return nameError;
    }

    @Nullable
    public Integer getEmailError() {
        return emailError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getPasswordRetypedError() {
        return passwordRetypedError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
