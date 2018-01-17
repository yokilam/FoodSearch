package nyc.c4q.foodsearch.recycleview;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
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
    private Button button;


    public BusinessViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        address = itemView.findViewById(R.id.display_address);
        rating = itemView.findViewById(R.id.rating);
        category = itemView.findViewById(R.id.category);
        businesslogo = itemView.findViewById(R.id.business_image);
        button = itemView.findViewById(R.id.button);

        log = itemView.getContext().getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        editor = log.edit();
    }

    public void onBind(final Business business) {
        if (log.contains(business.getId())) {
            button.setBackgroundResource(R.drawable.clicked_heart);
        } else if (!log.contains(business.getId())) {
            button.setBackgroundResource(R.drawable.heart);
        }

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!log.contains(business.getId())) {
                    button.setBackgroundResource(R.drawable.clicked_heart);
                    Gson gson = new Gson();
                    String json = gson.toJson(business);
                    editor.putString(business.getId(), json);
                    editor.commit();
                } else if (log.contains(business.getId())) {
                    editor.remove(business.getId()).commit();
                    button.setBackgroundResource(R.drawable.heart);
                }
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
