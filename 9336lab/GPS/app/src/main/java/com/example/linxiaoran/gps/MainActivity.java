package com.example.linxiaoran.gps;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener {

    private Button get_statue;
    private Button show_location;
    private TextView textView;
    private LocationManager locationManager;
    private TextView textView1;

    private TextView textView2;
    private String method;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        find_view();
        set_onclick_listener();
    }

    private void find_view() {
        get_statue = (Button) findViewById(R.id.get_statue);
        textView = (TextView) findViewById(R.id.textView);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        show_location = (Button) findViewById(R.id.show_location);
    }

    private void set_onclick_listener() {
        get_statue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task1();
            }
        });
        show_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task2();
            }
        });
    }

    private void task1() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            textView.setText("GPS is active\nNetwork is active");
        } else if (locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            textView.setText("Network is active");
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            textView.setText("GPS is active");
        } else {
            new AlertDialog.Builder(this).setTitle("Setting the GPS").
                    setMessage("GPS is not enabled.Do you want to go to setting menu?").
                    setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0);
                        }
                    }).setNegativeButton("Later", null).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000000000,1000,this);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = dateFormat.format(new java.util.Date());
        double latitude = location.getLatitude();
        double altitude = location.getAltitude();
        double longitude = location.getLongitude();
        double accuracy = location.getAccuracy();
        double speed = location.getSpeed();
        textView1.setText("Date/Time: " + date + "\n" +
                "Provider: " + method + "\n" +
                "Accuracy: " + accuracy + "\n" +
                "Altitude" + altitude + "\n" +
                "Latitude: " + latitude + "\n" +
                "Longitude: " + longitude + "\n" +
                "Speed: " + speed + "\n");

    }

    private void task2() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                textView.setText("permission not get");
                return;
            }
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&&!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                new AlertDialog.Builder(this).setTitle("Setting the GPS").
                        setMessage("GPS is not enabled.Do you want to go to setting menu?").
                        setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 0);
                            }
                        }).setNegativeButton("Later", null).show();
            }
            else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 0, this);
                if(locationManager != null){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    method = "NET WORK";
                }
            }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    method = "GPS";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = dateFormat.format(new java.util.Date());
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
            double longitude = location.getLongitude();
            double accuracy = location.getAccuracy();
            double speed = location.getSpeed();
            textView2.setText("Date/Time: " + date + "\n" +
                    "Provider: " + method + "\n" +
                    "Accuracy: " + accuracy + "\n" +
                    "Altitude" + altitude + "\n" +
                    "Latitude: " + latitude + "\n" +
                    "Longitude: " + longitude + "\n" +
                    "Speed: " + speed + "\n");
        }else{
            textView.setText("Processing......");
        }
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
