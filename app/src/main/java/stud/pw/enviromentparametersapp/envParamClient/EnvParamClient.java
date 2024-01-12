package stud.pw.enviromentparametersapp.envParamClient;

import static android.content.Context.MODE_PRIVATE;
import static stud.pw.enviromentparametersapp.data.common.UserInfo.getLoggedInUser;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stud.pw.enviromentparametersapp.R;
import stud.pw.enviromentparametersapp.data.model.LoggedInUser;
import stud.pw.enviromentparametersapp.models.SensorConfigRequest;
import stud.pw.enviromentparametersapp.models.SensorConfigResponse;
import stud.pw.enviromentparametersapp.models.StringResponse;
import stud.pw.enviromentparametersapp.models.UserResponse;
import stud.pw.enviromentparametersapp.ui.sensor.RecordListAdapter;
import stud.pw.enviromentparametersapp.models.ArrayResponse;
import stud.pw.enviromentparametersapp.models.BatteryLevelResponse;
import stud.pw.enviromentparametersapp.models.DateStringResponse;
import stud.pw.enviromentparametersapp.models.Sensor;
import stud.pw.enviromentparametersapp.models.SensorsIdsRequest;
import stud.pw.enviromentparametersapp.models.SurroundingConditions;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.google.gson.Gson;


public class EnvParamClient {

    Context context;
    //String serverUrl="https://env-parrameters-server.calmstone-fa48359e.westeurope.azurecontainerapps.io";
    String serverUrl = "http://10.0.2.2:8080";
    SharedPreferences sharedPreferences;

    public EnvParamClient(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userInfo", MODE_PRIVATE);
    }

