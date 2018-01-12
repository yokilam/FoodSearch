package nyc.c4q.foodsearch.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nyc.c4q.foodsearch.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends Fragment {
    View v;

    public ThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_third, container, false);

        return v;
    }

}
