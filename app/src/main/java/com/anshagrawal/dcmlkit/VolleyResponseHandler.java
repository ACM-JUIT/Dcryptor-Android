package com.anshagrawal.dcmlkit;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VolleyResponseHandler {
    Context mContext;
    String[] strArray1;

    public String[] decodeCipher(String text,boolean toStore, boolean base64, Context context) throws JSONException {
        ArrayList<String> decodes = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("data", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BuildConfig.LINK, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("decoded_data");
                    Log.d("bljj", jsonArray.getString(1));
//                    StringBuilder finalDecodedText = new StringBuilder();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String s = jsonArray.getString(i);
                        Log.d("bljj", s);
                        decodes.add(s);
//                        if (i < (jsonArray.length() - 1)) {
//                            finalDecodedText.append(jsonArray.getString(i)).append("\n");
//                        } else {
//                            finalDecodedText.append(jsonArray.getString(i));
//                        }

                    }

                    String[] strArray = new String[decodes.size()];
                    strArray1 = new String[decodes.size()];
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
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        return strArray1;
    }
}

