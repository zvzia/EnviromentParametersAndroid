package stud.pw.enviromentparametersapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import stud.pw.enviromentparametersapp.envParamClient.EnvParamClient;
import stud.pw.enviromentparametersapp.models.ArrayResponse;
import stud.pw.enviromentparametersapp.models.Sensor;
import stud.pw.enviromentparametersapp.models.SurroundingConditions;
import stud.pw.enviromentparametersapp.ui.sensor.SensorActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    ArrayList<Sensor> sensorsList;
    ArrayList<SurroundingConditions> lastRecordsList;
    ListView list;
    TextView info;
    TextView temp;
    TextView humid;
    TextView batery;
    TextView lastupdate;
    private static LastUpdateListAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharedPreferences sharedPreferences;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences("userInfo", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SwipeRefreshLayout pullToRefresh = getView().findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                pullToRefresh.setRefreshing(false);
            }
        });

        info = (TextView) getView().findViewById(R.id.noSensorContentInfo);
        temp = (TextView) getView().findViewById(R.id.textViewTemp);
        humid = (TextView) getView().findViewById(R.id.textViewHumid);
        batery = (TextView) getView().findViewById(R.id.textViewBatery);
        lastupdate = (TextView) getView().findViewById(R.id.textViewLastMeasurment);

        loadData();
    }

    private void displayList() {
        if (sensorsList != null && lastRecordsList != null) {
            list = (ListView) getView().findViewById(R.id.lastUpdateList);
            adapter = new LastUpdateListAdapter(getActivity(), sensorsList, lastRecordsList);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Sensor sensor = sensorsList.get(i);

                    Intent mIntent = new Intent(getActivity(), SensorActivity.class);
                    mIntent.putExtra("sensorId", sensor.getId());
                    startActivity(mIntent);
                }
            });
        }
    }

    private void loadData() {
        EnvParamClient envParamClient = new EnvParamClient(getContext());
        try {
            envParamClient.getSensorsList(new EnvParamClient.VolleyCallbackArrResponse() {
                @Override
                public void onSuccess(ArrayResponse result) {
                    sensorsList = result.getElements();

                    int[] sensorsIds = new int[sensorsList.size()];
                    for (int i = 0; i < sensorsList.size(); i++) {
                        sensorsIds[i] = sensorsList.get(i).getId();
                    }

                    try {
                        envParamClient.getLastRecords(sensorsIds, new EnvParamClient.VolleyCallbackArrResponse() {
                            @Override
                            public void onSuccess(ArrayResponse result) {
                                lastRecordsList = result.getElements();
                                displayList();

                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                LocalDateTime now = LocalDateTime.now();
                                TextView dataPullDate = (TextView) getView().findViewById(R.id.lastDataPull);
                                dataPullDate.setText("Data pulled at " + dtf.format(now));
                            }
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    if (sensorsList.isEmpty()) {
                        info.setVisibility(View.VISIBLE);
                        humid.setVisibility(View.INVISIBLE);
                        temp.setVisibility(View.INVISIBLE);
                        batery.setVisibility(View.INVISIBLE);
                        lastupdate.setVisibility(View.INVISIBLE);
                    }

                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            envParamClient.getLastUpdateDateString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}