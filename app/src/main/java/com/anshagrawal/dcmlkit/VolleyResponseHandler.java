package com.anshagrawal.dcmlkit;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
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
//    ProgressDialog dialog;
    Context mContext;
    String[] strArray1;
    public String[] decodeCipher(String text, Context context) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
//            dialog.show();
            jsonObject.put("data", text);
        } catch (JSONException e) {
//            dialog.dismiss();
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BuildConfig.LINK, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
//                    dialog.show();
                    JSONArray jsonArray = response.getJSONArray("decoded_data");
                    Log.d("bljj", jsonArray.getString(1));
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
                    strArray1=new String[decodes.size()];
                    for (int i = 0; i < decodes.size(); i++) {
                        strArray[i] = decodes.get(i);
                        strArray1[i] = decodes.get(i);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
//        requestQueue.add(jsonObjectRequest);
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        return strArray1;
    }

}
