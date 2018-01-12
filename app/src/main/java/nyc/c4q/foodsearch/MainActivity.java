package nyc.c4q.foodsearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String API_KEY = "2LI-rkohxty2ARYiC8vQcKhkm7SatgG4dsSVaySaeDWYqKWWTXqwpnhFNjMlqNHPpTwnTKhF0EvPn28M7oDGXr0VVms3XBfPPWmfbdOQ_KMTUGRQvVwUvjMA_vNXWnYx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String term = "burger";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.yelp.com/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YelpService yelpService = retrofit.create(YelpService.class);
        Call <ResponseBody> call = yelpService.getResults
                ("Bearer " + API_KEY, "11212", -73.9415728, 40.743309);
        call.enqueue(new Callback <ResponseBody>() {
            @Override
            public void onResponse(Call <ResponseBody> call, Response <ResponseBody> response) {
                try {
                    if (response.isSuccessful())
                        Log.d("onResponse: ", response.body().string());
                    else
                        Log.d("onResponse: ", response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call <ResponseBody> call, Throwable t) {
                Log.d("onFailure: ", "" + t);
            }
        });
    }
}
