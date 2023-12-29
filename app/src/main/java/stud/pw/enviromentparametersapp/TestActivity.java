package stud.pw.enviromentparametersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import stud.pw.enviromentparametersapp.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {

    private ActivityTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        EditText editText3 = binding.editText3;

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("gowno");
            }
        };

        editText3.addTextChangedListener(textWatcher);
    }
}