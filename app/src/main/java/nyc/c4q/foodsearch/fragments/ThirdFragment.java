package nyc.c4q.foodsearch.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import nyc.c4q.foodsearch.MainActivity;
import nyc.c4q.foodsearch.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends Fragment implements OnMapReadyCallback{
    private View v;
    private GoogleMap mGoogleMap;
    private MapView mapView;
    private Double bussinessLag;
    private Double bussinessLong;
    private String businessName;
    int PLACE_PICKER_REQUEST = 1;
    private Button placePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_third, container, false);
        getCoordinates();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = v.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (bussinessLong != null && bussinessLag != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(bussinessLag, bussinessLong))
                    .title(businessName)
                    .snippet("restaurant"));

            CameraPosition restaurant = CameraPosition.builder()
                    .target(new LatLng(bussinessLag, bussinessLong))
                    .zoom(16)
                    .bearing(0)
                    .tilt(45)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(restaurant));
        } else {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            CameraPosition myLocation = CameraPosition.builder()
                    .target(new LatLng(MainActivity.getCurrentLatitude(), MainActivity.getCurrentLongitude()))
                    .zoom(16)
                    .bearing(0)
                    .tilt(45)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(myLocation));
        }
    }

    public void getCoordinates() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            bussinessLong = bundle.getDouble("long");
            bussinessLag = bundle.getDouble("lag");
            businessName= bundle.getString("name");
            Log.d("geoLocate ", businessName + " " + bussinessLag + ", " + bussinessLong);
        }
    }
}
