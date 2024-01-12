package stud.pw.enviromentparametersapp.envParamClient;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import stud.pw.enviromentparametersapp.models.StringResponse;
import stud.pw.enviromentparametersapp.models.UserResponse;

public class ArduinoClient {

    Context context;
    String serverUrl = "http://10.0.2.2:8080"; //TODO change
    SharedPreferences sharedPreferences;

    public ArduinoClient(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userInfo", MODE_PRIVATE);
    }

    public void checkStatus(final EnvParamClient.VolleyCallbackResponse callback) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/status";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("nie powiodlo sie" + error);
            }
        });

        queue.add(stringRequest);
    }

    public void saveWifiCredentials(String ssid, String password, String token, final EnvParamClient.VolleyCallbackStringResponse callback) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverUrl + "/save";
        JSONObject jsonReqObj = new JSONObject();
        jsonReqObj.put("ssid", ssid);
        jsonReqObj.put("password", password);
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
        });

        queue.add(jsonObjectRequest);
    }
}
