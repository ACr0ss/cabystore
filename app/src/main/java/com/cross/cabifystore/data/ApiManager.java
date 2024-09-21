package com.cross.cabifystore.data;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cross.cabifystore.CabifyStoreApplication;
import com.cross.cabifystore.R;
import com.cross.cabifystore.activities.CartActivity;
import com.cross.cabifystore.models.Item;
import com.cross.cabifystore.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiManager {

    public static void getData(Context ctx) {
        String url = "https://gist.githubusercontent.com/palcalde/6c19259bd32dd6aafa327fa557859c2f/raw/ba51779474a150ee4367cda4f4ffacdcca479887/Products.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray products = response.getJSONArray("products");
                            ArrayList<Item> itemList = new ArrayList<>();

                            for (int i = 0; i < products.length(); i++) {
                                JSONObject productObject = products.getJSONObject(i);

                                String code = productObject.getString("code");
                                String name = productObject.getString("name");
                                double price = productObject.getDouble("price");

                                Item product = new Item(code, name, price);
                                itemList.add(product);
                            }

                            CabifyStoreApplication app = (CabifyStoreApplication) ctx.getApplicationContext();
                            SQLiteHandler sqLiteHandler = app.getSQLiteHandler();
                            sqLiteHandler.addItems(itemList);

                            new Handler().postDelayed(() -> {
                                ctx.startActivity(new Intent(ctx, CartActivity.class));
                            }, 500);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, ctx.getString(R.string.error), Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                getData(ctx);
                            }
                        }, 2000);
                    }
                });

        VolleySingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

}
