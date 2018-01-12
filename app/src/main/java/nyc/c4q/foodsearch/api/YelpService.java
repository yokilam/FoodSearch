package nyc.c4q.foodsearch.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by yokilam on 1/11/18.
 */

public interface YelpService {

    @GET("businesses/search")
    Call <ResponseBody> getResults(
            @Header("Authorization") String API_KEY,
            @Query("term") String term, @Query("longitude") double longitude,
            @Query("latitude") double latitude);


}
