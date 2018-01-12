package nyc.c4q.foodsearch.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nyc.c4q.foodsearch.R;

/**
 * Created by jervon.arnoldd on 12/6/17.
 */

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ModelHOlder>  {

    List<Integer> list;

    public ModelAdapter(List<Integer> list) {
        this.list = list;

    }

    @Override
    public ModelHOlder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        for (int i = 0; i <= 40; i++) {
            list.add(i);
        }
        return new ModelHOlder(view);
    }

    @Override
    public void onBindViewHolder(ModelHOlder holder, int position) {
        int model = list.get(position);

        holder.view.setText(model+"");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ModelHOlder extends RecyclerView.ViewHolder {
        TextView view;


        public ModelHOlder(View itemView) {
            super(itemView);
            view = (TextView) itemView.findViewById(R.id.title);
        }

    }
}
