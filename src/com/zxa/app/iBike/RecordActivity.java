package com.zxa.app.iBike;

import android.app.Activity;
import android.database.sqlite.SQLiteOpenHelper;
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
import android.content.Intent;
import android.provider.Settings;
import android.content.ActivityNotFoundException;
import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;

/**
 * Created by zxa on 16/3/15.
 */
public class RecordActivity extends Activity {
    private float totalDistance;
    private Location lastLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private EditText dialogEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
        this.totalDistance = 0;
        this.lastLocation = null;
        this.locationManager = null;
        this.dialogEditText = new EditText(this);
        Button beginButton = (Button)findViewById(R.id.begin_button);
        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

                // 检查gps有没有打开
                boolean permission = isOpenGPS(locationManager);

                if(!permission) {
                    openGPS(locationManager);
                    return;
                }

                // 检查gps权限
//                PackageManager pm = getPackageManager();
//                boolean permission = (PackageManager.PERMISSION_GRANTED ==
//                        pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", "com.zxa.app.ibike"));
//               // boolean permission = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//                if (permission) {
//                    showToast("有GPS权限");
//                }else {
//                    showToast("木有GPS权限");
//                   // requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 100);
//                    // return;
//                }

//                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                showLocation(location);

                locationListener = new LocationListener(){

                    public void onLocationChanged(Location location) {
                        // TODO Auto-generated method stub
                        if(lastLocation == null) {
                            lastLocation = location;
                            return;
                        }
                        showLocation(location);
                        lastLocation = location;
                    }

                    public void onProviderDisabled(String provider) {
                        // TODO Auto-generated method stub
                        showToast("gps已经关闭");
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

                };

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            }
        });
        Button endButton = (Button)findViewById(R.id.finish_button);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locationManager == null) {
                    return ;
                }
                // 关闭gps检测
                locationManager.removeUpdates(locationListener);
                locationManager = null;
                new AlertDialog.Builder(RecordActivity.this)
                        .setTitle("请输入此次骑行归属标签")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(dialogEditText)
                        .setPositiveButton("登录", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*
                                  这里将来做输入校验
                                */
                                // 不输入不关闭对话框
                                if(dialogEditText.getText() == null) {
                                    return;
                                }

                                // 校验成功,将记录写入sqllite数据库
                                SQLiteOpenHelper helper = new DataBaseHelper(RecordActivity.this);
                                DatabaseOperator databaseOper = new DatabaseOperator(helper.getWritableDatabase());
                                //databaseOper.insert(id, label, distance)
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    public void showToast(String message) {
        Toast toast=Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showLocation(Location location) {
        if(location != null) {
            TextView speedText = (TextView) findViewById(R.id.speed_text);
            // 时速
            speedText.setText(String.valueOf(location.getSpeed()*3.6));
            // 总距离
            float[] result = new float[1];
            Location.distanceBetween(lastLocation.getLatitude(),lastLocation.getLongitude(),
                    location.getLatitude(), location.getLongitude(), result);
            totalDistance += result[0];
            TextView totalDistanceText = (TextView) findViewById(R.id.totalDistance);
            totalDistanceText.setText(String.valueOf(totalDistance/1000.0));
        }
    }

    public boolean isOpenGPS(LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void openGPS(LocationManager locationManager) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try
        {
            startActivity(intent);

        } catch(ActivityNotFoundException ex)
        {
            // The Android SDK doc says that the location settings activity
            // may not be found. In that case show the general settings.

            // General settings activity
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
