package nyc.c4q.foodsearch.recycleview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import nyc.c4q.foodsearch.R;
import nyc.c4q.foodsearch.fragments.ThirdFragment;
import nyc.c4q.foodsearch.mode.view.Business;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

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
    public void onBindViewHolder(final SavedRecycleView.Test_Holder holder, int position) {
        final Business business = businessList.get(position);
        Picasso.with(holder.context)
                .load(business.getImage_url())
                .into(holder.imageView);

        holder.ratingBar.setRating((float) business.getRating());
        holder.rating.setText(String.valueOf(business.getRating()));

        holder.address.setText(business.getLocation().getDisplay_address().get(0) + " , " + business.getLocation().getDisplay_address().get(1));
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putDouble("long", business.getCoordinates().getLongitude());
                bundle.putDouble("lag", business.getCoordinates().getLatitude());
                bundle.putString("name", business.getName());
                Log.d(TAG, "onClick: " + business.getCoordinates().getLatitude() + " " + business.getCoordinates().getLongitude());
                ThirdFragment mfrag = new ThirdFragment();
                mfrag.setArguments(bundle);
                FragmentManager manager2 = ((FragmentActivity) holder.context).getSupportFragmentManager();
                FragmentTransaction transaction2 = manager2.beginTransaction();
                transaction2.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                transaction2.replace(R.id.container, mfrag);
                transaction2.commit();
            }
        });

        holder.phoneNum.setText(business.getDisplay_phone());
        holder.phoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:" + business.getDisplay_phone()));
                holder.context.startActivity(call);
            }
        });
        holder.businessName.setText(business.getName());

        if (holder.log.contains(business.getId())) {
            holder.shineButton.setChecked(true);
        } else if (!holder.log.contains(business.getId())) {
            holder.shineButton.setChecked(false);
        }

        ShineButton shineButtonJava = new ShineButton(holder.context.getApplicationContext());
        shineButtonJava.setBtnColor(Color.GRAY);
        shineButtonJava.setBtnFillColor(Color.RED);
        shineButtonJava.setShapeResource(R.raw.heart);
        shineButtonJava.setAllowRandomColor(true);
        shineButtonJava.setShineSize(50);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
        shineButtonJava.setLayoutParams(layoutParams);

        holder.shineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.log.contains(business.getId())) {
                    Gson gson = new Gson();
                    String json = gson.toJson(business);
                    holder.editor.putString(business.getId(), json);
                    holder.editor.commit();
                } else if (holder.log.contains(business.getId())) {
                    holder.editor.remove(business.getId()).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public class Test_Holder extends RecyclerView.ViewHolder {
        RatingBar ratingBar;
        ImageView imageView;
        TextView businessName, phoneNum, address, rating;
        Context context;
        ShineButton shineButton;

        private SharedPreferences log;
        private static final String SHARED_PREF_KEY = "MY_SAVED_LIST";
        private SharedPreferences.Editor editor;

        public Test_Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            businessName = itemView.findViewById(R.id.store_title);
            phoneNum = itemView.findViewById(R.id.phone_number);
            address = itemView.findViewById(R.id.address);
            context = itemView.getContext();
            shineButton = itemView.findViewById(R.id.po_image0);
            log = itemView.getContext().getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
            editor = log.edit();
            ratingBar = itemView.findViewById(R.id.rating_bar);
            rating = itemView.findViewById(R.id.rating);
        }
    }
}
