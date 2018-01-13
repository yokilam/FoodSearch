package nyc.c4q.foodsearch.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    View v;
    String term = "burger";
    private RecyclerView rv;
    List<Business> businessList = new ArrayList<>();
    private EditText userinput;

    AHBottomNavigation bottom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_second, container, false);

        bottom = getActivity().findViewById(R.id.bottom_navigation);
        rv = v.findViewById(R.id.food_rv);
//        MainActivity main= (MainActivity) v.getContext();
//        main.SetupRecyclerView();
        setupRetrofit();
        rv.addItemDecoration(new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));
        userinput = v.findViewById(R.id.search_edit);
        setup();


        String input = userinput.getText().toString();

        if (input.isEmpty()) {
            input = "";
        }

        return v;
    }

    public void setupRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.yelp.com/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YelpService yelpService = retrofit.create(YelpService.class);
        Call<BusinessModel> call = yelpService.getResults
                ("Bearer " + Constant.API_KEY, term, -73.9415728, 40.743309);
        call.enqueue(new Callback<BusinessModel>() {
            @Override
            public void onResponse(Call<BusinessModel> call, Response<BusinessModel> response) {
                try {
                    if (response.isSuccessful()) {
                        BusinessModel businessModel = response.body();
                        businessList = businessModel.getBusinesses();
                        rv.setAdapter(new BusinessAdapter(businessList));
                        Log.d("onResponse: ", "" + businessList);
                    } else
                        Log.d("onResponse: ", response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BusinessModel> call, Throwable t) {
                Log.d("onFailure: ", "" + t);
            }
        });
    }
//    public String input(String userinput){
//        String input = "";
//        if (userinput==null){
//            return input;
//        } else {
//            return input= userinput;
//        }
//    }

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

}
