package com.stickercamera.app.camera.ui;

/**
 * Created by q on 9/25/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.github.skykai.stickercamera.R;

public class MainActivity extends Activity implements LocationListener {

    LocationManager locationmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationmanager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria cri=new Criteria();
        String provider=locationmanager.getBestProvider(cri,false);

        if(provider!=null & !provider.equals(""))
        {
            Location location=locationmanager.getLastKnownLocation(provider);
            locationmanager.requestLocationUpdates(provider,2000,1,this);
            if(location!=null)
            {
                onLocationChanged(location);
            }
            else{
                Toast.makeText(getApplicationContext(),"location not found",Toast.LENGTH_LONG ).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Provider is null",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public void onLocationChanged(Location location) {
        //TextView textView2=(TextView)findViewById(R.id.textview2);

        //TextView textView3=(TextView)findViewById(R.id.textview3);

        //textView2.setText("Latitude"+location.getLatitude());
        //textView3.setText("Longitude"+ location.getLongitude());
    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }
    @Override
    public void onProviderEnabled(String s) {
    }
    @Override
    public void onProviderDisabled(String s) {
    }
}
