package nyc.c4q.foodsearch.api;

import nyc.c4q.foodsearch.mode.view.BusinessModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by yokilam on 1/11/18.
 */

public interface YelpService {

    @GET("businesses/search")
    Call <BusinessModel> getResults(
            @Header("Authorization") String API_KEY,
            @Query("term") String term, @Query("longitude") double longitude,
            @Query("latitude") double latitude);

    @GET("businesses/search")
    Call<BusinessModel> getSortRating(
            @Header("Authorization") String API_KEY,
            @Query("term") String term, @Query("longitude") double longitude,
            @Query("latitude") double latitude,@Query("sort_by") String rating);
}
