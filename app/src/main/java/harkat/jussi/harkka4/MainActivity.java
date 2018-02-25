package harkat.jussi.harkka4;

//refs : youtube : north border tech training

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    TextView timeText;
    TextView latitudeText;
    TextView longitudeText;
    TextView locationText;
    Button updateButton;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeText = (TextView) findViewById(R.id.latText);
        timeText = (TextView) findViewById(R.id.timeText);
        longitudeText = (TextView) findViewById(R.id.lonText);
        locationText = (TextView) findViewById(R.id.locationText);
        updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(CheckListener);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Log.d("onCreate", "" + mFusedLocationClient.toString() + "");


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    latitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
                    longitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
                    timeText.setText(String.valueOf(mLastLocation.getTime()));
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        getLastLocation();
    }

    private View.OnClickListener CheckListener = new View.OnClickListener() {
        public void onClick(View view) {
            getLastLocation();
            latitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            longitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            timeText.setText(String.valueOf(mLastLocation.getTime()));
            if (mLastLocation.getLatitude() == 65.0029763 && mLastLocation.getLongitude() == 25.4690442) {
                callGarageDoor(view);
            }
        }
    };

    private void getLastLocation() {

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
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(Task<Location> task) {
                        Log.d("fusedlocationClient", "runs");
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            Log.d("location lat: ", String.valueOf(mLastLocation.getLatitude()));
                            Log.d("location lon: ", String.valueOf(mLastLocation.getLongitude()));
                            Log.d("time: ", String.valueOf(mLastLocation.getTime()));
                        } else {
                            Log.d("location", "getLastLocation failed");
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void callGarageDoor(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:0447294321"));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }



}
