package nyc.c4q.foodsearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yokilam on 1/13/18.
 */

public class BusinessViewHolder extends RecyclerView.ViewHolder {
    private TextView name, address;

    public BusinessViewHolder(View itemView) {
        super(itemView);
        name= itemView.findViewById(R.id.name);
        address= itemView.findViewById(R.id.display_address);
    }

    public void onBind(Business business) {
        name.setText(business.getName());
        StringBuilder fulladdress= new StringBuilder();
                fulladdress.append(business.getLocation().getDisplay_address().get(0)).append(business.getLocation().getDisplay_address().get(1));
        address.setText(fulladdress.toString());
    }
}
