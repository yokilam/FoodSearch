package nyc.c4q.foodsearch.recycleview;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import nyc.c4q.foodsearch.mode.view.Business;
import nyc.c4q.foodsearch.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yokilam on 1/13/18.
 */

public class BusinessViewHolder extends RecyclerView.ViewHolder {
    private TextView name, address;

    private SharedPreferences log;
    private static final String SHARED_PREF_KEY = "MY_SAVED_LIST";
    SharedPreferences.Editor editor;
    Button button;


    public BusinessViewHolder(View itemView) {
        super(itemView);
        name= itemView.findViewById(R.id.name);
        address= itemView.findViewById(R.id.display_address);


        button=itemView.findViewById(R.id.button);

        log = itemView.getContext().getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        editor = log.edit();
    }

    public void onBind(final Business business) {
        name.setText(business.getName());
        StringBuilder fulladdress= new StringBuilder();
                fulladdress.append(business.getLocation().getDisplay_address().get(0)).append(business.getLocation().getDisplay_address().get(1));
        address.setText(fulladdress.toString());



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!log.contains(business.getId())){
                    Gson gson = new Gson();
                    String json = gson.toJson(business);

                    editor.putString(business.getId(), json);
                    editor.commit();

                }
            }
        });
    }
}
