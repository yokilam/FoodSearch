package nyc.c4q.foodsearch;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nyc.c4q.foodsearch.api.YelpService;
import nyc.c4q.foodsearch.constants.Constant;
import nyc.c4q.foodsearch.mode.view.Business;
import nyc.c4q.foodsearch.mode.view.BusinessModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by c4q on 1/20/18.
 */

public class Network_Call {

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.yelp.com/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private YelpService yelpService = retrofit.create(YelpService.class);
    private List<Business> businessList = new ArrayList<>();
    private List<Business> sortList = new ArrayList<>();
    private boolean imDone = false;


    public Network_Call() {
    }


    public void Network_Call(String term) {
        Call<BusinessModel> call = yelpService.getResults
                ("Bearer " + Constant.API_KEY, term, MainActivity.getCurrentLongitude(), MainActivity.getCurrentLatitude());
        call.enqueue(new Callback<BusinessModel>() {
            @Override
            public void onResponse(Call<BusinessModel> call, Response<BusinessModel> response) {
                try {
                    if (response.isSuccessful()) {
                        BusinessModel businessModel = response.body();
                        businessList = businessModel.getBusinesses();
                        Log.e("onResponse: ", "" + businessList);
                        imDone=true;
                    } else {
                        Log.e("onResponse: ", response.errorBody().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<BusinessModel> call, Throwable t) {
                Log.d("onFailure: ", "" + t.getMessage());
            }
        });
    }

    public List<Business> getSortedNetWork(String term, String sort) {
        final Call<BusinessModel> sortCall = yelpService.getSortRating
                ("Bearer " + Constant.API_KEY, term, MainActivity.getCurrentLongitude(), MainActivity.getCurrentLatitude(), sort);
        sortCall.enqueue(new Callback<BusinessModel>() {
            @Override
            public void onResponse(Call<BusinessModel> call, Response<BusinessModel> responseTwo) {
                BusinessModel sortingModel = responseTwo.body();
                Log.d(TAG, "onResponse: " + sortingModel.toString());
                sortList = sortingModel.getBusinesses();
                Log.d("Sort CAll worked", businessList.toString());
            }

            @Override
            public void onFailure(Call<BusinessModel> call, Throwable t) {

            }
        });
        return sortList;
    }


    public List<Business> getBusinessList() {
        return businessList;
    }

    public boolean isImDone() {
        return imDone;
    }
}




