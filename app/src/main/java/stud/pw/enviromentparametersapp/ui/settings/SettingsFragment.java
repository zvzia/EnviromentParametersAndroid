package stud.pw.enviromentparametersapp.ui.settings;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import stud.pw.enviromentparametersapp.R;
import stud.pw.enviromentparametersapp.data.LoginDataSource;
import stud.pw.enviromentparametersapp.data.LoginRepository;
import stud.pw.enviromentparametersapp.ui.login.LoginActivity;
import stud.pw.enviromentparametersapp.ui.login.LoginViewModel;
import stud.pw.enviromentparametersapp.ui.login.LoginViewModelFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button editProfileBtn = getView().findViewById(R.id.editProfileBtn);
        Button addSensorBtn = getView().findViewById(R.id.addSensorBtn);
        Button configureSensorBtn = getView().findViewById(R.id.configureSensorsBtn);
        Button logOutBtn = getView().findViewById(R.id.logOutBtn);
        editProfileBtn.setOnClickListener(editProfile);
        addSensorBtn.setOnClickListener(addSensor);
        configureSensorBtn.setOnClickListener(configureSensors);
        logOutBtn.setOnClickListener(logOutAction);

        sharedPreferences = getContext().getSharedPreferences("userInfo", MODE_PRIVATE);

        super.onViewCreated(view, savedInstanceState);
    }

    private View.OnClickListener editProfile = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            if (v.getId() == R.id.editProfileBtn) {
                Intent myIntent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(myIntent);
            }
        }
    };

    private View.OnClickListener addSensor = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            if (v.getId() == R.id.addSensorBtn) {
                Intent myIntent = new Intent(getActivity(), AddSensorActivity.class);
                startActivity(myIntent);
            }
        }
    };

    private View.OnClickListener configureSensors = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            if (v.getId() == R.id.configureSensorsBtn) {
                Intent myIntent = new Intent(getActivity(), EditSensorNamesActivity.class);
                startActivity(myIntent);
            }
        }
    };

    private View.OnClickListener logOutAction = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            if (v.getId() == R.id.logOutBtn) {
                LoginRepository.getInstance(new LoginDataSource()).logout(sharedPreferences);

                Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(myIntent);

                getActivity().finish();
            }
        }
    };


}