    public void login(String email, String password, final VolleyCallbackUserResponse callback) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/login";
        JSONObject jsonReqObj = new JSONObject();
        jsonReqObj.put("email", email);
        jsonReqObj.put("password", password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonReqObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                UserResponse userResponse = gson.fromJson(response.toString(), UserResponse.class);
                callback.onSuccess(userResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);
            }
        });

        queue.add(jsonObjectRequest);
    }

    public void register(String username, String email, String password, final VolleyCallbackStringResponse callback) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/registration";
        JSONObject jsonReqObj = new JSONObject();
        jsonReqObj.put("username", username);
        jsonReqObj.put("email", email);
        jsonReqObj.put("password", password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonReqObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                StringResponse stringResponse = gson.fromJson(response.toString(), StringResponse.class);
                callback.onSuccess(stringResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);
            }
        });

        queue.add(jsonObjectRequest);
    }

    public void getRecordsForSensorInRange(int id, Date start, Date end, String viewType, final VolleyCallbackResponse callback) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/getRecordsForSensorInRange";

        JSONObject jsonReqObj = new JSONObject();
        jsonReqObj.put("sensorId", id);
        jsonReqObj.put("start", start.getTime());
        jsonReqObj.put("end", end.getTime());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonReqObj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();

                ArrayResponse<SurroundingConditions> surroundingConditionsList = gson.fromJson(response.toString(), Utils.getArrayResponseType(SurroundingConditions.class));
                if (surroundingConditionsList.getElements().size() == 0) {
                    callback.onSuccess("empty");
                } else if (viewType == "list") {
                    ArrayList<SurroundingConditions> recordsList = surroundingConditionsList.getElements();
                    Collections.reverse(recordsList);

                    ListView list = (ListView) ((Activity) context).findViewById(R.id.records_list);
                    RecordListAdapter adapter = new RecordListAdapter(context, recordsList);
                    list.setAdapter(adapter);
                } else if (viewType == "chart") {
                    initializeCharts(surroundingConditionsList.getElements());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                LoggedInUser loggedInUser = getLoggedInUser(sharedPreferences);
                Map<String, String> headers = new HashMap<String, String>();

                String credentials = loggedInUser.getEmail() + ":" + loggedInUser.getPassword();
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void getSensorsList(final VolleyCallbackArrResponse callback) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/getSensorsList";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();

                ArrayResponse<Sensor> sensorsList = gson.fromJson(response.toString(), Utils.getArrayResponseType(Sensor.class));

                callback.onSuccess(sensorsList);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);
                try {
                    getSensorsList(callback);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                LoggedInUser loggedInUser = getLoggedInUser(sharedPreferences);
                Map<String, String> headers = new HashMap<String, String>();

                String credentials = loggedInUser.getEmail() + ":" + loggedInUser.getPassword();
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void getSensorById(int id, final VolleyCallbackSensorResponse callback) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/getSensorById";

        JSONObject jsonReqObj = new JSONObject();
        jsonReqObj.put("sensorId", id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonReqObj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                Sensor sensor = gson.fromJson(response.toString(), Sensor.class);
                callback.onSuccess(sensor);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                LoggedInUser loggedInUser = getLoggedInUser(sharedPreferences);
                Map<String, String> headers = new HashMap<String, String>();

                String credentials = loggedInUser.getEmail() + ":" + loggedInUser.getPassword();
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void getLastRecords(int[] ids, final VolleyCallbackArrResponse callback) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/getLastRecordsForSensors";

        SensorsIdsRequest idsReq = new SensorsIdsRequest();
        idsReq.setSensorIds(ids);

        JSONArray array = new JSONArray();
        for (int id : ids) {
            array.put(id);
        }
        JSONObject jsonReqObj = new JSONObject();
        jsonReqObj.put("sensorIds", array);
        //jsonReqObj.put("sensorIds", ids);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonReqObj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                ArrayResponse<SurroundingConditions> surroundingConditionsList = gson.fromJson(response.toString(), Utils.getArrayResponseType(SurroundingConditions.class));

                callback.onSuccess(surroundingConditionsList);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                LoggedInUser loggedInUser = getLoggedInUser(sharedPreferences);
                Map<String, String> headers = new HashMap<String, String>();

                String credentials = loggedInUser.getEmail() + ":" + loggedInUser.getPassword();
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void getSensorConfig(int sensorId, final VolleyCallbackSensorConfigResponse callback) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/getSensorConfig" + "?sensorId=" + sensorId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                SensorConfigResponse sensorConfigResponse = gson.fromJson(response.toString(), SensorConfigResponse.class);
                callback.onSuccess(sensorConfigResponse);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                LoggedInUser loggedInUser = getLoggedInUser(sharedPreferences);
                Map<String, String> headers = new HashMap<String, String>();

                String credentials = loggedInUser.getEmail() + ":" + loggedInUser.getPassword();
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void getLastUpdateDateString() throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/getLastUpdateDateString";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                String lastUpdateDateStr = gson.fromJson(response.toString(), DateStringResponse.class).getLastUpdateDate();

                if(lastUpdateDateStr.length()>15){
                    String date = lastUpdateDateStr.substring(0, 10);
                    String time = lastUpdateDateStr.substring(11, 16);

                    TextView lastUpdateValue = (TextView) ((Activity) context).findViewById(R.id.lastMeasurementValue);
                    lastUpdateValue.setText(date + "\n" + time);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                LoggedInUser loggedInUser = getLoggedInUser(sharedPreferences);
                Map<String, String> headers = new HashMap<String, String>();

                String credentials = loggedInUser.getEmail() + ":" + loggedInUser.getPassword();
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);

    }

    public void getLastBatteryLevel(int sensorId, final VolleyCallbackBatteryLevelResponse callback) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/getLastBatteryLevel" + "?sensorId=" + sensorId;

        /*JSONObject jsonReqObj = new JSONObject();
        jsonReqObj.put("sensorId", sensorId);*/

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                BatteryLevelResponse batteryLevelResponse = gson.fromJson(response.toString(), BatteryLevelResponse.class);
                callback.onSuccess(batteryLevelResponse);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                LoggedInUser loggedInUser = getLoggedInUser(sharedPreferences);
                Map<String, String> headers = new HashMap<String, String>();

                String credentials = loggedInUser.getEmail() + ":" + loggedInUser.getPassword();
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void setNewSensorName(int sensorId, String newName) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/updateSensorName";

        JSONObject jsonReqObj = new JSONObject();
        jsonReqObj.put("id", sensorId);
        jsonReqObj.put("name", newName);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonReqObj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                LoggedInUser loggedInUser = getLoggedInUser(sharedPreferences);
                Map<String, String> headers = new HashMap<String, String>();

                String credentials = loggedInUser.getEmail() + ":" + loggedInUser.getPassword();
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void setSensorConfig(SensorConfigRequest sensorConfigRequest, final VolleyCallbackStringResponse callback) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/setSensorConfig";

        JSONObject jsonReqObj = new JSONObject();
        jsonReqObj.put("sensorId", sensorConfigRequest.getSensorId());
        jsonReqObj.put("sensorName", sensorConfigRequest.getSensorName());
        jsonReqObj.put("measurementFreq", sensorConfigRequest.getMeasurementFreq());
        jsonReqObj.put("temperatureMax", sensorConfigRequest.getTemperatureMax());
        jsonReqObj.put("temperatureMin", sensorConfigRequest.getTemperatureMin());
        jsonReqObj.put("humidityMax", sensorConfigRequest.getHumidityMax());
        jsonReqObj.put("humidityMin", sensorConfigRequest.getHumidityMin());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, jsonReqObj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                StringResponse stringResponse = gson.fromJson(response.toString(), StringResponse.class);
                callback.onSuccess(stringResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                LoggedInUser loggedInUser = getLoggedInUser(sharedPreferences);
                Map<String, String> headers = new HashMap<String, String>();

                String credentials = loggedInUser.getEmail() + ":" + loggedInUser.getPassword();
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }
    public void setUserInfo(String name, String email, String password, final VolleyCallbackUserResponse callback) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/setUserInfo";
        JSONObject jsonReqObj = new JSONObject();
        jsonReqObj.put("name", name);
        jsonReqObj.put("email", email);
        jsonReqObj.put("password", password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, jsonReqObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                UserResponse userResponse = gson.fromJson(response.toString(), UserResponse.class);
                callback.onSuccess(userResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                LoggedInUser loggedInUser = getLoggedInUser(sharedPreferences);
                Map<String, String> headers = new HashMap<String, String>();

                String credentials = loggedInUser.getEmail() + ":" + loggedInUser.getPassword();
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void setMessageToken(String email, String token, final VolleyCallbackStringResponse callback) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/setUserMsgToken";
        JSONObject jsonReqObj = new JSONObject();
        jsonReqObj.put("messageToken", token);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, jsonReqObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                StringResponse stringResponse = gson.fromJson(response.toString(), StringResponse.class);
                callback.onSuccess(stringResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                LoggedInUser loggedInUser = getLoggedInUser(sharedPreferences);
                Map<String, String> headers = new HashMap<String, String>();

                String credentials = loggedInUser.getEmail() + ":" + loggedInUser.getPassword();
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void addSensor(String name, String token, final VolleyCallbackStringResponse callback) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/mobile/addSensor";
        JSONObject jsonReqObj = new JSONObject();
        jsonReqObj.put("name", name);
        jsonReqObj.put("token", token);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonReqObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                StringResponse stringResponse = gson.fromJson(response.toString(), StringResponse.class);
                callback.onSuccess(stringResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                LoggedInUser loggedInUser = getLoggedInUser(sharedPreferences);
                Map<String, String> headers = new HashMap<String, String>();

                String credentials = loggedInUser.getEmail() + ":" + loggedInUser.getPassword();
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }




    public interface VolleyCallbackArrResponse {
        void onSuccess(ArrayResponse result);
    }

    public interface VolleyCallbackSensorResponse {
        void onSuccess(Sensor result);
    }

    public interface VolleyCallbackBatteryLevelResponse {
        void onSuccess(BatteryLevelResponse result);
    }

    public interface VolleyCallbackUserResponse {
        void onSuccess(UserResponse result);
    }

    public interface VolleyCallbackResponse {
        void onSuccess(String result);
    }

    public interface VolleyCallbackStringResponse {
        void onSuccess(StringResponse result);
    }

    public interface VolleyCallbackSensorConfigResponse{
        void onSuccess(SensorConfigResponse result);
    }


    public void initializeCharts(ArrayList<SurroundingConditions> surroundingConditions) {

        //--- temperature chart ---
        AnyChartView anyChartViewTemp = ((Activity) context).findViewById(R.id.temperature_chart);
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
        AnyChartView anyChartViewHumid = ((Activity) context).findViewById(R.id.humidity_chart);
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
