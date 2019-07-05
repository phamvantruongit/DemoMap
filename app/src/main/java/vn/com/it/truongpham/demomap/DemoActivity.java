package vn.com.it.truongpham.demomap;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;

public class DemoActivity extends AppCompatActivity {
    //https://www.andrious.com/tutorials/android-real-time-location-tutorial/

    private static final int PERMISSIONS_REQUEST = 100;
    IntentFilter intentFilter;
    DemoTest demoTest;

    public static  boolean check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo);




        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (permission == PackageManager.PERMISSION_GRANTED) {
           startTrackerService();
        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }

        intentFilter =new IntentFilter();
        demoTest=new DemoTest();
        intentFilter.addAction("CheckIn");
        LocalBroadcastManager.getInstance(this).registerReceiver(demoTest,intentFilter);

    }

    public void stopService(){
        stopService(new Intent(this, TrackingService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTrackerService();
                }

            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(demoTest);
    }

    private void startTrackerService() {
        Intent intent=new Intent(this, TrackingService.class);
        intent.putExtra("longitude",106.654463);
        intent.putExtra("latitude",10.794884);
        startService(intent);
    }

    class DemoTest extends BroadcastReceiver{



        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("CheckIn")) {
                boolean check_in = intent.getBooleanExtra("check_in", false);
                double latitude=intent.getDoubleExtra("latitude",0);
                double longitude=intent.getDoubleExtra("longitude",0);
                Toast.makeText(DemoActivity.this, "Check In = " + check_in + "" + latitude +"" + longitude +"", Toast.LENGTH_SHORT).show();
                check=true;
                //stopService();
            }
        }


    }
}
