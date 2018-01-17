package nyc.c4q.foodsearch.recycleview;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import nyc.c4q.foodsearch.mode.view.Business;<<<<<<< HEAD
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
=======
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
>>>>>>> fb4b0c07829465616e42030584288a3c58c43007
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import nyc.c4q.foodsearch.mode.view.Business;
import nyc.c4q.foodsearch.R;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yokilam on 1/13/18.
 */

public class BusinessViewHolder extends RecyclerView.ViewHolder {


    private TextView name, address, rating, category;
    private ImageView businesslogo;
    private SharedPreferences log;
    private static final String SHARED_PREF_KEY = "MY_SAVED_LIST";
    private SharedPreferences.Editor editor;
    Context context;
    ShineButton shineButton;


    public BusinessViewHolder(View itemView) {
        super(itemView);
        shineButton = itemView.findViewById(R.id.po_image0);
        context = itemView.getContext();
        name = itemView.findViewById(R.id.name);
        address = itemView.findViewById(R.id.display_address);
        rating = itemView.findViewById(R.id.rating);
        category = itemView.findViewById(R.id.category);
        businesslogo = itemView.findViewById(R.id.business_image);

        log = itemView.getContext().getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        editor = log.edit();
    }

    public void onBind(final Business business) {
        name.setText(business.getName());
        StringBuilder fulladdress = new StringBuilder();
        fulladdress.append(business.getLocation().getDisplay_address().get(0)).append(", ").append(business.getLocation().getDisplay_address().get(1));
        address.setText(fulladdress.toString());
        rating.setText(String.valueOf(business.getRating()));
        category.setText(getCategories(business));

        Picasso.with(itemView.getContext())
                .load(business.getImage_url())
                .transform(new RoundedCornersTransformation(8, 8))
                .fit()
                .into(businesslogo);

        ShineButton shineButtonJava = new ShineButton(context.getApplicationContext());
        shineButtonJava.setBtnColor(Color.GRAY);
        shineButtonJava.setBtnFillColor(Color.RED);
        shineButtonJava.setShapeResource(R.raw.heart);
        shineButtonJava.setAllowRandomColor(true);
        shineButtonJava.setShineSize(50);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
        shineButtonJava.setLayoutParams(layoutParams);

        if (log.contains(business.getId())) {
            shineButton.setChecked(true);
        } else if (!log.contains(business.getId())) {
           shineButton.setChecked(false);
        }
        shineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!log.contains(business.getId())) {
                    Gson gson = new Gson();
                    String json = gson.toJson(business);
                    editor.putString(business.getId(), json);
                    editor.commit();
                } else if (log.contains(business.getId())) {
                    editor.remove(business.getId()).commit();
                }
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = business.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                itemView.getContext().startActivity(intent);
            }
        });
    }

    public String getCategories(Business business) {
        StringBuilder categories = new StringBuilder();
        for (int i = 0; i < business.getCategories().size(); i++) {
            categories.append(business.getCategories().get(i).getTitle()).append(", ");
        }
        return categories.substring(0, categories.length() - 2);
    }

}
