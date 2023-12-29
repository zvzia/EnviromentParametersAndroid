package stud.pw.enviromentparametersapp.ui.sensor;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.Date;

import stud.pw.enviromentparametersapp.R;
import stud.pw.enviromentparametersapp.envParamClient.EnvParamClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordListFragment extends Fragment {

    private static final String SENSOR_ID = "sensorId";
    private static final String START = "start";
    private static final String END = "end";

    private int sensorId;
    private Date start;
    private Date end;

    private SharedPreferences sharedPreferences;

    TextView noContentInfo;


    public RecordListFragment() {
        // Required empty public constructor
    }

    public static RecordListFragment newInstance(int sensorId, Date start, Date end) {
        RecordListFragment fragment = new RecordListFragment();
        Bundle args = new Bundle();
        args.putInt(SENSOR_ID, sensorId);
        args.putLong(START, start.getTime());
        args.putLong(END, end.getTime());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences("userInfo", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sensorId = getArguments().getInt(SENSOR_ID);
            start = new Date(getArguments().getLong(START));
            end = new Date(getArguments().getLong(END));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EnvParamClient envParamClient = new EnvParamClient(getContext());
        try {
            envParamClient.getRecordsForSensorInRange(sensorId, start, end, "list", new EnvParamClient.VolleyCallbackResponse() {
                @Override
                public void onSuccess(String result) {
                    if (result == "empty"){
                        noContentInfo = (TextView) getActivity().findViewById(R.id.noContentInfo);
                        noContentInfo.setVisibility(View.VISIBLE);
                    }
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        /*DbMock dbMock = new DbMock();
        ArrayList<SurroundingConditions> parametersRecords = dbMock.getRecordsForSensorInRange(sensorId, start, end);

        ListView list = (ListView) view.findViewById(R.id.records_list);
        RecordListAdapter adapter = new RecordListAdapter(getActivity(), parametersRecords);
        list.setAdapter(adapter);*/
    }
}