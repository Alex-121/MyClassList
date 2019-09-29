package com.example.mycontactlist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;

public class Exercise extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener gpsListener;
    final int PERMISSION_REQUEST_LOCATION = 101;
    LocationListener networkListener;
    Location currentBestLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        initMapButton();
        initListButton();
        initSettingButton();
        initGetLocationButton();

    }
    private void initListButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Exercise.this, ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initMapButton() {
        ImageButton ibMap = (ImageButton) findViewById(R.id.imageButtonMap);
        ibMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Exercise.this, ContactMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initSettingButton() {
        ImageButton ibSettings = (ImageButton) findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Exercise.this, ContactSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ImageButton editSettings = (ImageButton) findViewById(R.id.imageButtonSettings);
                editSettings.setEnabled(false);
            }
        });
    }

    private void initGetLocationButton() {
        final Button locationButton = (Button) findViewById(R.id.buttonCoods);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(Build.VERSION.SDK_INT >= 23) {
                        if(ContextCompat.checkSelfPermission(Exercise.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED){
                            if(ActivityCompat.shouldShowRequestPermissionRationale(Exercise.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                                Snackbar.make(findViewById(R.id.activity_exercise),"MyContactLisst requires this permission to locate your contacts",
                                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(Exercise.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                                    }
                                }).show();
                            }
                            else {
                                ActivityCompat.requestPermissions(Exercise.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                            }
                        }
                        else {
                            startLocationUpdates();
                        }

                        }
                    else {
                        startLocationUpdates();
                    }

                }
                catch (Exception e) {
                    Toast.makeText(getBaseContext(),"Error requesting permission", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();

        try {
            locationManager.removeUpdates(gpsListener);
            locationManager.removeUpdates(networkListener);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try{
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            gpsListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    TextView txtLatitude = (TextView) findViewById(R.id.gpsLat);
                    TextView txtLongitude = (TextView) findViewById(R.id.gpsLong);
                    TextView txtAccuracy = (TextView) findViewById(R.id.gpsAcc);

                    TextView txtLat = (TextView) findViewById(R.id.bestLat);
                    TextView txtLong = (TextView) findViewById(R.id.bestLong);
                    TextView txtAcc = (TextView) findViewById(R.id.bestAcc);

                    txtLatitude.setText(String.valueOf(location.getLatitude()));
                    txtLongitude.setText(String.valueOf(location.getLongitude()));
                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));

                    if(isBetterLocation(location)) {
                        currentBestLocation =location;

                        txtLat.setText(String.valueOf(location.getLatitude()));
                        txtLong.setText(String.valueOf(location.getLongitude()));
                        txtAcc.setText(String.valueOf(location.getAccuracy()));
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            networkListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    TextView txtLatitude = (TextView) findViewById(R.id.netLat);
                    TextView txtLongitude = (TextView) findViewById(R.id.netLong);
                    TextView txtAccuracy = (TextView) findViewById(R.id.netAcc);
                    TextView txtLat = (TextView) findViewById(R.id.bestLat);
                    TextView txtLong = (TextView) findViewById(R.id.bestLong);
                    TextView txtAcc = (TextView) findViewById(R.id.bestAcc);

                    txtLatitude.setText(String.valueOf(location.getLatitude()));
                    txtLongitude.setText(String.valueOf(location.getLongitude()));
                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));

                    if(isBetterLocation(location)) {
                        currentBestLocation =location;
                        txtLat.setText(String.valueOf(location.getLatitude()));
                        txtLong.setText(String.valueOf(location.getLongitude()));
                        txtAcc.setText(String.valueOf(location.getAccuracy()));
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,gpsListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,networkListener);
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(),"Error, Location not available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                }
                else {
                    Toast.makeText(Exercise.this,"MyContactList will not locate your contacts.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean isBetterLocation(Location location) {
        boolean isBetter = false;
        if(currentBestLocation == null) {
            isBetter=true;
        }
        else if(location.getAccuracy() <= currentBestLocation.getAccuracy()){
            isBetter =true;
        }
        else if(location.getTime() - currentBestLocation.getTime() > 5*60*1000) {
            isBetter = true;
        }
        return isBetter;
    }
}
