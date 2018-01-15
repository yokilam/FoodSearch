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

    View v;
    RecyclerView recyclerView;
    SavedRecycleView adapter;
    AHBottomNavigation bottom;
    List<Business> models = new ArrayList<>();

    float whenEmpty;


    private SharedPreferences log;
    private static final String SHARED_PREF_KEY = "MY_SAVED_LIST";
    SharedPreferences.Editor editor;

    ItemTouchHelper helper;

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_first, container, false);
        setupShared();
        bottom = getActivity().findViewById(R.id.bottom_navigation);
        setRecyclerView();
        setupTouch();
        helper.attachToRecyclerView(recyclerView);


        return v;
    }


    public void setRecyclerView() {


        recyclerView = (RecyclerView) v.findViewById(R.id.recycle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new SavedRecycleView(models);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {



                float tran = bottom.getTranslationY() + dy;

                whenEmpty = bottom.getTranslationY() + dy;

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
                models.remove(viewHolder.getAdapterPosition());

                try {
                    editor.remove(models.get(viewHolder.getAdapterPosition()).getId());
                    editor.commit();
                } catch (IndexOutOfBoundsException e) {
                }

                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                if (models.isEmpty() | log.getAll().isEmpty()){
                         bottom.setTranslationY(whenEmpty);
                }
            }
        });
    }

    public void setupShared() {
        log = v.getContext().getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        editor = log.edit();

        Map<String, ?> keys = log.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Gson gson = new Gson();
            String json = log.getString(entry.getKey(), null);
            Business obj = gson.fromJson(json, Business.class);

            models.add(obj);
        }
    }
}
