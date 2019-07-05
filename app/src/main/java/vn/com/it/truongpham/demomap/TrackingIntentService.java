package vn.com.it.truongpham.demomap;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class TrackingIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TrackingIntentService() {
        super("TrackingIntentService");
    }

    @Override
    protected void onHandleIntent( Intent intent) {
        double longitudeConfig = intent.getDoubleExtra("longitude", 0);
        double latitudeConfig = intent.getDoubleExtra("latitude", 0);
        getLocation(latitudeConfig, longitudeConfig);
    }

    private void getLocation(final double latitudeConfig, final double longitudeConfig) {

        final LocationRequest request = new LocationRequest();
        request.setInterval(1000);

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();


                    Location locationA = new Location("");
                    locationA.setLatitude(latitude);
                    locationA.setLongitude(longitude);
                    locationA.setAltitude(0);

                    Location locationB = new Location("");
                    locationB.setLatitude(latitudeConfig);
                    locationB.setLongitude(longitudeConfig);
                    locationB.setAltitude(0);
                    double km = locationA.distanceTo(locationB) / 1000;

                    Log.d("PPPP", String.format("%.2f", km) + "\n" + longitude + "" + longitude + "");

                    if (km < 5.0) {
                        Intent intent = new Intent();
                        intent.setAction("CheckIn");
                        intent.putExtra("check_in", true);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }
                }
            }, null);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
