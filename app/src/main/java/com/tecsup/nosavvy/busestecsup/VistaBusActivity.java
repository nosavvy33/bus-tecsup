package com.tecsup.nosavvy.busestecsup;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VistaBusActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_bus);

        ubicacion = getIntent().getStringExtra("ubicacion");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String [] coordenadas = ubicacion.split(" ");
        double x = Double.parseDouble(coordenadas[0]);
        double y = Double.parseDouble(coordenadas[1]);
        double lat = Double.valueOf(x).doubleValue();
        double lng = Double.valueOf(y).doubleValue();

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng punto = new LatLng(lat,lng);

        mMap.addMarker(new MarkerOptions().position(punto).title("BUS TECSUP"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(punto));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(punto,17));
    }
}
