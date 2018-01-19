package nyc.c4q.foodsearch;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import nyc.c4q.foodsearch.fragments.FirstFragment;
import nyc.c4q.foodsearch.fragments.SecondFragment;
import nyc.c4q.foodsearch.fragments.ThirdFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ERROR_DIALOG_REQEUST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private FirstFragment favFrag = new FirstFragment();
    private SecondFragment listFrag = new SecondFragment();
    private ThirdFragment mapFrag = new ThirdFragment();

    private ArrayList <AHBottomNavigationItem> items = new ArrayList <>();
    private AHBottomNavigation bottom;
    private static double currentLongitude;
    private static double currentLatitude;

    private int hostTab;

    public static double getCurrentLongitude() {
        return currentLongitude;
    }

    public static double getCurrentLatitude() {
        return currentLatitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBottomNav();
        isServicesOK();
        getLocationPermission();
        getDeviceLocation();
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google service version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK: an error occur, but we can fix it.");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQEUST);
            dialog.show();
        }
        return false;
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                Log.d(TAG, "getLocationPermission: PERMISSION_GRANTED");
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {

                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            currentLatitude = currentLocation.getLatitude();
                            currentLongitude = currentLocation.getLongitude();
                            Log.d(TAG, "onComplete: " + new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to 1st: " + latLng.latitude + ", lng " + latLng.longitude);

    }

    public void setBottomNav() {
        bottom = findViewById(R.id.bottom_navigation);
        bottom.setCurrentItem(2);
//        bottom.setTranslucentNavigationEnabled(false);
        bottom.setBehaviorTranslationEnabled(false);
//        bottom.setColored(true);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Map", R.drawable.ic_location_searching_black_24dp);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("List", R.drawable.headline);
        final AHBottomNavigationItem item3 = new AHBottomNavigationItem("Favorites", R.drawable.blank_heart);

        items.add(item1);
        items.add(item2);
        items.add(item3);

        bottom.addItems(items);

        bottom.setDefaultBackgroundColor(Color.LTGRAY);
        bottom.setAccentColor(Color.parseColor("#52c7b8"));
//        bottom.setInactiveColor(Color.LTGRAY);

// Colors for selected (active) and non-selected items (in color reveal mode).
//        bottom.setColoredModeColors(Color.WHITE, Color.LTGRAY);

        bottom.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        FragmentManager manager2 = getSupportFragmentManager();
                        FragmentTransaction transaction2 = manager2.beginTransaction();
                        transaction2.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                        transaction2.replace(R.id.container, mapFrag);
                        transaction2.commit();
                        hostTab = 0;
                        break;
                    case 1:
                        FragmentManager manager1 = getSupportFragmentManager();
                        FragmentTransaction transaction1 = manager1.beginTransaction();
                        if (hostTab == 0) {
                            transaction1.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        } else if (hostTab == 2) {
                            transaction1.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                        }
                        transaction1.replace(R.id.container, listFrag);
                        transaction1.commit();
                        break;
                    case 2:
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction.replace(R.id.container, favFrag);
                        transaction.commit();
                        hostTab = 2;
                        break;
                }
                return true;
            }
        });
    }


}
