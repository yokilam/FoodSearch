package nyc.c4q.foodsearch.recycleview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import nyc.c4q.foodsearch.MainActivity;
import nyc.c4q.foodsearch.mode.view.Business;
import nyc.c4q.foodsearch.R;

import static android.content.ContentValues.TAG;

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

        holder.address.setText(business.getLocation().getDisplay_address().get(0) +" , "+ business.getLocation().getDisplay_address().get(1));
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri myCord = Uri.parse("geo:" + business.getCoordinates().getLatitude() + "," + business.getCoordinates().getLongitude());
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, myCord);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                holder.context.startActivity(mapIntent);

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

        ShineButton shineButtonJava = new ShineButton(holder.context.getApplicationContext());

        shineButtonJava.setBtnColor(Color.GRAY);
        shineButtonJava.setBtnFillColor(Color.RED);
        shineButtonJava.setShapeResource(R.raw.heart);
        shineButtonJava.setAllowRandomColor(true);
        shineButtonJava.setShineSize(50);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
        shineButtonJava.setLayoutParams(layoutParams);
//        if (linearLayout != null) {
//            linearLayout.addView(shineButtonJava);
//        }

        holder. shineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "click");
            }
        });



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
        ShineButton shineButton;


        public Test_Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            businessName = itemView.findViewById(R.id.store_title);
            phoneNum = itemView.findViewById(R.id.phone_number);
            address = itemView.findViewById(R.id.address);
            context = itemView.getContext();
            shineButton = itemView.findViewById(R.id.po_image0);
        }
    }
}
