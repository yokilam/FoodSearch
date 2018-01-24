package nyc.c4q.foodsearch.fragments;


import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nyc.c4q.foodsearch.MainActivity;
import nyc.c4q.foodsearch.R;
import nyc.c4q.foodsearch.api.YelpService;
import nyc.c4q.foodsearch.constants.Constant;
import nyc.c4q.foodsearch.mode.view.Business;
import nyc.c4q.foodsearch.mode.view.BusinessModel;
import nyc.c4q.foodsearch.recycleview.BusinessAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    private View v;
    private String term = "burger";
    private RecyclerView rv;
    private List <Business> businessList = new ArrayList <>();
    private List <Business> sortList = new ArrayList <>();

    private BusinessAdapter adapter;
    private AHBottomNavigation bottom;
    private LocationManager locationManager;
    private String rating = "rating";
    private SearchView searchView;
    private Network_Call net;
    private Network_Call network_call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        net = new Network_Call();
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_second, container, false);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        network_call = new Network_Call();
        bottom = getActivity().findViewById(R.id.bottom_navigation);
        rv = v.findViewById(R.id.food_rv);
        rv.addItemDecoration(new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new BusinessAdapter();
        rv.setAdapter(adapter);
        setupRetrofit(term);
        setup();
        return v;
    }

    public void setupRetrofit(final String term) {
        network_call.Network_Call(term);
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
                term = query;
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
                network_call.getSortedNetWork(term, "rating");
                break;
            case R.id.action_sort_distance:
                network_call.getSortedNetWork(term, "distance");
                break;
        }
        return true;
    }

    class Network_Call {

        private Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.yelp.com/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        private YelpService yelpService = retrofit.create(YelpService.class);

        public Network_Call() {
        }

        public void Network_Call(String term) {
            Call <BusinessModel> call = yelpService.getResults
                    ("Bearer " + Constant.API_KEY, term, MainActivity.getCurrentLongitude(), MainActivity.getCurrentLatitude());
            call.enqueue(new Callback <BusinessModel>() {
                @Override
                public void onResponse(Call <BusinessModel> call, Response <BusinessModel> response) {
                    try {
                        if (response.isSuccessful()) {
                            BusinessModel businessModel = response.body();
                            businessList = businessModel.getBusinesses();
                            Log.e("onResponse: ", "" + businessList);
                            adapter.swap(businessList);
                        } else {
                            Log.e("onResponse: ", response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call <BusinessModel> call, Throwable t) {
                    Log.d("onFailure: ", "" + t.getMessage());
                }
            });
        }

        public void getSortedNetWork(String term, String sort) {
            final Call <BusinessModel> sortCall = yelpService.getSortRating
                    ("Bearer " + Constant.API_KEY, term, MainActivity.getCurrentLongitude(), MainActivity.getCurrentLatitude(), sort);
            sortCall.enqueue(new Callback <BusinessModel>() {
                @Override
                public void onResponse(Call <BusinessModel> call, Response <BusinessModel> responseTwo) {
                    BusinessModel sortingModel = responseTwo.body();
                    Log.d(TAG, "onResponse: " + sortingModel.toString());
                    sortList = sortingModel.getBusinesses();
                    Log.d("Sort CAll worked", businessList.toString());
                    adapter.swap(sortList);
                }

                @Override
                public void onFailure(Call <BusinessModel> call, Throwable t) {

                }
            });
        }

    }
}
