package com.zxa.app.iBike;

import android.app.Activity;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.content.Context;
import android.widget.Toast;
import android.widget.Button;


/**
 * Created by zxa on 16/3/15.
 */
public class RecordActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        Button beginButton = (Button)findViewById(R.id.begin_button);
        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

                // 检查gps权限
                PackageManager pm = getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", "packageName"));
                if (permission) {
                    showToast("有这个权限");
                }else {
                    showToast("木有这个权限");
                }

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                showLocation(location);

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener(){

                    public void onLocationChanged(Location location) {
                        // TODO Auto-generated method stub
                        showLocation(location);
                    }

                    public void onProviderDisabled(String provider) {
                        // TODO Auto-generated method stub
                        showLocation(null);
                    }

                    public void onProviderEnabled(String provider) {
                        // TODO Auto-generated method stub
                        try{
                            showLocation(locationManager.getLastKnownLocation(provider));}
                        catch (SecurityException e){
                            showToast("gps权限错误");
                        }
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        // TODO Auto-generated method stub
                    }

                });
            }
        });
    }

    public void showToast(String message) {
        Toast toast=Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showLocation(Location location) {
        TextView speedText = (TextView)findViewById(R.id.speed_text);
        speedText.setText(String.valueOf(location.getSpeed()));
    }
}
