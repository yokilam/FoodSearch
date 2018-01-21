package nyc.c4q.foodsearch.fragments;


import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nyc.c4q.foodsearch.MainActivity;
import nyc.c4q.foodsearch.Network_Call;
import nyc.c4q.foodsearch.mode.view.Business;
import nyc.c4q.foodsearch.recycleview.BusinessAdapter;
import nyc.c4q.foodsearch.mode.view.BusinessModel;
import nyc.c4q.foodsearch.R;
import nyc.c4q.foodsearch.api.YelpService;
import nyc.c4q.foodsearch.constants.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    private View v;
    private String term = "burger";
    private RecyclerView rv;
    List<Business> businessList = new ArrayList<>();
    List<Business> sortList = new ArrayList<>();
    private double c4qLat = 40.7429595;
    private double c4qLong = -73.9415728;

    private BusinessAdapter adapter;
    AHBottomNavigation bottom;
    LocationManager locationManager;
    private String rating = "rating";
    private SearchView searchView;
    Network_Call net;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_second, container, false);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


        bottom = getActivity().findViewById(R.id.bottom_navigation);
        rv = v.findViewById(R.id.food_rv);
        rv.addItemDecoration(new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new BusinessAdapter(businessList);
        rv.setAdapter(adapter);
        setupRetrofit(term);
        setup();
        net = new Network_Call();
        return v;
    }

    public void setupRetrofit(String term) {
        adapter.swap(net.Network_Call(term));
    }

    public void setup() {
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                float tran = bottom.getTranslationY() + dy;
                boolean scrooldown = dy > 0;

                if (scrooldown) {
                    tran = Math.min(tran, bottom.getHeight());
                } else {
                    tran = Math.max(tran, 0f);
                }
                bottom.setTranslationY(tran);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.option, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("onQueryTextSubmit", query);
                setupRetrofit(query);
//                UserFeedback.show( "SearchOnQueryTextSubmit: " + query);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, menuInflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_sort_rating:
                adapter.swap(net.getSortedNetWork(term, "rating"));
                break;
            case R.id.action_sort_distance:
                adapter.swap(net.getSortedNetWork(term, "distance"));
                break;
        }
        return true;
    }
}
