package nyc.c4q.foodsearch;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nyc.c4q.foodsearch.api.YelpService;
import nyc.c4q.foodsearch.constants.Constant;
import nyc.c4q.foodsearch.fragments.FirstFragment;
import nyc.c4q.foodsearch.fragments.SecondFragment;
import nyc.c4q.foodsearch.fragments.ThirdFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String term = "burger";

    FirstFragment first = new FirstFragment();
    SecondFragment second = new SecondFragment();
    ThirdFragment third = new ThirdFragment();

    private ArrayList <AHBottomNavigationItem> items = new ArrayList <>();

    AHBottomNavigation bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom = findViewById(R.id.bottom_navigation);

        setBottomNav();

    }

    public void setBottomNav() {
        bottom = findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Favorites", R.drawable.star_black);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("List", R.drawable.headline);
        final AHBottomNavigationItem item3 = new AHBottomNavigationItem("Map", R.drawable.ic_location_searching_black_24dp);

        items.add(item1);
        items.add(item2);
        items.add(item3);

        bottom.addItems(items);

//        bottom.setDefaultBackgroundColor(Color.RED);
//        bottom.setAccentColor(Color.RED);
//        bottom.setInactiveColor(Color.LTGRAY);

        bottom.setCurrentItem(1);
        bottom.setTranslucentNavigationEnabled(true);
        bottom.setBehaviorTranslationEnabled(true);
        bottom.setColored(true);
// Colors for selected (active) and non-selected items (in color reveal mode).
        bottom.setColoredModeColors(Color.WHITE, Color.LTGRAY);

        bottom.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position) {
                    case 0:
                        item3.setDrawable(R.drawable.ic_location_searching_black_24dp);

                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction.replace(R.id.container, first);
                        transaction.commit();
                        break;
                    case 1:
                        item3.setDrawable(R.drawable.ic_location_searching_black_24dp);

                        FragmentManager manager1 = getSupportFragmentManager();
                        FragmentTransaction transaction1 = manager1.beginTransaction();
                        transaction1.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction1.replace(R.id.container, second);
                        transaction1.commit();
                        break;
                    case 2:
                        item3.setDrawable(R.drawable.ic_location_searching_black_24dp);
                        FragmentManager manager2 = getSupportFragmentManager();
                        FragmentTransaction transaction2 = manager2.beginTransaction();
                        transaction2.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction2.replace(R.id.container, third);
                        transaction2.commit();
                        break;
                }
                return true;
            }
        });
    }


}
