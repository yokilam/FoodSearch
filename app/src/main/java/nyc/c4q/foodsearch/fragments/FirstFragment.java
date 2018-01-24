package nyc.c4q.foodsearch.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import nyc.c4q.foodsearch.mode.view.Business;
import nyc.c4q.foodsearch.R;
import nyc.c4q.foodsearch.recycleview.SavedRecycleView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {

    private View v;
    private RecyclerView recyclerView;
    private SavedRecycleView adapter;
    private AHBottomNavigation bottom;
    private List <Business> models = new ArrayList <>();
    private RelativeLayout relativeLayout;

    private float whenEmpty;

    private SharedPreferences log;
    private static final String SHARED_PREF_KEY = "MY_SAVED_LIST";
    private SharedPreferences.Editor editor;

    ItemTouchHelper helper;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_first, container, false);
        bottom = getActivity().findViewById(R.id.bottom_navigation);
        log = v.getContext().getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        editor = log.edit();
        relativeLayout = v.findViewById(R.id.relative);
        setRecyclerView();
        setupTouch();
        setupShared();

        helper.attachToRecyclerView(recyclerView);
        if (models.isEmpty()) {
            relativeLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        return v;
    }

    public void setRecyclerView() {
        recyclerView = v.findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SavedRecycleView(models);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                float tran = bottom.getTranslationY() + dy;
                boolean scrollDown = dy > 0;
                if (scrollDown) {
                    tran = Math.min(tran, bottom.getHeight());
                } else {
                    tran = Math.max(tran, 0f);
                }
                bottom.setTranslationY(tran);
                if (models.isEmpty()) {
                    Toast.makeText(v.getContext(), "Favorite List is  Empty", Toast.LENGTH_LONG).show();
                    recyclerView.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    bottom.restoreBottomNavigation();
                }
            }
        });
    }

    public void setupTouch() {

        helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
                ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(models, from, to);
                adapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String hello = models.get(viewHolder.getAdapterPosition()).getId();
                models.remove(viewHolder.getAdapterPosition());
                editor.remove(hello).commit();
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
    }

    public void setupShared() {
        Map <String, ?> keys = log.getAll();
        models.clear();
        for (String entry : keys.keySet()) {
            Gson gson = new Gson();
            String json = log.getString(entry, null);
            Business obj = gson.fromJson(json, Business.class);
            if (!models.contains(obj)) {
                models.add(obj);
            }
            if (models.isEmpty()) {
                relativeLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        editor.clear();
        editor.commit();
        for (int i = 0; i < models.size(); i++) {
            Gson gson = new Gson();
            String json = gson.toJson(models.get(i));
            editor.putString(models.get(i).getId(), json);
            editor.commit();
        }
    }
}
