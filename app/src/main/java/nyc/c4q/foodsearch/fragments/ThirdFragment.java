package nyc.c4q.foodsearch.fragments;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nyc.c4q.foodsearch.MainActivity;
import nyc.c4q.foodsearch.R;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends Fragment implements OnMapReadyCallback {
    private View v;
    GoogleMap mGoogleMap;
    private MapView mapView;
    private Double bussinessLag;
    private Double bussinessLong;
    private String businessName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_third, container, false);
        getCoordinates();
        geoLocate();

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

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(bussinessLag, bussinessLong))
                .title("something")
                .snippet("restaurant"));

        CameraPosition restaurant = CameraPosition.builder()
                .target(new LatLng(bussinessLag, bussinessLong))
                .zoom(16)
                .bearing(0)
                .tilt(45)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(restaurant));
    }

    public void getCoordinates() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            bussinessLong = bundle.getDouble("long");
            bussinessLag = bundle.getDouble("lag");
            businessName= bundle.getString("name");
        } else {
            bussinessLag= MainActivity.getCurrentLatitude();
            bussinessLong= MainActivity.getCurrentLongitude();
            Log.d(TAG, "getCoordinates: " + bussinessLag + " " + bussinessLong);
        }
    }

    private void geoLocate(){
        Geocoder geocoder= new Geocoder(getActivity());
        List<Address> list= new ArrayList <>();
        try {
            list = geocoder.getFromLocation(bussinessLag,bussinessLong, 1);
        }catch (IOException e) {
            Log.e(TAG, "geoLocate: " + e.getMessage());
        }
        if (list.size()>0) {
            Address address= list.get(0);
            Log.d(TAG, "geoLocate: found a location" + address.toString());
        }
    }
}
