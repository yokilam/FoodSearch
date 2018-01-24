package nyc.c4q.foodsearch.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.foodsearch.mode.view.Business;
import nyc.c4q.foodsearch.R;

/**
 * Created by yokilam on 1/13/18.
 */

public class BusinessAdapter extends RecyclerView.Adapter<BusinessViewHolder> {
    List<Business> businessList = new ArrayList<>();

    public BusinessAdapter(List<Business> businessList) {
        this.businessList = businessList;
    }

    public BusinessAdapter() {

    }

    @Override
    public BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View child = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_view, parent, false);
        return new BusinessViewHolder(child);
    }

    @Override
    public void onBindViewHolder(BusinessViewHolder holder, int position) {
        holder.onBind(businessList.get(position));
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public void swap(List<Business> list) {
        businessList.clear();
        businessList = list;
        notifyDataSetChanged();
    }
}
