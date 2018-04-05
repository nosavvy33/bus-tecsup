package com.tecsup.nosavvy.busestecsup;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String ENDPOINT = "http://10.200.173.173/bus_tecsup/list_paradero.php";
    RequestQueue rqservs;


    private TableLayout tabla;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabla = (TableLayout)findViewById(R.id.tabla);
        rqservs = Volley.newRequestQueue(this);
        getParaderos();
    }

    public void getParaderos(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, ENDPOINT, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0 ; i < response.length(); i++){
                    try {
                        Log.d(LOG_TAG,response.get(i).toString());
                        JSONObject x = (JSONObject) response.get(i);

                        TableRow tableRow = new TableRow(getApplicationContext());
                        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,0));
                        tableRow.setGravity(Gravity.CENTER);
                        tableRow.setBackgroundResource(R.drawable.row_element);

                        TextView paraderoItem = new TextView(getApplicationContext());
                        paraderoItem.setText(x.getString("nombre"));
                        paraderoItem.setGravity(Gravity.CENTER);
                        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
                        params.setMargins(18,18,18,18);
                        paraderoItem.setLayoutParams(params);

                        TextView paraderoItem1 = new TextView(getApplicationContext());
                        paraderoItem1.setText(x.getString("hora_partida"));
                        paraderoItem1.setGravity(Gravity.CENTER);
                        paraderoItem1.setLayoutParams(params);

                        tableRow.addView(paraderoItem);
                        tableRow.addView(paraderoItem1);

                        final String coordenadas = x.getString("ubicacion");

                        tableRow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            Intent i = new Intent(getApplicationContext(), VistaBusActivity.class);
                            i.putExtra("ubicacion",coordenadas);
                            startActivity(i);
                            }
                        });

                        tabla.addView(tableRow);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        rqservs.add(jsonArrayRequest);


    }


}
