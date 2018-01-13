package nyc.c4q.foodsearch.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nyc.c4q.foodsearch.mode.view.Business;
import nyc.c4q.foodsearch.R;

/**
 * Created by jervon.arnoldd on 1/13/18.
 */

public class SavedRecycleView extends RecyclerView.Adapter<SavedRecycleView.Test_Holder> {
    List<Business> businessList;


    public SavedRecycleView(List<Business> businessList) {
        this.businessList = businessList;
    }

    @Override
    public SavedRecycleView.Test_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View child = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new Test_Holder(child);
    }

    @Override
    public void onBindViewHolder(SavedRecycleView.Test_Holder holder, int position) {
        Business business = businessList.get(position);
        Picasso.with(holder.context)
                .load(business.getImage_url())
                .into(holder.imageView);

        holder.address.setText(business.getLocation().getDisplay_address().get(0) + business.getLocation().getDisplay_address().get(1));
        holder.phoneNum.setText(business.getDisplay_phone());
        holder.businessName.setText(business.getName());
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public class Test_Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView businessName;
        TextView phoneNum;
        TextView address;
        Context context;

        public Test_Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            businessName = itemView.findViewById(R.id.store_title);
            phoneNum = itemView.findViewById(R.id.phone_number);
            address = itemView.findViewById(R.id.address);
            context = itemView.getContext();
        }
    }
}
