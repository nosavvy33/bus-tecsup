package com.tecsup.nosavvy.busestecsup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final String LOG = this.getClass().getSimpleName();

    private final int PERMISSION = 100;
    String [] permission  = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET};


    private LocationManager locationManager;
    private LocationListener locationListener;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String ENDPOINT = "http://10.200.175.100/bus_tecsup/list_paradero.php";
    private final String ENDPOINT2 = "http://10.200.175.100/bus_tecsup/list_pasantia.php?idalumno=";

    RequestQueue rqservs;


    private TableLayout tabla;
    private TableLayout pasantias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabla = (TableLayout) findViewById(R.id.tabla);
        pasantias = (TableLayout) findViewById(R.id.pasantias);
        rqservs = Volley.newRequestQueue(this);
        getParaderos();
        getPasantia();
        obtainGeo();
}

    public void getPasantia(){
        final JSONObject[] result = new JSONObject[1];

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,ENDPOINT2+"1",null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                result[0] = response;
                try {
                    if(response.length() > 0){
                       // Log.e("GET PASANTIA \t",response.getString("fecha_hora"));
                        TableRow tableRow = new TableRow(getApplicationContext());
                        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0));
                        tableRow.setGravity(Gravity.CENTER);
                        tableRow.setBackgroundResource(R.drawable.row_element);

                        TextView empresa=  new TextView(getApplicationContext());
                        empresa.setText(String.valueOf(response.getString("nombre_empresa")));
                        empresa.setGravity(Gravity.CENTER);
                        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                        params.setMargins(18, 18, 18, 18);
                        empresa.setLayoutParams(params);

                        TextView fecha=  new TextView(getApplicationContext());
                        String [] fecha_comienzo = response.getString("fecha_hora").split(" ");
                        fecha.setText(fecha_comienzo[0]);
                        fecha.setGravity(Gravity.CENTER);
                        fecha.setLayoutParams(params);

                        tableRow.addView(empresa);
                        tableRow.addView(fecha);
                        pasantias.addView(tableRow);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                    TableRow tableRow = new TableRow(getApplicationContext());
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0));
                    tableRow.setGravity(Gravity.CENTER);
                    tableRow.setBackgroundResource(R.drawable.row_element);

                    TextView noPasantia =  new TextView(getApplicationContext());
                    noPasantia.setText("Usted no tiene pasantías asignadas");
                    noPasantia.setGravity(Gravity.CENTER);
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    params.setMargins(18, 18, 18, 18);
                    noPasantia.setLayoutParams(params);

                    tableRow.addView(noPasantia);
                    pasantias.addView(tableRow);

                }
        });
        rqservs.add(jsonObjectRequest);

    }

    public void getParaderos() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, ENDPOINT, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d(LOG_TAG, response.get(i).toString());
                        JSONObject x = (JSONObject) response.get(i);

                        TableRow tableRow = new TableRow(getApplicationContext());
                        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0));
                        tableRow.setGravity(Gravity.CENTER);
                        tableRow.setBackgroundResource(R.drawable.row_element);

                        TextView paraderoItem = new TextView(getApplicationContext());
                        paraderoItem.setText(x.getString("nombre"));
                        paraderoItem.setGravity(Gravity.CENTER);
                        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                        params.setMargins(18, 18, 18, 18);
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
                                i.putExtra("ubicacion", coordenadas);
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
    public void obtainGeo(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String coordinates = String.valueOf(location.getLatitude())+" "+String.valueOf(location.getLongitude());
                Log.d(LOG, "COORDENADAS \t" +coordinates);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        boolean isPermissionRequired = false;
       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            isPermissionRequired = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(isPermissionRequired){
                    ActivityCompat.requestPermissions(this, permission,PERMISSION);
                }
            }
        } else {
            configureButton();
        }
    }


    private void configureButton() {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    boolean isPermissionRequired = true;
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsRe
                    // sult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                   // return;

                }else{
                    //     locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);
                    locationManager.requestLocationUpdates("gps",2000,0,locationListener);

                    // locationManager.requestSingleUpdate("gps",locationListener,null);
                    //doesnt work the one below
                    //  locationManager.getLastKnownLocation("gps");

                }

            }




    //public void ubicacionPermission() {

       /* while (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){*/

        /*if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION);
            if (PackageManager.PERMISSION_GRANTED != PERMISSION) {
                finish();
            } else {
                getUbicacionn();
            }
        }*/
       /* boolean permissionRequired = false;
        if(ContextCompat.checkSelfPermission(this, permission[0]) != PackageManager.PERMISSION_GRANTED)
            permissionRequired = true;
        if(permissionRequired){
            ActivityCompat.requestPermissions(this, permission, PERMISSION);
        }else{
            getUbicacionn();
        }

        // }


    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case PERMISSION: {
                for (int i=0; i<grantResults.length; i++){
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, permission[i] + " permissions declined!", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                finishAffinity();
                            }
                        }, Toast.LENGTH_LONG);
                    }else{
                        configureButton();
                        obtainGeo();
                    }
                }
            }
        }
    }




    /*public void getUbicacionn() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            finish();
            }else{
            locationManager.requestLocationUpdates("gps", 2000, 0, locationListener);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String coordinates = String.valueOf(location.getLatitude())+" "+String.valueOf(location.getAltitude());
                    Log.d(LOG, "COORDENADAS \t" +coordinates);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            };
        }
    }*/



}
