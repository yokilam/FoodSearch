package nyc.c4q.foodsearch;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.ArrayList;

import nyc.c4q.foodsearch.fragments.FirstFragment;
import nyc.c4q.foodsearch.fragments.SecondFragment;
import nyc.c4q.foodsearch.fragments.ThirdFragment;

public class MainActivity extends AppCompatActivity {

    FirstFragment favFrag = new FirstFragment();
    SecondFragment listFrag = new SecondFragment();
    ThirdFragment mapFrag = new ThirdFragment();

    private ArrayList<AHBottomNavigationItem> items = new ArrayList<>();
    AHBottomNavigation bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBottomNav();
    }

    public void setBottomNav() {
        bottom = findViewById(R.id.bottom_navigation);
        bottom.setCurrentItem(2);
//        bottom.setTranslucentNavigationEnabled(false);
        bottom.setBehaviorTranslationEnabled(false);
//        bottom.setColored(true);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Map", R.drawable.ic_location_searching_black_24dp);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("List", R.drawable.headline);
        final AHBottomNavigationItem item3 = new AHBottomNavigationItem("Favorites", R.drawable.blank_heart );

        items.add(item1);
        items.add(item2);
        items.add(item3);

        bottom.addItems(items);

//        bottom.setDefaultBackgroundColor(Color.RED);
//        bottom.setAccentColor(Color.RED);
//        bottom.setInactiveColor(Color.LTGRAY);

// Colors for selected (active) and non-selected items (in color reveal mode).
        bottom.setColoredModeColors(Color.WHITE, Color.LTGRAY);
        bottom.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        FragmentManager manager2 = getSupportFragmentManager();
                        FragmentTransaction transaction2 = manager2.beginTransaction();
                        transaction2.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction2.replace(R.id.container, mapFrag);
                        transaction2.commit();
                        break;
                    case 1:
                        FragmentManager manager1 = getSupportFragmentManager();
                        FragmentTransaction transaction1 = manager1.beginTransaction();
                        transaction1.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction1.replace(R.id.container, listFrag);
                        transaction1.commit();
                        break;
                    case 2:
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction.replace(R.id.container, favFrag);
                        transaction.commit();
                        break;
                }
                return true;
            }
        });
    }


}
