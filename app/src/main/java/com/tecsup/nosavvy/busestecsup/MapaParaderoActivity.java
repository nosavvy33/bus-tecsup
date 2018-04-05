package com.tecsup.nosavvy.busestecsup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MapaParaderoActivity extends AppCompatActivity {
//AIzaSyArhNx-_P5oVSTouIvEeEvzrAgewg8Khgk
    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_paradero);
        Log.d(LOG_TAG,getIntent().getStringExtra("ubicacion"));
    }
}
