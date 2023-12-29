package stud.pw.enviromentparametersapp.ui.sensor;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stud.pw.enviromentparametersapp.R;
import stud.pw.enviromentparametersapp.envParamClient.EnvParamClient;
import stud.pw.enviromentparametersapp.models.SurroundingConditions;

public class ChartsFragment extends Fragment {

    private static final String SENSOR_ID = "sensorId";
    private static final String START = "start";
    private static final String END = "end";


    private int sensorId;
    private Date start;
    private Date end;
    private SharedPreferences sharedPreferences;

    ConstraintLayout noContentInfo;


    public ChartsFragment() {
        // Required empty public constructor
    }

    public static ChartsFragment newInstance(int sensorId, Date start, Date end) {
        ChartsFragment fragment = new ChartsFragment();
        Bundle args = new Bundle();
        args.putInt(SENSOR_ID, sensorId);
        args.putLong(START, start.getTime());
        args.putLong(END, end.getTime());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        return inflater.inflate(R.layout.fragment_charts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EnvParamClient envParamClient = new EnvParamClient(getContext());
        try {
            envParamClient.getRecordsForSensorInRange(sensorId, start, end, "chart", new EnvParamClient.VolleyCallbackResponse() {
                @Override
                public void onSuccess(String result) {
                    if (result == "empty"){
                        noContentInfo = (ConstraintLayout) getView().findViewById(R.id.noContentInfoCharts);
                        noContentInfo.setVisibility(View.VISIBLE);
                    }
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void initializeCharts(ArrayList<SurroundingConditions> surroundingConditions) {

        //DbMock dbMock = new DbMock();
        //ArrayList<SurroundingConditions> surroundingConditions = dbMock.getRecordsForSensorInRange(sensorId, start, end);

        //--- temperature chart ---
        AnyChartView anyChartViewTemp = getView().findViewById(R.id.temperature_chart);
        APIlib.getInstance().setActiveAnyChartView(anyChartViewTemp);

        Cartesian cartesianTemp = AnyChart.line();
        cartesianTemp.animation(true);
        cartesianTemp.title("Temperatures");

        List<DataEntry> seriesDataTemp = new ArrayList<>();
        for (SurroundingConditions record : surroundingConditions) {
            seriesDataTemp.add(new CustomDataEntryTemp(record));
        }

        Line seriesTemp = cartesianTemp.line(seriesDataTemp);
        seriesTemp.name("Temperature");
        seriesTemp.color("#4F5783");

        anyChartViewTemp.setChart(cartesianTemp);


        //--- humidity chart ---
        AnyChartView anyChartViewHumid = getView().findViewById(R.id.humidity_chart);
        APIlib.getInstance().setActiveAnyChartView(anyChartViewHumid);

        Cartesian cartesianHumid = AnyChart.line();
        cartesianHumid.animation(true);
        cartesianHumid.title("Humidity");

        List<DataEntry> seriesDataHumid = new ArrayList<>();
        for (SurroundingConditions record : surroundingConditions) {
            seriesDataHumid.add(new CustomDataEntryHumid(record));
        }

        Line seriesHumid = cartesianHumid.line(seriesDataHumid);
        seriesHumid.name("Humidity");
        seriesHumid.color("#4F5783");

        anyChartViewHumid.setChart(cartesianHumid);

    }

    private static class CustomDataEntryTemp extends ValueDataEntry {
        CustomDataEntryTemp(SurroundingConditions record) {
            super(record.getDateString(true), record.getTemperature());
        }

    }

    private static class CustomDataEntryHumid extends ValueDataEntry {
        CustomDataEntryHumid(SurroundingConditions record) {
            super(record.getDateString(true), record.getHumidity());
        }

    }
}