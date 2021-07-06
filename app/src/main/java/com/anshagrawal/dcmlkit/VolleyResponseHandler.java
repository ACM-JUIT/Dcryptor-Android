package com.anshagrawal.dcmlkit;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anshagrawal.dcmlkit.Activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VolleyResponseHandler {
    ArrayList<String> decodes;
    ProgressDialog dialog;

    Context context;
    public String[] decodeCipher(String text) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String[] strArray1 = null;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Processing your result!");
        dialog.setCancelable(false);
        JSONObject jsonObject = new JSONObject();
        try {
            dialog.show();
            jsonObject.put("data", text);
        } catch (JSONException e) {
            dialog.dismiss();
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BuildConfig.LINK, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    dialog.show();
                    JSONArray jsonArray = response.getJSONArray("decoded_data");
                    StringBuilder finalDecodedText = new StringBuilder();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String s = jsonArray.getString(i);
                        decodes.add(s);
                        if (i < (jsonArray.length() - 1)) {
                            finalDecodedText.append(jsonArray.getString(i)).append("\n");
                        } else {
                            finalDecodedText.append(jsonArray.getString(i));
                        }

                    }

                    String[] strArray = new String[decodes.size()];
                    for (int i = 0; i < decodes.size(); i++) {
                        strArray[i] = decodes.get(i);
                        strArray1[i] = decodes.get(i);
                    }



//                    arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_listview, strArray);
//                    activityMainBinding.myListView.setAdapter(arrayAdapter);
                    dialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
        return strArray1;
    }

}
